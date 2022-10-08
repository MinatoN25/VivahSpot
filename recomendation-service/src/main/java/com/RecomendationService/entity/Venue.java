package com.RecomendationService.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.Data;

@Document
@Data
public class Venue {

	@Transient
    public static final String SEQUENCE_NAME = "venue_sequence";

	
	@Id
	private int venueId;
	
	@NotNull
	private String venueName;
	@NotNull(message = "Please enter details for venue landmark")
	private String landmark;
	private List<String> venueSpaceType;
	@NotNull(message = "Please enter details for venue capacity")
	private Integer venueCapacity;
	private List<String> cuisine;
	private List<String> venueFacilities;
	@NotNull
	private Integer venuePrice;
	@NotNull(message = "Please mention food type like veg,non-veg ,etc.")
	private String foodType;
	private int venueRating;
	private boolean isVerified;
	private boolean isAvailable;
	private List<Room> roomDetails;
	private String city;
}
