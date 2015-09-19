package com.pelicano.finance.yandex;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import com.pelicano.finance.mvc.Pkcs7MessageBase;
import com.pelicano.finance.serialization.DateTimeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckOrderRequest extends Pkcs7MessageBase {
	@XmlAttribute
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private DateTime requestDateTime;
	@XmlAttribute
	private String action;
	@XmlAttribute
	private String md5;
	@XmlAttribute
	private long shopId;
	@XmlAttribute
	private long shopArticleId;
	@XmlAttribute
	private long invoiceId;
	@XmlAttribute
	private String orderNumber;
	@XmlAttribute
	private String customerNumber;
	@XmlAttribute
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private DateTime orderCreatedDatetime;
	@XmlAttribute
	private String orderSumAmount;
	@XmlAttribute
	private String orderSumCurrencyPaycash;
	@XmlAttribute
	private String orderSumBankPaycash;
	@XmlAttribute
	private String shopSumAmount;
	@XmlAttribute
	private String shopSumCurrencyPaycash;
	@XmlAttribute
	private String shopSumBankPaycash;
	@XmlAttribute
	private String paymentPayerCode;
	@XmlAttribute
	private String paymentType;

	public DateTime getRequestDateTime() {
		return requestDateTime;
	}
	public void setRequestDateTime(DateTime requestDateTime) {
		this.requestDateTime = requestDateTime;
	}

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	public long getShopId() {
		return shopId;
	}
	public void setShopId(long shopId) {
		this.shopId = shopId;
	}
	
	public long getShopArticleId() {
		return shopArticleId;
	}
	public void setShopArticleId(long shopArticleId) {
		this.shopArticleId = shopArticleId;
	}
	
	public long getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public String getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	
	public DateTime getOrderCreatedDatetime() {
		return orderCreatedDatetime;
	}
	public void setOrderCreatedDatetime(DateTime orderCreatedDatetime) {
		this.orderCreatedDatetime = orderCreatedDatetime;
	}
	
	public String getOrderSumAmount() {
		return orderSumAmount;
	}
	public void setOrderSumAmount(String orderSumAmount) {
		this.orderSumAmount = orderSumAmount;
	}
	
	public String getOrderSumCurrencyPaycash() {
		return orderSumCurrencyPaycash;
	}
	public void setOrderSumCurrencyPaycash(String orderSumCurrencyPaycash) {
		this.orderSumCurrencyPaycash = orderSumCurrencyPaycash;
	}
	
	public String getOrderSumBankPaycash() {
		return orderSumBankPaycash;
	}
	public void setOrderSumBankPaycash(String orderSumBankPaycash) {
		this.orderSumBankPaycash = orderSumBankPaycash;
	}
	
	public String getShopSumAmount() {
		return shopSumAmount;
	}
	public void setShopSumAmount(String shopSumAmount) {
		this.shopSumAmount = shopSumAmount;
	}
	
	public String getShopSumCurrencyPaycash() {
		return shopSumCurrencyPaycash;
	}
	public void setShopSumCurrencyPaycash(String shopSumCurrencyPaycash) {
		this.shopSumCurrencyPaycash = shopSumCurrencyPaycash;
	}
	
	public String getShopSumBankPaycash() {
		return shopSumBankPaycash;
	}
	public void setShopSumBankPaycash(String shopSumBankPaycash) {
		this.shopSumBankPaycash = shopSumBankPaycash;
	}
	
	public String getPaymentPayerCode() {
		return paymentPayerCode;
	}
	public void setPaymentPayerCode(String paymentPayerCode) {
		this.paymentPayerCode = paymentPayerCode;
	}
	
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
}