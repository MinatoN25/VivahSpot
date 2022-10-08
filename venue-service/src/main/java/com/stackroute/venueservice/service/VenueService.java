package com.stackroute.venueservice.service;

import java.util.List;
import java.util.Optional;
import com.stackroute.venueservice.model.Venue;

public interface VenueService {

	Venue addVenue(Venue venue);

	Venue updateVenue(Venue venue);

	Optional<Venue> deleteVenue(int venueId);

	Optional<Venue> getVenueById(int venueId);
	
	List<Venue> getAllVenues();
	
	List<Venue> listVenuesByName(String query);
	
	Venue giveRatingToBookedVenue(int venueId, int venueRating, String userEmail);
	
}
