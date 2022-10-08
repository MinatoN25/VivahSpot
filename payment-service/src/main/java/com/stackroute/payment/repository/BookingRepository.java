package com.stackroute.payment.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.stackroute.payment.model.Booking;

@Repository
public interface BookingRepository extends MongoRepository<Booking, Integer> {

}
