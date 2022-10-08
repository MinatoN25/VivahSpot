package com.stackroute.payment.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.DetailedRefund;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.RefundRequest;
import com.paypal.api.payments.Sale;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.stackroute.payment.config.MQConfig;
import com.stackroute.payment.exception.BookingNotFoundException;
import com.stackroute.payment.exception.InvoiceAlreadyExistsException;
import com.stackroute.payment.exception.InvoiceNotFoundException;
import com.stackroute.payment.exception.NotEligibleForPaymentException;
import com.stackroute.payment.exception.UnauthorizedException;
import com.stackroute.payment.model.Booking;
import com.stackroute.payment.model.Invoice;
import com.stackroute.payment.model.Status;
import com.stackroute.payment.repository.BookingRepository;
import com.stackroute.payment.repository.InvoiceRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class PayPalServiceImpl implements PayPalService {

	private APIContext apiContext;
	private InvoiceRepository invoiceRepository;
	private BookingRepository bookingRepository;

	@Autowired
	public PayPalServiceImpl(APIContext apiContext, InvoiceRepository invoiceRepository,
			BookingRepository bookingRepository) {
		this.apiContext = apiContext;
		this.invoiceRepository = invoiceRepository;
		this.bookingRepository = bookingRepository;
	}

	@RabbitListener(queues = MQConfig.REFUND_QUEUE)
	public void refundListerner(Booking booking) {
		try {
			refund(booking);
		} catch (Exception ee) {
		log.error("Exception while processing refund");
		}
	}

	public Payment createPayment(int bookingId, String cancelUrl, String successUrl) throws PayPalRESTException {
		Optional<Booking> booking = bookingRepository.findById(bookingId);
		if (!booking.isPresent()) {
			throw new BookingNotFoundException("Booking not found in the system");
		}
		apiContext.setRequestId(null);

		Payment payment = setPayment(
				getTransactions(setAmount(booking.get().getCurrency(), booking.get().getVenuePrice()), bookingId),
				cancelUrl, successUrl);

		return payment.create(apiContext);
	}

	@Override
	public DetailedRefund refund(Booking booking) throws PayPalRESTException {

		Invoice invoice = invoiceRepository.getInvoiceByBookingId(booking.getBookingId());
		RefundRequest refund = new RefundRequest();
		Sale sale = new Sale();
		sale.setId(invoice.getInvoiceId());

		refund.setAmount(setAmount(invoice.getCurrency(), (80 * invoice.getTotalAmount()) / 100));

		return sale.refund(apiContext, refund);
	}

	public Amount setAmount(String currency, Double total) {
		if (currency.equals("INR")) {
			currency = "USD";
			total = total / 79.71;
		}

		Amount amount = new Amount();
		amount.setCurrency(currency);
		total = new BigDecimal(String.valueOf(total)).setScale(2, RoundingMode.HALF_UP).doubleValue();
		amount.setTotal(String.valueOf(total));

		return amount;

	}

	public List<Transaction> getTransactions(Amount amount, int bookingId) {
		Transaction transaction = new Transaction();
		transaction.setDescription(String.valueOf(bookingId));
		transaction.setAmount(amount);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);
		return transactions;
	}

	public Payment setPayment(List<Transaction> transactions, String cancelUrl, String successUrl) {
		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(successUrl);
		payment.setRedirectUrls(redirectUrls);
		return payment;
	}

	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {

		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecute = new PaymentExecution();
		paymentExecute.setPayerId(payerId);

		return payment.execute(apiContext, paymentExecute);
	}

	public Invoice saveInvoiceOnSuccessfulPayment(Payment payment) {
		Invoice invoice = new Invoice();
		invoice.setInvoiceId(payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId());
		invoice.setPaymentMethod(payment.getPayer().getPaymentMethod());
		invoice.setPaymentMode(
				payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getPaymentMode());
		invoice.setCurrency(payment.getTransactions().get(0).getAmount().getCurrency());
		invoice.setTotalAmount(Double.valueOf(payment.getTransactions().get(0).getAmount().getTotal()));
		invoice.setPaymentStatus(payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getState());
		invoice.setPaymentTime(LocalDateTime.now().toString());
		invoice.setBookingId(Integer.parseInt(payment.getTransactions().get(0).getDescription()));

		Optional<Invoice> invoice1 = invoiceRepository.findById(invoice.getInvoiceId());
		if (invoice1.isPresent()) {
			throw new InvoiceAlreadyExistsException("Invoice already exists");

		} else {
			invoice.setUserEmail(bookingRepository.findById(invoice.getBookingId()).get().getUserEmail());
			invoice.setVenueOwnerEmail(bookingRepository.findById(invoice.getBookingId()).get().getVenueOwnerEmail());
			invoice.setBookingDate(
					bookingRepository.findById(invoice.getBookingId()).get().getBookingDate().toString());
			invoice.setVenueName(bookingRepository.findById(invoice.getBookingId()).get().getVenueName());
			return invoiceRepository.save(invoice);
		}

	}

	@Override
	public Invoice getInvoiceDetailsByInvoiceId(String paymentId, String userEmail) {
		Optional<Invoice> invoice = invoiceRepository.findById(paymentId);
		if (invoice.isPresent()) {
			if (!invoice.get().getUserEmail().equals(userEmail)) {
				throw new UnauthorizedException("You are not authorized to view invoice details for this booking");
			}
			return invoiceRepository.findById(paymentId).get();
		} else
			throw new InvoiceNotFoundException("Could not get invoice details");
	}

	@Override
	public boolean updateSlotStatusOnSuccessfulPayment(int bookingId) {
		Optional<Booking> booking = bookingRepository.findById(bookingId);
		if (booking.isPresent()) {
			booking.get().getSlot().setSlotStatus(Status.BOOKED);
			bookingRepository.save(booking.get());
			return true;
		} else {
			throw new BookingNotFoundException("Booking not found");
		}
	}

	@Override
	public boolean isEligibleForPayment(int bookingId, String userEmail) {
		Optional<Booking> booking = bookingRepository.findById(bookingId);
		if (booking.isPresent()) {
			if (!booking.get().getUserEmail().equals(userEmail)) {
				throw new UnauthorizedException("You are not authorized to make this payment");
			}
			if (booking.get().getSlot().getSlotStatus().equals(Status.BOOKED)) {
				throw new NotEligibleForPaymentException("Payment is already completed for this booking");
			}
			if (booking.get().getSlot().getSlotStatus().equals(Status.PENDING_PAYMENT)) {
				return true;
			} else
				throw new NotEligibleForPaymentException("Booking is not eligible for payment");
		} else {
			throw new BookingNotFoundException("Booking not found in the system");
		}
	}
}
