package com.pelicano.finance.mvc;

import javax.xml.bind.annotation.XmlTransient;

public class Pkcs7MessageBase {
	public static enum Status {
		Ok,
		AuthenticationError
	}
	
	@XmlTransient
	public Status status;
}