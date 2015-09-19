package com.pelicano.finance.serialization.pkcs7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.util.encoders.Base64;

public class PEMHelper {
	public CMSSignedData readPEMContainer(InputStream body)
			throws UnsupportedEncodingException, IOException, CMSException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(body, "UTF-8"));
		String nextLine = reader.readLine();
		if (nextLine != null) {
			nextLine = reader.readLine();
		}
		StringBuffer buffer = new StringBuffer();
		while (nextLine != null) {
			String toWrite = nextLine;
			nextLine = reader.readLine();
			if (nextLine != null) {
				buffer.append(toWrite);
			}
		}
		return new CMSSignedData(Base64.decode(buffer.toString()));
	}
}