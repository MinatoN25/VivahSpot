package com.stackroute.venueservice.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "BookingDetails")
public class Booking {

	@Id
	private int bookingId;
	private int venueId;
	private String userEmail;
	private Slot slot;
}
