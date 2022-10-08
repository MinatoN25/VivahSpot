package com.stackroute.payment.service;

import com.paypal.api.payments.DetailedRefund;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.stackroute.payment.model.Booking;
import com.stackroute.payment.model.Invoice;

public interface PayPalService {
	
	Payment createPayment(int bookingId, String cancelUrl, String successUrl) throws PayPalRESTException;
	Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
	Invoice saveInvoiceOnSuccessfulPayment(Payment payment);
	Invoice getInvoiceDetailsByInvoiceId(String paymentId, String userEmail);
	boolean updateSlotStatusOnSuccessfulPayment(int bookingId);
	boolean isEligibleForPayment(int bookingId, String userEmail);
	DetailedRefund refund(Booking booking) throws PayPalRESTException;
}
