package com.pelicano.finance.serialization;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

public class DateTimeAdapter extends XmlAdapter<String, DateTime> {

	@Override
	public DateTime unmarshal(String v) throws Exception {
		return new DateTime(v);
	}

	@Override
	public String marshal(DateTime v) throws Exception {
		return v == null ? null : v.toString(ISODateTimeFormat.dateTime());
	}
}