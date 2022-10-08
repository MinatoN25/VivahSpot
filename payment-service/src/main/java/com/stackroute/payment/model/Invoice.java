package com.stackroute.payment.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Invoice")
public class Invoice {

	@Id
	private String invoiceId;
	private int bookingId;
	private String userEmail;
	private String venueOwnerEmail;
	private String paymentMethod;
	private String currency;
	private double totalAmount;
	private String paymentMode;
	private String paymentStatus;
	private String paymentTime;
	private String bookingDate;
	private String venueName;

}
