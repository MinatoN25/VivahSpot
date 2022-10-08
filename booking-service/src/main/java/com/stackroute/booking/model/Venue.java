package com.stackroute.booking.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "venue")
public class Venue {

	@Id
	private int venueId;
	private List<Slot> slots;
	private boolean approvalRequired;
	private String venueOwnerEmail;
	private String currency;
	private Double venuePrice;
	private String venueName;
	
	
}
