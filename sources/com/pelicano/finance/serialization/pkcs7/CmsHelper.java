package com.pelicano.finance.serialization.pkcs7;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;

public class CmsHelper {
	
	private final Set<X509Certificate> chainVerificationCerts;
	private final CRLVerifier crlVerifier;
	
	public CmsHelper(
			Set<X509Certificate> chainVerificationCerts,
			CRLVerifier crlVerifier) {
		this.chainVerificationCerts = chainVerificationCerts;
		this.crlVerifier = crlVerifier;
	}
	
	@SuppressWarnings("unchecked")
	public void verifyCmsSignatures(CMSSignedData cms)
			throws CertificateException, CMSException,
			OperatorCreationException, InvalidSignatureException {
		Store<X509CertificateHolder> certStore = cms.getCertificates();
		SignerInformationStore signers = cms.getSignerInfos();
		Collection<SignerInformation> c = signers.getSigners(); 
		Iterator<SignerInformation> it = c.iterator();
		while (it.hasNext()) { 
			SignerInformation signer = it.next(); 
			Collection<X509CertificateHolder> certCollection = certStore.getMatches(signer.getSID()); 
			Iterator<X509CertificateHolder> certIt = certCollection.iterator();
			X509CertificateHolder certHolder = certIt.next();
			X509Certificate cert = 
					new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
			if (!signer.verify(
					new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert))) {
				throw new InvalidSignatureException("Packet signature is invalid");
			}
			
			verifyCertificate(cert);
		 }
	}

	private void verifyCertificate(X509Certificate cert) throws InvalidSignatureException {
		try {
			verifyCertificate(cert, chainVerificationCerts);
			
		} catch (CertificateVerificationException e) {
			throw new InvalidSignatureException("Certificate verification failed", e);
		}
	}
	
	private PKIXCertPathBuilderResult verifyCertificate(X509Certificate cert,
			Set<X509Certificate> additionalCerts) throws CertificateVerificationException {
		try {
			// Check for self-signed certificate
			if (isSelfSigned(cert)) {
				throw new CertificateVerificationException("The certificate is self-signed.");
			}
			 
			// Prepare a set of trusted root CA certificates
			// and a set of intermediate certificates
			Set<X509Certificate> trustedRootCerts = new HashSet<X509Certificate>();
			Set<X509Certificate> intermediateCerts = new HashSet<X509Certificate>();
			for (X509Certificate additionalCert : additionalCerts) {
				if (isSelfSigned(additionalCert)) {
					trustedRootCerts.add(additionalCert);
				} else {
					intermediateCerts.add(additionalCert);
				}
			}
			 
			// Attempt to build the certification chain and verify it
			PKIXCertPathBuilderResult verifiedCertChain = 
				verifyCertificate(cert, trustedRootCerts, intermediateCerts);
			// Check whether the certificate is revoked by the CRL
			// given in its CRL distribution point extension
			crlVerifier.verifyCertificateCRLs(cert);
			 
			// The chain is built and verified. Return it as a result
			return verifiedCertChain;
		} catch (GeneralSecurityException certPathEx) {
			throw new CertificateVerificationException(
				"Error verifying the certificate: " + 
				cert.getSubjectX500Principal(), certPathEx);
		}
	}
	
	private static PKIXCertPathBuilderResult verifyCertificate(X509Certificate cert, 
			Set<X509Certificate> trustedRootCerts,
			Set<X509Certificate> intermediateCerts) throws GeneralSecurityException {
		// Create the selector that specifies the starting certificate
		X509CertSelector selector = new X509CertSelector(); 
		selector.setCertificate(cert);
		 
		// Create the trust anchors (set of root CA certificates)
		Set<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>();
		for (X509Certificate trustedRootCert : trustedRootCerts) {
			trustAnchors.add(new TrustAnchor(trustedRootCert, null));
		}
		 
		// Configure the PKIX certificate builder algorithm parameters
		PKIXBuilderParameters pkixParams = 
			new PKIXBuilderParameters(trustAnchors, selector);
		 
		// Disable CRL checks (this is done manually as additional step)
		pkixParams.setRevocationEnabled(false);
		 
		// Specify a list of intermediate certificates
		CertStore intermediateCertStore = CertStore.getInstance("Collection",
				new CollectionCertStoreParameters(intermediateCerts), "BC");
		pkixParams.addCertStore(intermediateCertStore);
		 
			// Build and verify the certification chain
		CertPathBuilder builder = CertPathBuilder.getInstance("PKIX", "BC");
		PKIXCertPathBuilderResult result = 
			(PKIXCertPathBuilderResult) builder.build(pkixParams);
		return result;
	}
	
	/**
	 * Checks whether given X.509 certificate is self-signed.
	 */
	private static boolean isSelfSigned(X509Certificate cert) 
			throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException {
		try {
			// Try to verify certificate signature with its own public key
			PublicKey key = cert.getPublicKey();
			cert.verify(key);
			return true;
		} catch (SignatureException sigEx) {
			// Invalid signature --> not self-signed
			return false;
		} catch (InvalidKeyException keyEx) {
			// Invalid key --> not self-signed
			return false;
		}
	}
}