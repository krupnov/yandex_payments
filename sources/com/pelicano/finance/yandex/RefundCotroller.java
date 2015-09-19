package com.pelicano.finance.yandex;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pelicano.finance.mvc.Pkcs7MessageBase.Status;

	
@Controller
public class RefundCotroller {
	public static final String TEST_CHECK_ORDER_URL = "/checkrefundtest";
	public static final String TEST_PAYMENT_AVISO_URL = "/avisorefundtest";
	
	private final Logger logger = LogManager.getLogger(RefundCotroller.class);
	
	@RequestMapping(value=TEST_CHECK_ORDER_URL, method=RequestMethod.POST, produces = "application/xml")
	public @ResponseBody CheckOrderResponse testCheckOrder(
			@RequestBody CheckOrderRequest request) {
		CheckOrderResponse response = new CheckOrderResponse();
		return convertToResponse(request, response);
	}

	@RequestMapping(value=TEST_PAYMENT_AVISO_URL, method=RequestMethod.POST, produces = "application/xml")
	public @ResponseBody PaymentAvisoResponse testPaymentAvisio(
			@RequestBody PaymentAvisoRequest request) {
		logger.info("Got aviso request");
		PaymentAvisoResponse response = new PaymentAvisoResponse();
		return (PaymentAvisoResponse)convertToResponse(request, response);
	}
	
	private static CheckOrderResponse convertToResponse(CheckOrderRequest request,
			CheckOrderResponse response) {
		response.setPerformedDatetime(DateTime.now());
		if (request.status == Status.Ok) {
			response.setCode(CheckOrderResponse.SUCCESS_CODE);
			response.setInvoiceId(request.getInvoiceId());
			response.setOrderSumAmount(request.getOrderSumAmount());
			response.setShopId(request.getShopId());
		} else {
			response.setCode(CheckOrderResponse.NOT_AUTHORIZED_CODE);
			response.setMessage("Pkcs7 container not signed correctly");
		}
		return response;
	}
}