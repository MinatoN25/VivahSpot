package com.stackroute.email.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

	private String invoiceId;
	private int bookingId;
	private String userEmail;
	private String venueOwnerEmail;
	private String paymentMethod;
	private String currency;
	private String totalAmount;
	private String paymentMode;
	private String paymentStatus; 
	@JsonDeserialize
	private String paymentTime;
	private int venueId;
	private String bookingDate;
	private String venueName;

}
