package com.stackroute.venueservice.model;

import java.util.List;

import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "venue")
public class Venue {

	@Transient
	public static final String SEQUENCE_NAME = "venue_sequence";

	@Id
	private int venueId;
	@NotNull
	private String venueName;
	@NotNull(message = "Please enter details for venue city")
	private String city;
	private List<String> venueSpaceType;
	@NotNull(message = "Please enter details for venue capacity")
	private int venueCapacity;
	private List<String> cuisine;
	private List<String> venueFacilities;
	@NotNull
	private int venuePrice;
	@NotNull
	private String currency;
	@NotNull(message = "Please mention food type like veg,non-veg ,etc.")
	private String foodType;
	private int venueRating;
	private boolean isVerified;
	private boolean isAvailable;
	private List<Room> roomDetails;
	@NotNull
	private List<Slot> slots;
	@NotNull
	private String venueOwnerEmail;

}
