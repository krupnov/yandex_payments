package com.pelicano.finance.serialization.pkcs7;

import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.security.cert.CertificateException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;

import com.pelicano.finance.dao.s3.ByteArrayInOutStream;
import com.pelicano.finance.serialization.SerializationException;

public class Pkcs7Serializator {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	private final CmsHelper cmsHelper;
	private final PEMHelper pemHelper;
	private final XmlHelper xmlHelper;
	
	public Pkcs7Serializator(
			CmsHelper cmsHelper,
			PEMHelper pemHelper,
			XmlHelper xmlHelper) {
		this.cmsHelper = cmsHelper;
		this.pemHelper = pemHelper;
		this.xmlHelper = xmlHelper;
	}
	
	public <T> T deserialize(Class<?> clazz, InputStream body) 
			throws InvalidSignatureException, IOException, SerializationException {
		try {
			CMSSignedData cms = pemHelper.readPEMContainer(body);
			cmsHelper.verifyCmsSignatures(cms);
			ByteArrayInOutStream out = new ByteArrayInOutStream();
			cms.getSignedContent().write(out);
			return xmlHelper.deserialize(clazz, out.getInputStream());
		} catch (CMSException | CertificateException | OperatorCreationException | XMLStreamException | JAXBException e) {
			throw new SerializationException("Unable to parse CMS packet", e);
		}
	}
}