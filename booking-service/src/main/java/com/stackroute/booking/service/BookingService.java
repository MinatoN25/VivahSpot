package com.stackroute.booking.service;

import java.time.LocalDate;
import java.util.List;

import com.stackroute.booking.model.Approval;
import com.stackroute.booking.model.Booking;
import com.stackroute.booking.model.Status;

public interface BookingService {

	Booking slotBooking(Booking booking);
	Booking cancelBooking(int bookingId, String userEmail);
	Booking getBookingById(int bookingId);
	boolean checkAvailability(int venueId, Integer slotId, LocalDate bookingDate);
	Booking bookingApproval(Approval approval, int bookingId, String ownerEmail);
	List<Booking> getAllBookingsForVenueOwner (String venueOwnerEmail);
	List<Booking> getAllBookingsForVenueOwnerByStatus (String venueOwnerEmail, Status slotStatus);
	List<Booking> getAllBookingsForUser (String userEmail);
	List<Booking> getAllBookingsForUserByStatus (String userEmail, Status slotStatus);
	List<Booking> getAllBookingsForUserByVenueIdAndStatus(String userEmail,int venueId, Status slotStatus);
	List<Booking> getAllBookingsForUserByVenueId(String userEmail, int venueId);
	List<Booking> getAllBookingsForVenueOwnerByVenueIdAndStatus(String venueOwnerEmail,int venueId, Status slotStatus);
	List<Booking> getAllBookingsForVenueOwnerByVenueId(String venueOwnerEmail, int venueId);
	
}
