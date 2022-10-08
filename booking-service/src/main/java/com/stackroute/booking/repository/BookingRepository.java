package com.stackroute.booking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.stackroute.booking.model.Booking;

@Repository
public interface BookingRepository extends MongoRepository<Booking, Integer> {

	@Query(value = "{venueOwnerEmail : ?0}")
	List<Booking> getBookingsForVenueOwner(String venueOwnerEmail);
	
	@Query(value = "{userEmail : ?0}")
	List<Booking> getBookingsForUser(String userEmail);
	
	@Query("{'venueOwnerEmail':?0, 'slot.slotStatus':?1}")
	List<Booking> getBookingForVenueOwnerByStatus(String venueOwnerEmail, String status);
	
	@Query("{'userEmail':?0, 'slot.slotStatus':?1}")
	List<Booking> getBookingForUserByStatus(String userEmail, String status);
	
	@Query("{'venueId': ?0, 'bookingDate': ?1, 'slotId': ?2, 'slot.slotStatus':?3}")
	List<Booking> getBookingByVenueStatusAndBookingDate(int venueId, LocalDate bookingDate, int slotId, String status);
	
	@Query("{'venueId': ?0, 'bookingDate': ?1, 'slotId': ?2, 'userEmail': ?3, 'slot.slotStatus':?4}")
	List<Booking> getBookingForUserByVenueStatusAndBookingDate(int venueId, LocalDate bookingDate, int slotId,String userEmail, String status);

	@Query("{'userEmail':?0, 'venueId':?1}")
	List<Booking> getBookingForUserByVenueId(String userEmail, int venueId);

	@Query("{'userEmail':?0, 'venueId':?1, 'slot.slotStatus':?2}")
	List<Booking> getBookingForUserByVenueIdAndStatus(String userEmail, int venueId, String status);
	
	@Query("{'venueOwnerEmail':?0, 'venueId':?1}")
	List<Booking> getBookingForVenueOwnerByVenueId(String venueOwnerEmail, int venueId);

	@Query("{'venueOwnerEmail':?0, 'venueId':?1, 'slot.slotStatus':?2}")
	List<Booking> getBookingForVenueOwnerByVenueIdAndStatus(String venueOwnerEmail, int venueId, String status);
	

	
}
