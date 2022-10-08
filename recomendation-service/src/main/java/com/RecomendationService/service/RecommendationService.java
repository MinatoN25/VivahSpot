package com.RecomendationService.service;

import java.util.List;

import com.RecomendationService.entity.Venue;

public interface RecommendationService {


	List<Venue> listVenues(String query);

	List<Venue> listVenueByVenueName(String venueName);

	List<Venue> findByPriceAndCapacity(Integer venuePrice, Integer venueCapacity);

	List<Venue> listVenuesFromLowToHigh(String query);

	List<Venue> listVenuesFromHighToLOw(String query);

	List<Venue> listVenuesGreaterThan(String query, int price);

	List<Venue> listVenuesLesserThan(String query, int price);

	List<Venue> listVenuesInBetween(String query, int price1, int price2);

	List<Venue> listVenuesFromLowToHighCapacity(String query);

	List<Venue> listVenuesFromHighToLOwCapacity(String query);

	List<Venue> listVenuesGreaterThanCapacity(String query, int size);

	List<Venue> listVenuesLesserThanCapacity(String query, int size);

	List<Venue> listVenuesInBetweenCapacity(String query, int size1, int size2);

	List<Venue> getAllVenuesAsperUserCity(String userEmail);

	

}
