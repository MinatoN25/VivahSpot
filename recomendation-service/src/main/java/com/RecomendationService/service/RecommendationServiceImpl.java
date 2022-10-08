package com.RecomendationService.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RecomendationService.constants.RecomendationConstant;
import com.RecomendationService.entity.User;
import com.RecomendationService.entity.Venue;
import com.RecomendationService.exception.VenueNotFoundException;
import com.RecomendationService.repository.RecommendationRepository;
import com.RecomendationService.repository.UserRepository;

@Service
public class RecommendationServiceImpl implements RecommendationService {

	@Autowired
	private RecommendationRepository recommendationRepository;
	
	@Autowired
	private UserRepository userRepository;


	@Override
	public List<Venue> listVenues(String query) {
		List<Venue> venues = recommendationRepository.getVenue(query);
		System.out.println(venues);
//		if (venues.isEmpty()|| venues==null) {
//		    throw new VenueNotFoundException("Venue Not Found");
//		}
		return venues;
	}

	@Override
	public List<Venue> listVenueByVenueName(String venueName) {
		List<Venue> venues = recommendationRepository.getByVenueName(venueName);
		if (venues.isEmpty()) {
			throw new VenueNotFoundException(RecomendationConstant.VENUE_INFO_MSG);
		}
		return venues;
	}

	@Override
	public List<Venue> findByPriceAndCapacity(Integer venuePrice, Integer venueCapacity) {
		List<Venue> venues = recommendationRepository.findByPriceAndCapacity(venuePrice, venueCapacity);
		if (venues.isEmpty()) {
			throw new VenueNotFoundException(RecomendationConstant.VENUE_NOTFOUND_ASPER_PRICEANDCAPACITY_MSG);
		}
		return venues;
	}

	@Override
	public List<Venue> listVenuesFromLowToHigh(String query) {
		List<Venue> venues = recommendationRepository.findVenueByVenuePriceLowtoHigh(query);
//		if (venues.isEmpty()) {
//			throw new VenueNotFoundException("Venue Not Found");
//		}
		return venues;
	}

	@Override
	public List<Venue> listVenuesFromHighToLOw(String query) {
		List<Venue> venues = recommendationRepository.findVenueByVenuePriceHighToLow(query);
//		if (venues.isEmpty()){
//			throw new VenueNotFoundException("Venue Not Found");
//		}
		return venues;
	}

	@Override
	public List<Venue> listVenuesGreaterThan(String query, int price) {
		List<Venue> venues = recommendationRepository.getVenue(query).stream()
				.filter(venue1 -> venue1.getVenuePrice() > price).collect(Collectors.toList());

//				.sorted((o1, o2)->o1.getVenuePrice().
//                compareTo(o2.getVenuePrice())).
//                collect(Collectors.toList());;S

//		if(venues.isEmpty()) {
//			throw new VenueNotFoundException(RecomendationConstant.VENUE_NOTFOUND_GREATERTHAN_MSG);
//		}
		return venues;
	}

	@Override
	public List<Venue> listVenuesLesserThan(String query, int price) {
		List<Venue> venues = recommendationRepository.getVenue(query).stream()
				.filter(venue1 -> venue1.getVenuePrice() < price).collect(Collectors.toList());

//		if (venues.isEmpty()) {
//			throw new VenueNotFoundException("Venue Not Found");
//		}
		return venues;
	}

	@Override
	public List<Venue> listVenuesInBetween(String query, int price1, int price2) {
		List<Venue> venues = recommendationRepository.getVenue(query).stream()
				.filter(venue1 -> venue1.getVenuePrice() < price1 && venue1.getVenuePrice() > price2 ).collect(Collectors.toList());

//		if(venues.isEmpty()) {
//			throw new VenueNotFoundException("Venue Not Found");
//		}
		return venues;
	}

	@Override
	public List<Venue> listVenuesFromLowToHighCapacity(String query) {
		List<Venue> venues = recommendationRepository.findVenueByVenueCapacityLowtoHigh(query);
		return venues;
	}

	@Override
	public List<Venue> listVenuesFromHighToLOwCapacity(String query) {
		List<Venue> venues = recommendationRepository.findVenueByVenueCapacityHighToLow(query);
		return venues;
	}

	@Override
	public List<Venue> listVenuesGreaterThanCapacity(String query, int size) {
		List<Venue> venues = recommendationRepository.getVenue(query).stream()
				.filter(venue1 -> venue1.getVenueCapacity() > size).collect(Collectors.toList());
		return venues;
	}

	@Override
	public List<Venue> listVenuesLesserThanCapacity(String query, int size) {
		List<Venue> venues = recommendationRepository.getVenue(query).stream()
				.filter(venue1 -> venue1.getVenueCapacity() < size).collect(Collectors.toList());
		return venues;
	}

	@Override
	public List<Venue> listVenuesInBetweenCapacity(String query, int size1, int size2) {
		List<Venue> venues = recommendationRepository.getVenue(query).stream()
				.filter(venue1 -> venue1.getVenueCapacity() < size1 && venue1.getVenueCapacity() > size2 ).collect(Collectors.toList());

		return venues;
	}

	@Override
	public List<Venue> getAllVenuesAsperUserCity(String userEmail) {
		User user = userRepository.getuserbyEmail(userEmail);
		List<Venue> venues = recommendationRepository.getAllVenuesAsperUserCity(user.getCity());
		if(venues.isEmpty()) {
			throw new VenueNotFoundException("No venues found for " + user.getCity());
		}
		return venues;
	}

//	@Override
//	public List<Venue> getAllVenuesAsperUserCity(String userEmail) {
//		
//		User user = userRepository.getuserbyEmail(userEmail);
//		city=user.getCity();
//		List<Venue> venue = recommendationRepository.getAllVenuesAsperUserCity(userEmail,city);
//		
//		return venue;
//	}

	

}
