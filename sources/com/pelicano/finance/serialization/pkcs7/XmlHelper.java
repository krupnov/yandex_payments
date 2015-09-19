package com.pelicano.finance.serialization.pkcs7;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

public class XmlHelper {
	
	@SuppressWarnings("unchecked")
	public <T> T deserialize(Class<?> clazz, InputStream in) throws XMLStreamException, JAXBException {
		return (T) JAXBContext.newInstance(clazz).createUnmarshaller().unmarshal(in);
	}
}