package com.pelicano.finance.yandex;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import com.pelicano.finance.serialization.DateTimeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentAvisoRequest extends CheckOrderRequest {
	@XmlAttribute
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private DateTime paymentDatetime;
	@XmlAttribute
	private String cps_user_country_code;
	
	public DateTime getPaymentDatetime() {
		return paymentDatetime;
	}
	public void setPaymentDatetime(DateTime paymentDatetime) {
		this.paymentDatetime = paymentDatetime;
	}
	public String getCps_user_country_code() {
		return cps_user_country_code;
	}
	public void setCps_user_country_code(String cps_user_country_code) {
		this.cps_user_country_code = cps_user_country_code;
	}
}