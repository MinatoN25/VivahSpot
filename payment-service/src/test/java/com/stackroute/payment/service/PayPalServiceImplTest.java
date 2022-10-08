package com.stackroute.payment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.RelatedResources;
import com.paypal.api.payments.Sale;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.stackroute.payment.exception.BookingNotFoundException;
import com.stackroute.payment.exception.InvoiceAlreadyExistsException;
import com.stackroute.payment.exception.InvoiceNotFoundException;
import com.stackroute.payment.exception.NotEligibleForPaymentException;
import com.stackroute.payment.exception.UnauthorizedException;
import com.stackroute.payment.model.Booking;
import com.stackroute.payment.model.Invoice;
import com.stackroute.payment.model.Slot;
import com.stackroute.payment.model.Status;
import com.stackroute.payment.repository.BookingRepository;
import com.stackroute.payment.repository.InvoiceRepository;

@ContextConfiguration(classes = {PayPalServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PayPalServiceImplTest {
    @MockBean
    private APIContext aPIContext;
    
    @MockBean
    private Payment payment;
    
    @MockBean
    private InvoiceRepository invoiceRepository;
    
    @MockBean
    private BookingRepository bookingRepository;

    @Autowired
    private PayPalServiceImpl paypalServiceImpl;
    
    @MockBean
    private Invoice invoice;
    
    @BeforeEach
    public void setUp() {
    	invoice = new Invoice();
    	invoice.setCurrency("GBP");
        invoice.setBookingId(1);
        invoice.setInvoiceId("24");
        invoice.setPaymentMethod("Paypal");
        invoice.setPaymentMode("Instant");
        invoice.setPaymentStatus("Completed");
        invoice.setPaymentTime(LocalDateTime.now().toString());
        invoice.setTotalAmount(10.00);
        invoice.setBookingId(1);
        invoice.setUserEmail("manish@gmail.com");
        invoice.setVenueOwnerEmail("owner@gmail.com");
    }
    
    @Test
    void testSetAmount() {
        Amount actualSetAmountResult = paypalServiceImpl.setAmount("GBP", 10.0d);
        assertEquals("GBP", actualSetAmountResult.getCurrency());
        assertEquals("10.0", actualSetAmountResult.getTotal());
    }
  
    @Test
    void testGetTransactions() {
        assertEquals(1, paypalServiceImpl.getTransactions(new Amount("GBP", "Total"),1).size());
    }

    @Test
    void testSetPayment() {
        Payment actualSetPaymentResult = paypalServiceImpl.setPayment(new ArrayList<>(), "https://localhost:/pay/cancel",
                "https://localhost:/pay/success");
        assertTrue(actualSetPaymentResult.getTransactions().isEmpty());
        assertEquals("sale", actualSetPaymentResult.getIntent());
        RedirectUrls redirectUrls = actualSetPaymentResult.getRedirectUrls();
        assertEquals("https://localhost:/pay/success", redirectUrls.getReturnUrl());
        assertEquals("https://localhost:/pay/cancel", redirectUrls.getCancelUrl());
        assertEquals("paypal", actualSetPaymentResult.getPayer().getPaymentMethod());
    }
    
    @Test
    void testSaveInvoiceSuccess() {
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
		tran.setDescription("123");
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
		Booking booking = new Booking();
		booking.setUserEmail("manish@gmail.com");
		booking.setVenueOwnerEmail("owner@gmail.com");
		booking.setBookingId(123);
		booking.setBookingDate(LocalDate.now().plusDays(2));
		Optional<Booking> book = Optional.of(booking);
		when(bookingRepository.findById(123)).thenReturn(book);
    	when(invoiceRepository.findById((String) any())).thenReturn(Optional.empty());
        when(invoiceRepository.save((Invoice) any())).thenReturn(invoice);
        assertSame(invoice, paypalServiceImpl.saveInvoiceOnSuccessfulPayment(payment));
        verify(invoiceRepository).save((Invoice) any());
    }

    @Test
    void testGetInvoiceDetailsByPaymentId() {
        Optional<Invoice> ofResult = Optional.of(invoice);
        when(invoiceRepository.findById((String) any())).thenReturn(ofResult);
        assertSame(invoice, paypalServiceImpl.getInvoiceDetailsByInvoiceId("24",invoice.getUserEmail()));
        verify(invoiceRepository, atLeast(1)).findById((String) any());
    }
    
    @Test
    void testGetInvoiceDetailsByPaymentIdUnauthorizedException() {
        Optional<Invoice> ofResult = Optional.of(invoice);
        when(invoiceRepository.findById((String) any())).thenReturn(ofResult);
        assertThrows(UnauthorizedException.class, () -> paypalServiceImpl.getInvoiceDetailsByInvoiceId("24", "mm@gmail.com"));
        verify(invoiceRepository).findById((String) any());
    }

    @Test
    void testGetInvoiceDetailsByPaymentIdInvoiceNotFoundException() {
        when(invoiceRepository.findById((String) any())).thenReturn(Optional.empty());
        assertThrows(InvoiceNotFoundException.class, () -> paypalServiceImpl.getInvoiceDetailsByInvoiceId("24", invoice.getUserEmail()));
        verify(invoiceRepository).findById((String) any());
    }
    
    @Test
    void testSaveInvoiceSuccessInvoiceAlreadyExistsException() {
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
		tran.setDescription("123");
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
    	Optional<Invoice> invoice1 = Optional.of(invoice);
    	when(invoiceRepository.findById((String) any())).thenReturn(invoice1);
        assertThrows(InvoiceAlreadyExistsException.class, () -> paypalServiceImpl.saveInvoiceOnSuccessfulPayment(payment));
        verify(invoiceRepository).findById((String) any());
    }
    
    @Test
    void testUpdateSlotStatusOnSuccessfulPayment() {
    	Booking booking = new Booking();
    	booking.setBookingId(1);
    	booking.setSlot(new Slot(1,Status.PENDING_PAYMENT,"Morning"));
    	Optional<Booking> booking1 = Optional.of(booking);
    	when(bookingRepository.findById(1)).thenReturn(booking1);
    	when(bookingRepository.save(booking1.get())).thenReturn(booking);
    	assertTrue(paypalServiceImpl.updateSlotStatusOnSuccessfulPayment(1));
    }
    
    @Test
    void testUpdateSlotStatusOnSuccessfulPaymentBookingNotFoundException() {
    	Booking booking = new Booking();
    	booking.setBookingId(1);
    	booking.setSlot(new Slot(1,Status.PENDING_PAYMENT,"Morning"));
    	when(bookingRepository.findById(2)).thenReturn(null);
    	assertThrows(BookingNotFoundException.class, () -> paypalServiceImpl.updateSlotStatusOnSuccessfulPayment(1));
    }
    
    @Test
    void testIsEligibleForPayment() {
    	Booking booking = new Booking();
    	booking.setBookingId(1);
    	booking.setUserEmail("manish@gmail.com");
    	booking.setSlot(new Slot(1,Status.PENDING_PAYMENT,"Morning"));
    	Optional<Booking> booking1 = Optional.of(booking);
    	when(bookingRepository.findById(1)).thenReturn(booking1);
    	assertTrue(paypalServiceImpl.isEligibleForPayment(1,"manish@gmail.com"));
    }
    
    @Test
    void testIsEligibleForPaymentBookingNotEligibleForPayment() {
    	Booking booking = new Booking();
    	booking.setBookingId(1);
    	booking.setUserEmail("manish@gmail.com");
    	booking.setSlot(new Slot(1,Status.BOOKED,"Morning"));
    	Optional<Booking> booking1 = Optional.of(booking);
    	when(bookingRepository.findById(1)).thenReturn(booking1);
    	assertThrows(NotEligibleForPaymentException.class, () ->paypalServiceImpl.isEligibleForPayment(1,"manish@gmail.com"));
    }
    
    @Test
    void testIsEligibleForPaymentUnauthorizedException() {
    	Booking booking = new Booking();
    	booking.setBookingId(1);
    	booking.setUserEmail("manish@gmail.com");
    	booking.setSlot(new Slot(1,Status.BOOKED,"Morning"));
        Optional<Booking> ofResult = Optional.of(booking);
        when(bookingRepository.findById(1)).thenReturn(ofResult);
        assertThrows(UnauthorizedException.class, () -> paypalServiceImpl.isEligibleForPayment(1, "mm@gmail.com"));
    }
    
    @Test
    void testIsEligibleForPaymentBookingNotFoundException() {
    	Booking booking = new Booking();
    	booking.setBookingId(1);
    	booking.setUserEmail("manish@gmail.com");
    	booking.setSlot(new Slot(1,Status.BOOKED,"Morning"));
    	Optional<Booking> booking1 = Optional.of(booking);
    	when(bookingRepository.findById(1)).thenReturn(booking1);
    	assertThrows(BookingNotFoundException.class, () ->paypalServiceImpl.isEligibleForPayment(2,"manish@gmail.com"));
    }
}

