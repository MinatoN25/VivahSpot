package com.RecomendationService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.RecomendationService.constants.RecomendationConstant;
import com.RecomendationService.entity.Venue;
import com.RecomendationService.exception.VenueNotFoundException;
import com.RecomendationService.service.RecommendationService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping(path = RecomendationConstant.ROOT_PATH)
public class RecommendationController {

	@Autowired
	private RecommendationService recommendationService;


	@GetMapping(path = RecomendationConstant.SEARCH_PATH)
	public ResponseEntity<?> listOfVenues(@RequestParam("query") String query) {
		List<Venue> venues = recommendationService.listVenues(query);
		if (venues.isEmpty()) {
			return new ResponseEntity<String>("venue not found", HttpStatus.CONFLICT);

		} else {
			recommendationService.listVenues(query);
			return new ResponseEntity<List<Venue>>(recommendationService.listVenues(query), HttpStatus.OK);
		}

	}

	@GetMapping(path = RecomendationConstant.SEARCH_LOWTOHIGH_PATH)
	public ResponseEntity<?> listVenuesFromLowToHigh(@RequestParam("query") String query) {
		List<Venue> venues = recommendationService.listVenuesFromLowToHigh(query);
		if (venues.isEmpty()) {
			return new ResponseEntity<String>("venue not found", HttpStatus.CONFLICT);

		} else {
			recommendationService.listVenuesFromLowToHigh(query);
			return new ResponseEntity<List<Venue>>(recommendationService.listVenuesFromLowToHigh(query), HttpStatus.OK);
		}
	}

	@GetMapping(path = RecomendationConstant.SEARCH_HIGHTOLOW_PATH)
	public ResponseEntity<?> listVenuesFromHighToLOw(@RequestParam("query") String query) {
		List<Venue> venues = recommendationService.listVenuesFromHighToLOw(query);
		if (venues.isEmpty()) {
			return new ResponseEntity<String>("venue not found", HttpStatus.CONFLICT);

		} else {
			recommendationService.listVenuesFromHighToLOw(query);
			return new ResponseEntity<List<Venue>>(recommendationService.listVenuesFromHighToLOw(query), HttpStatus.OK);
		}
	}

	@GetMapping(path = RecomendationConstant.SEARCH_BYNAME_PATH)
	public ResponseEntity<?> getVenueByVenueName(@PathVariable("venueName") String venueName) {
		try {
			return new ResponseEntity<>(recommendationService.listVenueByVenueName(venueName), HttpStatus.OK);
		} catch (VenueNotFoundException ex) {
			return new ResponseEntity<>(ex.getErrorMessage(), HttpStatus.CONFLICT);

		} catch (Exception e) {
			return new ResponseEntity<>(RecomendationConstant.VENUE_INFO_MSG, HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping(path = RecomendationConstant.SEARCH_BYPRICEANDCAPACITY_PATH)
	public ResponseEntity<?> findByPriceAndCapacity(@PathVariable("venuePrice") int venuePrice,
			@PathVariable("venueCapacity") int venueCapacity) {
		try {
			return new ResponseEntity<>(recommendationService.findByPriceAndCapacity(venuePrice, venueCapacity),
					HttpStatus.OK);
		} catch (VenueNotFoundException e) {
			return new ResponseEntity<>(e.getErrorMessage(), HttpStatus.CONFLICT);

		}

	}

	@GetMapping(path = RecomendationConstant.SEARCH_BYGREATER_PATH)
	public ResponseEntity<?> getVenueByGreaterThan(@RequestParam String query, @RequestParam int price) {
		List<Venue> venues = recommendationService.listVenuesGreaterThan(query,price);
		if (venues.isEmpty()) {
			return new ResponseEntity<>(RecomendationConstant.VENUE_NOTFOUND_GREATERTHAN_MSG, HttpStatus.CONFLICT);

		} else {
			recommendationService.listVenuesGreaterThan(query, price);
			return new ResponseEntity<List<Venue>>(recommendationService.listVenuesGreaterThan(query,price), HttpStatus.OK);
		}
	}

	@GetMapping(path = RecomendationConstant.SEARCH_BYLESSER_PATH)
	public ResponseEntity<?> getVenueByLesserThan(@RequestParam String query, @RequestParam int price) {
		List<Venue> venues = recommendationService.listVenuesLesserThan(query,price);
		if (venues.isEmpty()) {
			return new ResponseEntity<>(RecomendationConstant.VENUE_NOTFOUND_LESSERTHAN_MSG, HttpStatus.CONFLICT);

		} else {
			recommendationService.listVenuesLesserThan(query, price);
			return new ResponseEntity<List<Venue>>(recommendationService.listVenuesLesserThan(query,price), HttpStatus.OK);
		}
	}

//	@GetMapping(path = RecomendationConstant.SEARCH_BYINBETWEEN_PATH)
//	public ResponseEntity<List<Venue>> getVenueByInBetween(@RequestParam String query, @RequestParam int price1,
//			@RequestParam int price2) {
//		return new ResponseEntity<List<Venue>>(recommendationService.listVenuesInBetween(query, price1, price2),
//				HttpStatus.OK);
//
//	}
	
	@GetMapping(path = RecomendationConstant.SEARCH_BYINBETWEEN_PATH)
	public ResponseEntity<?> getVenueByInBetween(@RequestParam String query, @RequestParam int price1,@RequestParam int price2) {
		List<Venue> venues = recommendationService.listVenuesInBetween(query,price1,price2);
		if (venues.isEmpty()) {
			return new ResponseEntity<>(RecomendationConstant.VENUE_NOTFOUND_INBETWEEN_MSG, HttpStatus.CONFLICT);

		} else {
			recommendationService.listVenuesInBetween(query, price1,price2);
			return new ResponseEntity<List<Venue>>(recommendationService.listVenuesInBetween(query,price1,price2), HttpStatus.OK);
		}
	}
	

	@GetMapping(path = RecomendationConstant.SEARCH_CAPACITY_LOWTOHIGH_PATH)
	public ResponseEntity<?> listVenuesCapacityFromLowToHigh(@RequestParam("query") String query) {
		List<Venue> venues = recommendationService.listVenuesFromLowToHighCapacity(query);
		if (venues.isEmpty()) {
			return new ResponseEntity<>(RecomendationConstant.VENUE_NOTFOUND_MSG, HttpStatus.CONFLICT);

		} else {
			recommendationService.listVenuesFromLowToHighCapacity(query);
			return new ResponseEntity<List<Venue>>(recommendationService.listVenuesFromLowToHighCapacity(query), HttpStatus.OK);
		}
	}
	
	@GetMapping(path = RecomendationConstant.SEARCH_CAPACITY_HIGHTOLOW_PATH)
	public ResponseEntity<?> listVenuesCapacityFromHighToLow(@RequestParam("query") String query) {
		List<Venue> venues = recommendationService.listVenuesFromHighToLOwCapacity(query);
		if (venues.isEmpty()) {
			return new ResponseEntity<>(RecomendationConstant.VENUE_NOTFOUND_MSG, HttpStatus.CONFLICT);

		} else {
			recommendationService.listVenuesFromHighToLOwCapacity(query);
			return new ResponseEntity<List<Venue>>(recommendationService.listVenuesFromHighToLOwCapacity(query), HttpStatus.OK);
		}
	}
	
	@GetMapping(path = RecomendationConstant.SEARCH_CAPACITY_BYGREATER_PATH)
	public ResponseEntity<?> getVenueByCapacityGreaterThan(@RequestParam String query, @RequestParam int size) {
		List<Venue> venues = recommendationService.listVenuesGreaterThanCapacity(query,size);
		if (venues.isEmpty()) {
			return new ResponseEntity<>(RecomendationConstant.VENUE_NOTFOUND_GREATERTHAN_CAPACITY_MSG, HttpStatus.CONFLICT);

		} else {
			recommendationService.listVenuesGreaterThanCapacity(query, size);
			return new ResponseEntity<List<Venue>>(recommendationService.listVenuesGreaterThanCapacity(query,size), HttpStatus.OK);
		}
	}
	
	@GetMapping(path = RecomendationConstant.SEARCH_CAPACITY_BYLESSER_PATH)
	public ResponseEntity<?> getVenueByCapacityLesserThan(@RequestParam String query, @RequestParam int size) {
		List<Venue> venues = recommendationService.listVenuesLesserThanCapacity(query,size);
		if (venues.isEmpty()) {
			return new ResponseEntity<>(RecomendationConstant.VENUE_NOTFOUND_LESSERTHAN_CAPACITY_MSG, HttpStatus.CONFLICT);

		} else {
			recommendationService.listVenuesLesserThanCapacity(query, size);
			return new ResponseEntity<List<Venue>>(recommendationService.listVenuesLesserThanCapacity(query,size), HttpStatus.OK);
		}
	}
	
	@GetMapping(path = RecomendationConstant.SEARCH_CAPACITY_BYINBETWEEN_PATH)
	public ResponseEntity<?> getVenueByCapacityInBetween(@RequestParam String query, @RequestParam int size1,@RequestParam int size2) {
		List<Venue> venues = recommendationService.listVenuesInBetweenCapacity(query,size1,size2);
		if (venues.isEmpty()) {
			return new ResponseEntity<>(RecomendationConstant.VENUE_NOTFOUND_INBETWEEN_CAPACITY_MSG, HttpStatus.CONFLICT);

		} else {
			recommendationService.listVenuesInBetweenCapacity(query, size1,size2);
			return new ResponseEntity<List<Venue>>(recommendationService.listVenuesInBetweenCapacity(query,size1,size2), HttpStatus.OK);
		}
	}
	
	@GetMapping(path = RecomendationConstant.SEARCH_BY_USER_CITY)
	public ResponseEntity<?> getAllVenuesAsperUserCity(@RequestAttribute(name = "claims") Claims claims) {
		try {
			return new ResponseEntity<>(recommendationService.getAllVenuesAsperUserCity(claims.getSubject()), HttpStatus.CREATED);
		
		} catch (VenueNotFoundException venueNotFound) {
			return new ResponseEntity<String>(venueNotFound.getErrorMessage(), HttpStatus.NOT_FOUND);
		}
	}

}

