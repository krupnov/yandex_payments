package com.pelicano.finance.mvc;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.cms.CMSException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.pelicano.finance.serialization.InvalidSignatureException;
import com.pelicano.finance.serialization.SerializationException;
import com.pelicano.finance.serialization.pkcs7.CRLVerifier;
import com.pelicano.finance.serialization.pkcs7.CertificateParser;
import com.pelicano.finance.serialization.pkcs7.CmsHelper;
import com.pelicano.finance.serialization.pkcs7.PEMHelper;
import com.pelicano.finance.serialization.pkcs7.Pkcs7Serializator;
import com.pelicano.finance.serialization.pkcs7.XmlHelper;

public class Pkc7HttpMessageConverter extends AbstractHttpMessageConverter<Pkcs7MessageBase> {

	private static final String CERTIFICATE_BUCKET = "yandex.certificates";
	private static final String CERTIFICATE_FOLDER = "certificates/";
	private static final String CRL_FOLDER = "crls/";
	
	private final Logger logger = LogManager.getLogger(Pkc7HttpMessageConverter.class);
	
	private Pkcs7Serializator serializator;
	private final AmazonS3Client client;
	
	public Pkc7HttpMessageConverter(
			AmazonS3Client client) {
		super(MediaType.parseMediaType("application/pkcs7-mime"));
		this.client = client;
	}
	
	@Override
	protected boolean supports(Class<?> clazz) {
		return Pkcs7MessageBase.class.isAssignableFrom(clazz);
	}

	@Override
	protected Pkcs7MessageBase readInternal(
			Class<? extends Pkcs7MessageBase> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		if (serializator == null) {
			initSerializator();
		}
		try {
			Pkcs7MessageBase requestContainer = serializator.deserialize(
					clazz,
					inputMessage.getBody());
			requestContainer.status = Pkcs7MessageBase.Status.Ok;
			return requestContainer;
		} catch (SerializationException err) {
			throw new HttpMessageNotReadableException("Unable to deserialize pkcs7 request body: " + err);
		} catch (InvalidSignatureException err) {
			Pkcs7MessageBase requestContainer = new Pkcs7MessageBase();
			requestContainer.status = Pkcs7MessageBase.Status.AuthenticationError;
			return requestContainer;
		}
	}

	private synchronized void initSerializator() {
		if (serializator == null) {
			Set<X509Certificate> chainVerificationCerts = loadCertificatesFromS3();
			CRLVerifier crlVerifier = new CRLVerifier();
			addCrls(crlVerifier, chainVerificationCerts);
			serializator = new Pkcs7Serializator(
					new CmsHelper(chainVerificationCerts, crlVerifier),
					new PEMHelper(),
					new XmlHelper());
		}
	}

	private void addCrls(CRLVerifier crlVerifier, Set<X509Certificate> chainVerificationCerts) {
		final ListObjectsRequest listCertificateRequest = new ListObjectsRequest().
				withBucketName(CERTIFICATE_BUCKET).
				withPrefix(CRL_FOLDER);
		final ObjectListing certificateListing = client.listObjects(listCertificateRequest);
		for (S3ObjectSummary objectSummary : certificateListing.getObjectSummaries()) {
			if (objectSummary.getKey().equals(CRL_FOLDER)) {
				continue;
			}
			S3Object object = client.getObject(
					objectSummary.getBucketName(), objectSummary.getKey());
			try {
				if (!crlVerifier.tryAddCrl(object.getObjectContent(), chainVerificationCerts)) {
					logger.warn("Unable to load CRL: " + objectSummary.getKey());
				}
			} catch (InvalidKeyException | CertificateException | CRLException
					| NoSuchAlgorithmException | NoSuchProviderException
					| SignatureException e) {
				logger.warn("Unable to load CRL: " + objectSummary.getKey(), e);
			}
		}
	}

	private Set<X509Certificate> loadCertificatesFromS3() {
		final ListObjectsRequest listCertificateRequest = new ListObjectsRequest().
				withBucketName(CERTIFICATE_BUCKET).
				withPrefix(CERTIFICATE_FOLDER);
		final ObjectListing certificateListing = client.listObjects(listCertificateRequest);
		CertificateParser parser = new CertificateParser();
		Set<X509Certificate> chainVerificationCerts = new HashSet<X509Certificate>();
		for (S3ObjectSummary objectSummary : certificateListing.getObjectSummaries()) {
			if (objectSummary.getKey().equals(CERTIFICATE_FOLDER)) {
				continue;
			}
			S3Object object = client.getObject(
					objectSummary.getBucketName(), objectSummary.getKey());
			try {
				chainVerificationCerts.add(parser.parseCertificateInDerFormat(object.getObjectContent()));
			} catch (CertificateException | CMSException | IOException e) {
				throw new RuntimeException("Unable to load certificate from s3 bucket", e);
			}
		}
		return chainVerificationCerts;
	}

	@Override
	protected void writeInternal(Pkcs7MessageBase t,
			HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		// TODO Auto-generated method stub
	}
}