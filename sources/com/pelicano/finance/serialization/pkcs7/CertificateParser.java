package com.pelicano.finance.serialization.pkcs7;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.bouncycastle.cms.CMSException;

public class CertificateParser {
	public X509Certificate parseCertificateInDerFormat(InputStream in) throws CMSException, IOException, CertificateException {
		CertificateFactory factory = CertificateFactory.getInstance("X.509");
		X509Certificate cert = (X509Certificate) factory.generateCertificate(in);
		return cert;
	}
}