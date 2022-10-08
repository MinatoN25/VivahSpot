package com.stackroute.payment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RelatedResources;
import com.paypal.api.payments.Sale;
import com.paypal.api.payments.Transaction;
import com.stackroute.payment.constants.PayPalConstants;
import com.stackroute.payment.exception.BookingNotFoundException;
import com.stackroute.payment.exception.InvoiceNotFoundException;
import com.stackroute.payment.exception.UnauthorizedException;
import com.stackroute.payment.model.Invoice;
import com.stackroute.payment.service.PayPalService;

@RunWith(SpringRunner.class)
@WebMvcTest(PayPalController.class)
class PayPalControllerTest {

	@Autowired
	private MockMvc mock;

	@MockBean
	private PayPalService payPalService;

	@MockBean
	private RabbitTemplate rabbitTemplate;

	@Value("${server.port}")
	public String port;

	@InjectMocks
	private PayPalController bookingController;

	private String userToken;

	@Test
	void testGetInvoiceDetailsSuccess() throws Exception {
		userToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5pc2hAZ21haWwuY29tIiwiaWF0IjoxNjYzNDEwOTYyfQ.jkDylYu-Rf4oEMg39t_aYYqWZ2wQG5aVvwshR3fFoI05sZB_XKbfMqV_JMbVC1UOlpfJOE0l24Qep14uuu1ErQ";
		Invoice invoice = new Invoice();
		invoice.setCurrency("GBP");
		invoice.setInvoiceId("42");
		invoice.setPaymentMethod("Paypal");
		invoice.setPaymentMode("Instant");
		invoice.setPaymentStatus("Completed");
		invoice.setBookingId(1);
		invoice.setUserEmail("manish@gmail.com");
		invoice.setVenueOwnerEmail("owner@gmail.com");
		LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
		invoice.setPaymentTime(atStartOfDayResult.toString());
		invoice.setTotalAmount(10.00);
		when(payPalService.getInvoiceDetailsByInvoiceId("42", "manish@gmail.com")).thenReturn(invoice);
		mock.perform(
				get("/api/v1/payment/getInvoiceDetails/{invoiceId}", 42).header("Authorization", "Bearer " + userToken))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"invoiceId\":\"42\",\"bookingId\":1,\"userEmail\":\"manish@gmail.com\",\"venueOwnerEmail\":\"owner@gmail.com\",\"paymentMethod\":\"Paypal\",\"currency\":\"GBP\",\"totalAmount\":10.0,\"paymentMode\":\"Instant\",\"paymentStatus\":\"Completed\",\"paymentTime\":\"1970-01-01T00:00\",\"bookingDate\":null,\"venueName\":null}"))
				.andDo(print());
	}

	@Test
	void testGetInvoiceDetailsInvoiceNotFoundException() throws Exception {
		userToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5pc2hAZ21haWwuY29tIiwiaWF0IjoxNjYzNDEwOTYyfQ.jkDylYu-Rf4oEMg39t_aYYqWZ2wQG5aVvwshR3fFoI05sZB_XKbfMqV_JMbVC1UOlpfJOE0l24Qep14uuu1ErQ";
		when(payPalService.getInvoiceDetailsByInvoiceId((String) any(), (String) any()))
				.thenThrow(new InvoiceNotFoundException("Invoice not found"));

		mock.perform(
				get("/api/v1/payment/getInvoiceDetails/{invoiceId}", 42).header("Authorization", "Bearer " + userToken))
				.andExpect(status().isConflict()).andExpect(MockMvcResultMatchers.content().string("Invoice not found"))
				.andDo(print());

	}

	@Test
	void testGetInvoiceDetailsUnauthorizedException() throws Exception {
		userToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5pc2hAZ21haWwuY29tIiwiaWF0IjoxNjYzNDEwOTYyfQ.jkDylYu-Rf4oEMg39t_aYYqWZ2wQG5aVvwshR3fFoI05sZB_XKbfMqV_JMbVC1UOlpfJOE0l24Qep14uuu1ErQ";
		when(payPalService.getInvoiceDetailsByInvoiceId("42", "manish@gmail.com"))
				.thenThrow(new UnauthorizedException("Unautorized"));

		mock.perform(get("/api/v1/payment/getInvoiceDetails/{invoiceId}", "42").header("Authorization",
				"Bearer " + userToken)).andExpect(status().isUnauthorized())
				.andExpect(MockMvcResultMatchers.content().string("Unautorized")).andDo(print());
	}

	@Test
	void testPaymentUnauthorizedException() throws Exception {
		userToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5pc2hAZ21haWwuY29tIiwiaWF0IjoxNjYzNDEwOTYyfQ.jkDylYu-Rf4oEMg39t_aYYqWZ2wQG5aVvwshR3fFoI05sZB_XKbfMqV_JMbVC1UOlpfJOE0l24Qep14uuu1ErQ";
		when(payPalService.isEligibleForPayment(123, "manish@gmail.com"))
				.thenThrow(new UnauthorizedException("Unautorized"));

		mock.perform(post("/api/v1/payment/pay/123").header("Authorization", "Bearer " + userToken))
				.andExpect(status().isUnauthorized()).andExpect(MockMvcResultMatchers.content().string("Unautorized"))
				.andDo(print());
	}

	@Test
	void testPaymentReturnApprovalLink() throws Exception {
		String userToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5pc2hAZ21haWwuY29tIiwiaWF0IjoxNjYzNDEwOTYyfQ.jkDylYu-Rf4oEMg39t_aYYqWZ2wQG5aVvwshR3fFoI05sZB_XKbfMqV_JMbVC1UOlpfJOE0l24Qep14uuu1ErQ";
		Payment payment = new Payment();
		List<Links> links = new ArrayList<>();
		Links l = new Links();
		l.setRel("approval_url");
		l.setHref("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-13203752FN674472X");
		links.add(l);
		payment.setLinks(links);
		when(payPalService.isEligibleForPayment(123, "manish@gmail.com")).thenReturn(true);
		when(payPalService.createPayment(123,
				"http://localhost:" + port + PayPalConstants.ROOT_PATH + PayPalConstants.PAYMENT_CANCEL_PATH,
				"http://localhost:" + port + PayPalConstants.ROOT_PATH + PayPalConstants.PAYMENT_SUCCESS_PATH))
				.thenReturn(payment);

		mock.perform(post("/api/v1/payment/pay/123").header("Authorization", "Bearer " + userToken))
				.andExpect(status().isAccepted())
				.andExpect(MockMvcResultMatchers.content().string(
						"https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-13203752FN674472X"))
				.andDo(print());
	}

	@Test
	void testPaymentNullPaymentException() throws Exception {
		String userToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5pc2hAZ21haWwuY29tIiwiaWF0IjoxNjYzNDEwOTYyfQ.jkDylYu-Rf4oEMg39t_aYYqWZ2wQG5aVvwshR3fFoI05sZB_XKbfMqV_JMbVC1UOlpfJOE0l24Qep14uuu1ErQ";
		when(payPalService.isEligibleForPayment(123, "manish@gmail.com")).thenReturn(true);
		when(payPalService.createPayment(123,
				"http://localhost:" + port + "/" + PayPalConstants.ROOT_PATH + PayPalConstants.PAYMENT_CANCEL_PATH,
				"http://localhost:" + port + "/" + PayPalConstants.ROOT_PATH + PayPalConstants.PAYMENT_SUCCESS_PATH))
				.thenReturn(new Payment());

		mock.perform(post("/api/v1/payment/pay/123").header("Authorization", "Bearer " + userToken))
				.andExpect(status().is(400)).andExpect(MockMvcResultMatchers.content().string("Bad Request"))
				.andDo(print());
	}

	@Test
	void testPaymentNullLinks() throws Exception {
		String userToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5pc2hAZ21haWwuY29tIiwiaWF0IjoxNjYzNDEwOTYyfQ.jkDylYu-Rf4oEMg39t_aYYqWZ2wQG5aVvwshR3fFoI05sZB_XKbfMqV_JMbVC1UOlpfJOE0l24Qep14uuu1ErQ";
		Payment payment = new Payment();
		payment.setLinks(new ArrayList<>());
		when(payPalService.isEligibleForPayment(123, "manish@gmail.com")).thenReturn(true);
		when(payPalService.createPayment(123,
				"http://localhost:" + port + PayPalConstants.ROOT_PATH + PayPalConstants.PAYMENT_CANCEL_PATH,
				"http://localhost:" + port + PayPalConstants.ROOT_PATH + PayPalConstants.PAYMENT_SUCCESS_PATH))
				.thenReturn(payment);

		mock.perform(post("/api/v1/payment/pay/123").header("Authorization", "Bearer " + userToken))
				.andExpect(status().is(409)).andExpect(MockMvcResultMatchers.content()
						.string("Could not initiate payment, please try after some time"))
				.andDo(print());
	}

	@Test
	void testPaymentBookingNotFoundException() throws Exception {
		String userToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5pc2hAZ21haWwuY29tIiwiaWF0IjoxNjYzNDEwOTYyfQ.jkDylYu-Rf4oEMg39t_aYYqWZ2wQG5aVvwshR3fFoI05sZB_XKbfMqV_JMbVC1UOlpfJOE0l24Qep14uuu1ErQ";
		Payment payment = new Payment();
		payment.setState("approved");
		Payer payer = new Payer();
		PayerInfo payerInfo = new PayerInfo();
		payerInfo.setEmail("acb@gmail.com");
		payerInfo.setFirstName("John");
		payerInfo.setLastName("mikey");
		payer.setPayerInfo(payerInfo);
		payer.setPaymentMethod("Instant");
		List<Transaction> transactions = new ArrayList<>();
		Transaction tran = new Transaction();
		tran.setDescription("VivahSpot Booking: " + 123);
		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal("9.00");
		List<RelatedResources> relatedResources = new ArrayList<>();
		RelatedResources relatedR = new RelatedResources();
		Sale s = new Sale();
		s.setState("completed");
		s.setPaymentMode("paypal");
		relatedR.setSale(s);
		relatedResources.add(relatedR);
		tran.setRelatedResources(relatedResources);
		tran.setAmount(amount);
		transactions.add(tran);
		payment.setPayer(payer);
		payment.setTransactions(transactions);

		when(payPalService.executePayment((String) any(), (String) any())).thenReturn(payment);

		Invoice invoice = new Invoice();
		invoice.setBookingId(123);
		when(payPalService.saveInvoiceOnSuccessfulPayment((Payment) any())).thenReturn(invoice);
		when(payPalService.updateSlotStatusOnSuccessfulPayment(123))
				.thenThrow(new BookingNotFoundException("Booking not found"));

		mock.perform(get("/api/v1/pay/success?paymentId=dsfvsd&PayerID=cdscds").header("Authorization",
				"Bearer " + userToken)).andExpect(status().is(409))
				.andExpect(MockMvcResultMatchers.content().string("Booking not found")).andDo(print());
	}

	@Test
	void testPaymentCancelled() throws Exception {
		String userToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5pc2hAZ21haWwuY29tIiwiaWF0IjoxNjYzNDEwOTYyfQ.jkDylYu-Rf4oEMg39t_aYYqWZ2wQG5aVvwshR3fFoI05sZB_XKbfMqV_JMbVC1UOlpfJOE0l24Qep14uuu1ErQ";
		mock.perform(get("/api/v1/pay/cancel").header("Authorization", "Bearer " + userToken))
				.andExpect(status().is(200)).andExpect(MockMvcResultMatchers.content().string("Payment cancelled"))
				.andDo(print());
	}

	@Test
	void testPaymentSuccessfulSuccessMessage() throws Exception {
		String userToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5pc2hAZ21haWwuY29tIiwiaWF0IjoxNjYzNDEwOTYyfQ.jkDylYu-Rf4oEMg39t_aYYqWZ2wQG5aVvwshR3fFoI05sZB_XKbfMqV_JMbVC1UOlpfJOE0l24Qep14uuu1ErQ";
		Payment payment = new Payment();
		payment.setState("approved");
		Payer payer = new Payer();
		PayerInfo payerInfo = new PayerInfo();
		payerInfo.setEmail("acb@gmail.com");
		payerInfo.setFirstName("John");
		payerInfo.setLastName("mikey");
		payer.setPayerInfo(payerInfo);
		payer.setPaymentMethod("Instant");
		List<Transaction> transactions = new ArrayList<>();
		Transaction tran = new Transaction();
		tran.setDescription("VivahSpot Booking: " + 123);
		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal("9.00");
		List<RelatedResources> relatedResources = new ArrayList<>();
		RelatedResources relatedR = new RelatedResources();
		Sale s = new Sale();
		s.setState("completed");
		s.setPaymentMode("paypal");
		relatedR.setSale(s);
		relatedResources.add(relatedR);
		tran.setRelatedResources(relatedResources);
		tran.setAmount(amount);
		transactions.add(tran);
		payment.setPayer(payer);
		payment.setTransactions(transactions);

		when(payPalService.executePayment((String) any(), (String) any())).thenReturn(payment);

		Invoice invoice = new Invoice();
		invoice.setBookingId(123);
		invoice.setCurrency("GBP");
		invoice.setInvoiceId("42");
		invoice.setPaymentMethod("Paypal");
		invoice.setPaymentMode("Instant");
		invoice.setPaymentStatus("Completed");
		invoice.setBookingId(1);
		invoice.setUserEmail("manish@gmail.com");
		invoice.setVenueOwnerEmail("owner@gmail.com");
		LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
		invoice.setPaymentTime(atStartOfDayResult.toString());
		invoice.setTotalAmount(10.00);
		when(payPalService.saveInvoiceOnSuccessfulPayment((Payment) any())).thenReturn(invoice);

		mock.perform(get("/api/v1/pay/success?paymentId=dsfvsd&PayerID=cdscds").header("Authorization",
				"Bearer " + userToken)).andExpect(status().is(200))
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"invoiceId\":\"42\",\"bookingId\":1,\"userEmail\":\"manish@gmail.com\",\"venueOwnerEmail\":\"owner@gmail.com\",\"paymentMethod\":\"Paypal\",\"currency\":\"GBP\",\"totalAmount\":10.0,\"paymentMode\":\"Instant\",\"paymentStatus\":\"Completed\",\"paymentTime\":\"1970-01-01T00:00\",\"bookingDate\":null,\"venueName\":null}"));
	}

	@Test
	void testPaymentSuccessfulFailedMessage() throws Exception {
		String userToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5pc2hAZ21haWwuY29tIiwiaWF0IjoxNjYzNDEwOTYyfQ.jkDylYu-Rf4oEMg39t_aYYqWZ2wQG5aVvwshR3fFoI05sZB_XKbfMqV_JMbVC1UOlpfJOE0l24Qep14uuu1ErQ";

		Payment payment = new Payment();
		payment.setState("failed");

		when(payPalService.executePayment((String) any(), (String) any())).thenReturn(payment);

		mock.perform(get("/api/v1/pay/success?paymentId=dsfvsd&PayerID=cdscds").header("Authorization",
				"Bearer " + userToken)).andExpect(status().isBadGateway())
				.andExpect(MockMvcResultMatchers.content().string("Payment failed")).andDo(print());
	}

	@Test
	void testPaymentSuccessfulBadRequest() throws Exception {
		String userToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5pc2hAZ21haWwuY29tIiwiaWF0IjoxNjYzNDEwOTYyfQ.jkDylYu-Rf4oEMg39t_aYYqWZ2wQG5aVvwshR3fFoI05sZB_XKbfMqV_JMbVC1UOlpfJOE0l24Qep14uuu1ErQ";

		mock.perform(get("/api/v1/pay/success").param("PayerID", "JNCSJK").param("paymentId", "CSHBCS")
				.header("Authorization", "Bearer " + userToken)).andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string("Bad Request")).andDo(print());
	}
}
