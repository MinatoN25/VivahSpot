package com.stackroute.payment.constants;

public class PayPalConstants {
	public static final String ROOT_PATH="/api/v1";
	public static final String PAYMENT_PATH= "/payment/pay/{bookingId}";
	public static final String PAYMENT_SUCCESS_PATH= "/pay/success";
	public static final String PAYMENT_CANCEL_PATH= "/pay/cancel";
	public static final String PAYMENT_INVOICE_PATH= "/payment/getInvoiceDetails/{invoiceId}";
	public static final String REFUND_PATH= "/payment/refund/{bookingId}";
	public static final String PAYMENT_CANCEL_MSG="Payment cancelled";
	public static final String PAYMENT_SUCCESS_MSG="Payment successful";
	public static final String PAYMENT_CONFLICT_MSG="Exception occurred, please contact support team";
	public static final String PAYMENT_CORRECT_INFO_MSG="Please correct the entered information";
	public static final String PAYMENT_FAILED_MSG="Payment failed";
	public static final String PAYMENT_INITIATION_FAILED_MSG="Could not initiate payment, please try after some time";
	public static final String REFUND_INITIATION_FAILED_MSG="Could not initiate refund request, please try again after some time";
	public static final String REFUND_MSG="Money refunded: ";
	public static final String BAD_REQUEST_MSG="Bad Request";
	public static final String SELECT_CURRENCY_MSG="Please select your currency";
	public static final String PAYMENT_VALIDATION_MSG="could not get payment details";
	public static final String RABBITMQ_MSG="Exception occured in rabbitmq";
}
