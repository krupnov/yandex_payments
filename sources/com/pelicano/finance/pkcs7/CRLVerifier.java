package com.pelicano.finance.serialization.pkcs7;

import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CRLVerifier {

	private final List<X509CRL> crls = new LinkedList<X509CRL>();
	
	public boolean tryAddCrl(InputStream in, Collection<X509Certificate> issuers) 
			throws CertificateException, CRLException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509CRL crl = (X509CRL) cf.generateCRL(in);
		
		for (X509Certificate issuer : issuers) {
			try {
				crl.verify(issuer.getPublicKey());
				crls.add(crl);
				return true;
			} catch (CRLException | SignatureException e) {
			}
		}
		return false;
	}
	
	public void verifyCertificateCRLs(X509Certificate cert) throws CertificateVerificationException {
		for (X509CRL crl : crls) {
			if (crl.isRevoked(cert)) {
				throw new CertificateVerificationException(
					"The certificate is revoked by CRL: " + crl.toString());
			}
		}
	}
}
