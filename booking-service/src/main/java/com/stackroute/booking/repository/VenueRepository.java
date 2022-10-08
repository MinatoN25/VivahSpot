package com.stackroute.booking.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.stackroute.booking.model.Venue;

@Repository
public interface VenueRepository extends MongoRepository<Venue, Integer> {

}
