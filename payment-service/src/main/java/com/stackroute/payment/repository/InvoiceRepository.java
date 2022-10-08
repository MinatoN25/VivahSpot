package com.stackroute.payment.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.stackroute.payment.model.Invoice;

public interface InvoiceRepository extends MongoRepository<Invoice, String> {
	
	@Query(value = "{bookingId : ?0}")
	Invoice getInvoiceByBookingId(int bookingId);

}
