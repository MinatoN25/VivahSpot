package com.stackroute.payment.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.stackroute.payment.config.MQConfig;
import com.stackroute.payment.constants.PayPalConstants;
import com.stackroute.payment.exception.BookingNotFoundException;
import com.stackroute.payment.exception.InvoiceAlreadyExistsException;
import com.stackroute.payment.exception.InvoiceNotFoundException;
import com.stackroute.payment.exception.NotEligibleForPaymentException;
import com.stackroute.payment.exception.UnauthorizedException;
import com.stackroute.payment.model.Invoice;
import com.stackroute.payment.service.PayPalService;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = PayPalConstants.ROOT_PATH)
public class PayPalController {

	private PayPalService payPalService;
	private RabbitTemplate template;

	@Autowired
	public PayPalController(PayPalService payPalService, RabbitTemplate template) {
		this.payPalService = payPalService;
		this.template = template;
	}

	@Value("${server.port}")
	public String port;

	@ApiOperation(value = "This method is used for initiating the payment")
	@PostMapping(path = PayPalConstants.PAYMENT_PATH)
	public ResponseEntity<String> payment(@PathVariable Integer bookingId, @RequestAttribute(name = "claims") Claims claims) {
		try {

			if (payPalService.isEligibleForPayment(bookingId, claims.getSubject())) {
				Payment payment = payPalService.createPayment(bookingId,
						"http://localhost:" + port  + PayPalConstants.ROOT_PATH
								+ PayPalConstants.PAYMENT_CANCEL_PATH,
						"http://localhost:" + port  + PayPalConstants.ROOT_PATH
								+ PayPalConstants.PAYMENT_SUCCESS_PATH);
				for (Links link : payment.getLinks()) {
					if (link.getRel().equals("approval_url")) {
						return new ResponseEntity<>(link.getHref(), HttpStatus.ACCEPTED);
					}
				}
			}
		} catch (UnauthorizedException authEx) {
			return new ResponseEntity<>(authEx.getErrorMessage(), HttpStatus.UNAUTHORIZED);
		} catch (NotEligibleForPaymentException notEligibleEx) {
			return new ResponseEntity<>(notEligibleEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (BookingNotFoundException bookingEx) {
			return new ResponseEntity<>(bookingEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (PayPalRESTException paypalrest) {

			return new ResponseEntity<>(paypalrest.getDetails().getDetails().get(0).getIssue(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(PayPalConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(PayPalConstants.PAYMENT_INITIATION_FAILED_MSG, HttpStatus.CONFLICT);

	}

	@ApiOperation(value = "This method is called upon cancellation of payment")
	@GetMapping(path = PayPalConstants.PAYMENT_CANCEL_PATH)
	public ResponseEntity<String> paymentCancelled() {
		return new ResponseEntity<>(PayPalConstants.PAYMENT_CANCEL_MSG, HttpStatus.OK);
	}

	@ApiOperation(value = "This method is called upon payment successful")
	@GetMapping(path = PayPalConstants.PAYMENT_SUCCESS_PATH)
	public ResponseEntity<?> paymentSuccessful(@RequestParam("paymentId") String paymentId,
			@RequestParam("PayerID") String payerId) {

		Invoice invoice = new Invoice();
		try {
			
			Payment payment = payPalService.executePayment(paymentId, payerId);

			if (payment.getState().equals("approved")) {

				invoice = payPalService.saveInvoiceOnSuccessfulPayment(payment);
				payPalService.updateSlotStatusOnSuccessfulPayment(invoice.getBookingId());
				
				
				template.convertAndSend(MQConfig.INVOICE_EXCHANGE, MQConfig.INVOICE_ROUTING_KEY, invoice);

				return new ResponseEntity<>(invoice, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(PayPalConstants.PAYMENT_FAILED_MSG, HttpStatus.BAD_GATEWAY);
			}
		} catch (InvoiceAlreadyExistsException invoiceEx) {
			return new ResponseEntity<>(invoiceEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (BookingNotFoundException bookingEx) {
			return new ResponseEntity<>(bookingEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (PayPalRESTException paypalrest) {
			return new ResponseEntity<>(paypalrest.getDetails().getDetails().get(0).getIssue(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(PayPalConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "This method is used for getting the invoice by paymentId")
	@GetMapping(path = PayPalConstants.PAYMENT_INVOICE_PATH)
	public ResponseEntity<?> getInvoiceDetails(@PathVariable String invoiceId, @RequestAttribute(name = "claims") Claims claims) {
		try {
			return new ResponseEntity<>(payPalService.getInvoiceDetailsByInvoiceId(invoiceId, claims.getSubject()), HttpStatus.OK);
		} catch (UnauthorizedException authEx) {
			return new ResponseEntity<>(authEx.getErrorMessage(), HttpStatus.UNAUTHORIZED);
		} catch (InvoiceNotFoundException invoiceEx) {
			return new ResponseEntity<>(invoiceEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(PayPalConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}
	}
}
