package com.pelicano.finance.serialization.pkcs7;

public class InvalidSignatureException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7376675282725067998L;

	public InvalidSignatureException(String message) {
		super(message);
	}

	public InvalidSignatureException(String message, Exception e) {
		super(message, e);
	}
}