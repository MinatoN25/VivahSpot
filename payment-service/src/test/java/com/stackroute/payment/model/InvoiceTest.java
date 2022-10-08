package com.stackroute.payment.model;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;


public class InvoiceTest {
	private static Invoice invoice;
	@BeforeAll
	public static void setUp() throws Exception {

		invoice = new Invoice();
        invoice.setCurrency("GBP");
        invoice.setBookingId(42);
        invoice.setPaymentMethod("Payment Method");
        invoice.setPaymentMode("Payment Mode");
        invoice.setPaymentStatus("Payment Status");
        invoice.setPaymentTime(LocalDateTime.now().toString());
        invoice.setTotalAmount(10.00);
        invoice.setUserEmail("manish@gmail.com");
        invoice.setVenueOwnerEmail("owner@gmail.com");
		
	}

	@Test
	void test() {
		BeanTester beanTester=new BeanTester();
		beanTester.getFactoryCollection().addFactory(LocalDateTime.class,
	            new LocalDateTimeFactory());
		beanTester.testBean(Invoice.class);
	}

}
