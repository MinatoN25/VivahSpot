package com.stackroute.payment.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.stackroute.payment.model.Invoice;

@RunWith(SpringRunner.class)
@DataMongoTest
class InvoiceRepositoryTest {

	@Autowired
	private InvoiceRepository invoiceRepository;
	private Invoice invoice;

	@BeforeEach
	public void setUp() throws Exception {
		invoice = new Invoice();
		invoice.setInvoiceId("sdjksa");
		invoice.setCurrency("USD");
		invoice.setPaymentMethod("Instant");
		invoice.setPaymentMode("paypal");
		invoice.setPaymentStatus("completed");
		invoice.setPaymentTime(LocalDateTime.now().toString());
		invoice.setTotalAmount(9.00);
        invoice.setUserEmail("manish@gmail.com");
        invoice.setVenueOwnerEmail("owner@gmail.com");
		invoiceRepository.deleteById("sdjksa");
	}

	@AfterEach
	public void tearDown() throws Exception {

		invoiceRepository.deleteById("sdjksa");

	}

	@Test
	void saveinvoiceTest() {

		invoiceRepository.insert(invoice);
		Invoice fetchinvoice = invoiceRepository.findById("sdjksa").get();
		Assert.assertEquals(invoice.getUserEmail(), fetchinvoice.getUserEmail());
	}

	@Test
	void deleteinvoiceTest() {
		invoiceRepository.insert(invoice);
		Invoice fetchinvoice = invoiceRepository.findById("sdjksa").get();
		Assert.assertEquals(invoice.getUserEmail(), fetchinvoice.getUserEmail());
		invoiceRepository.delete(fetchinvoice);
		assertThrows(NoSuchElementException.class, () -> {
			invoiceRepository.findById("sdjksa").get();
		});
	}

	@Test
	void updateinvoiceTest() {
		invoiceRepository.insert(invoice);
		Invoice fetchedinvoice = invoiceRepository.findById("sdjksa").get();
		fetchedinvoice.setCurrency("INR");
		invoiceRepository.save(fetchedinvoice);
		fetchedinvoice = invoiceRepository.findById("sdjksa").get();
		Assert.assertEquals("INR", fetchedinvoice.getCurrency());
	}

	@Test
	void getinvoiceByIdTest() {
		invoiceRepository.insert(invoice);
		Invoice fetchedinvoice = invoiceRepository.findById("sdjksa").get();
		Assert.assertEquals(invoice.getInvoiceId(), fetchedinvoice.getInvoiceId());

	}

}
