package com.RecomendationService.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.RecomendationService.entity.Venue;

@Repository
public interface RecommendationRepository extends MongoRepository<Venue, Integer> {

	@Query(value = "{$or:[{city:{$regex:?0,$options:'i'}},{cuisine:{$regex:?0,$options:'i'}},{venueName:{$regex:?0,$options:'i'}},{venueSpaceType:{$regex:?0,$options:'i'}}]}")
	List<Venue> getVenue(String query);
	
	@Query(value = "{$or:[{city:{$regex:?0,$options:'i'}},{cuisine:{$regex:?0,$options:'i'}},{venueName:{$regex:?0,$options:'i'}},{venueSpaceType:{$regex:?0,$options:'i'}}]}",sort = "{venuePrice : 1}")
	List<Venue> findVenueByVenuePriceLowtoHigh(String query);
	
	@Query(value = "{$or:[{city:{$regex:?0,$options:'i'}},{cuisine:{$regex:?0,$options:'i'}},{venueName:{$regex:?0,$options:'i'}},{venueSpaceType:{$regex:?0,$options:'i'}}]}",sort = "{venuePrice : -1}")
	List<Venue> findVenueByVenuePriceHighToLow(String query);

	@Query
	("{venueName: ?0}")
	List<Venue> getByVenueName(String venueName);

	@Query
	("{'venuePrice': ?0, 'venueCapacity': ?1}")
	List<Venue> findByPriceAndCapacity(int venuePrice, int venueCapacity);
	
	

//	@Query("{venuePrice : {$gt: ?0}}")
//	List<Venue> findByVenueGreaterThan(int venuePrice);
	
	@Query(value = "{$or:[{city:{$regex:?0,$options:'i'}},{cuisine:{$regex:?0,$options:'i'}},{venueName:{$regex:?0,$options:'i'}},{venueSpaceType:{$regex:?0,$options:'i'}}]}")
	List<Venue> getVenueGreater(String query);
	
	
	@Query(value = "{$or:[{city:{$regex:?0,$options:'i'}},{cuisine:{$regex:?0,$options:'i'}},{venueName:{$regex:?0,$options:'i'}},{venueSpaceType:{$regex:?0,$options:'i'}}]}")
	List<Venue> getVenueLesser(String query);
	
	@Query(value = "{$or:[{city:{$regex:?0,$options:'i'}},{cuisine:{$regex:?0,$options:'i'}},{venueName:{$regex:?0,$options:'i'}},{venueSpaceType:{$regex:?0,$options:'i'}}]}")
	List<Venue> getVenueInBetween(String query);
	
	
	@Query(value = "{$or:[{city:{$regex:?0,$options:'i'}},{cuisine:{$regex:?0,$options:'i'}},{venueName:{$regex:?0,$options:'i'}},{venueSpaceType:{$regex:?0,$options:'i'}}]}",sort = "{venueCapacity : 1}")
	List<Venue> findVenueByVenueCapacityLowtoHigh(String query);
	
	@Query(value = "{$or:[{city:{$regex:?0,$options:'i'}},{cuisine:{$regex:?0,$options:'i'}},{venueName:{$regex:?0,$options:'i'}},{venueSpaceType:{$regex:?0,$options:'i'}}]}",sort = "{venueCapacity : -1}")
	List<Venue> findVenueByVenueCapacityHighToLow(String query);
	
	@Query(value = "{$or:[{city:{$regex:?0,$options:'i'}},{cuisine:{$regex:?0,$options:'i'}},{venueName:{$regex:?0,$options:'i'}},{venueSpaceType:{$regex:?0,$options:'i'}}]}")
	List<Venue> getVenueGreaterCapacity(String query);
	
	
	@Query(value = "{$or:[{city:{$regex:?0,$options:'i'}},{cuisine:{$regex:?0,$options:'i'}},{venueName:{$regex:?0,$options:'i'}},{venueSpaceType:{$regex:?0,$options:'i'}}]}")
	List<Venue> getVenueLesserCapacity(String query);
	
	@Query(value = "{$or:[{city:{$regex:?0,$options:'i'}},{cuisine:{$regex:?0,$options:'i'}},{venueName:{$regex:?0,$options:'i'}},{venueSpaceType:{$regex:?0,$options:'i'}}]}")
	List<Venue> getVenueInBetweenCapacity(String query);
	
	
	@Query("{'city':?0}")
	List<Venue> getAllVenuesAsperUserCity(String city);

	
	
//	@Query("{venuePrice : {$lt: ?0}}")
//	List<Venue> findByVenueLessThan(int venuePrice);
	
//	@Query("{venuePrice : {$gt : ?0, $lt : ?1}}")
//	List<Venue> findByVenueBetween(int from, int to);
//	
//	@Query(value = "{venueId : ?0}", sort = "{venuePrice : 1}")
//	List<Venue> findByVenueAscending(int venueId);


}

//@Query(value ="{venuePrice : {$gt: ?0}}", sort = "{venuePrice : 1}")
//@Query
//("{'venuePrice': { $range: [ 0, 5000 ]}}")


