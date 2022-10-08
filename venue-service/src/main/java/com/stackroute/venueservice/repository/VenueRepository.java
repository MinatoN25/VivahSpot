package com.stackroute.venueservice.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.stackroute.venueservice.model.Venue;

@Repository
public interface VenueRepository extends MongoRepository<Venue, Integer> {
	
	@Query
	("{'venueName': ?0}")
	List<Venue> findByName(String venueName);
}

