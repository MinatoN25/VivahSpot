package com.stackroute.venueservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.stackroute.venueservice.model.Booking;

@Repository
public interface BookingRepository extends MongoRepository<Booking, Integer> {

	@Query(value = "{userEmail : ?0}")
	List<Booking> getBookingByUserEmail(String userEmail);
}
