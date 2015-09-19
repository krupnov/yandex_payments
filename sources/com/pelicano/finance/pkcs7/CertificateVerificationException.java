package com.pelicano.finance.serialization.pkcs7;

public class CertificateVerificationException extends Exception {
	private static final long serialVersionUID = 1L;
 
	public CertificateVerificationException(String message, Throwable cause) {
		super(message, cause);
	}
 
	public CertificateVerificationException(String message) {
		super(message);
	}
}