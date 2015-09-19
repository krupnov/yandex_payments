package com.pelicano.finance.yandex;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

import com.pelicano.finance.serialization.DateTimeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckOrderResponse {
	public static final int SUCCESS_CODE = 0;
	public static final int NOT_AUTHORIZED_CODE = 1;
	public static final int REFUSE_PAYMENT_CODE = 100;
	public static final int PARSE_ERROR_CODE = 200;
	
	@XmlAttribute
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	private DateTime performedDatetime;
	@XmlAttribute
	private int code;
	@XmlAttribute
	private long shopId;
	@XmlAttribute
	private long invoiceId;
	@XmlAttribute
	private String orderSumAmount;
	@XmlAttribute
	private String message;
	@XmlAttribute
	private String techMessage;
	
	public CheckOrderResponse() {
	}
	
	public CheckOrderResponse(
			DateTime performedDatetime,
			int code,
			long shopId,
			long invoiceId,
			String orderSumAmount) {
		this.performedDatetime = performedDatetime;
		this.code = code;
		this.shopId = shopId;
		this.invoiceId = invoiceId;
		this.orderSumAmount = orderSumAmount;
	}

	public DateTime getPerformedDatetime() {
		return performedDatetime;
	}
	public void setPerformedDatetime(DateTime performedDatetime) {
		this.performedDatetime = performedDatetime;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

	public long getShopId() {
		return shopId;
	}
	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

	public long getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getOrderSumAmount() {
		return orderSumAmount;
	}
	public void setOrderSumAmount(String orderSumAmount) {
		this.orderSumAmount = orderSumAmount;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getTechMessage() {
		return techMessage;
	}
	public void setTechMessage(String techMessage) {
		this.techMessage = techMessage;
	}
}