package com.stackroute.booking.constants;

public class BookingConstants {
	public static final String ROOT_PATH = "/api/v1";
	public static final String BOOKING_PATH = "/bookVenue";
	public static final String CANCEL_BOOKING_PATH = "/cancelBooking/{bookingId}";
	public static final String GET_BOOKING_PATH = "/getBooking/{bookingId}";
	public static final String GET_OWNER_BOOKING_HISTORY_PATH = "/getAllBookingsForOwner";
	public static final String GET_USER_BOOKING_HISTORY_PATH = "/getAllBookingsForUser";
	public static final String BOOKING_ACCEPT_REJECT_PATH = "/bookingApproval/{bookingId}/{approval}";
	public static final String GET_OWNER_BOOKINGS_BY_STATUS_PATH = "/getAllBookingsForOwnerByStatus/{slotStatus}";
	public static final String GET_USER_BOOKINGS_BY_STATUS_PATH = "/getAllBookingsForUserByStatus/{slotStatus}";
	
	public static final String GET_OWNER_BOOKINGS_BY_VENUE_STATUS_PATH = "/getAllBookingsForOwnerByVenueIdAndStatus/{venueId}/{slotStatus}";
	public static final String GET_USER_BOOKINGS_BY_VENUE_STATUS_PATH = "/getAllBookingsForUserByVenueIdAndStatus/{venueId}/{slotStatus}";
	
	public static final String GET_OWNER_BOOKINGS_BY_VENUE_ID_PATH = "/getAllBookingsForOwnerByVenueId/{venueId}";
	public static final String GET_USER_BOOKINGS_BY_VENUE_ID_PATH = "/getAllBookingsForUserByVenueId/{venueId}";
	
	public static final String CHECK_AVAILABLITY_PATH = "/isAvailable";
	public static final String BAD_REQUEST_MSG = "Bad Request";
	public static final String BOOKING_INFO_MSG = "Please enter all the required fields for booking";
	public static final String VENUE_BOOKED_MSG = "Venue and slot is booked for the date entered";
	public static final String VENUE_AVAILABLE_MSG = "Venue and slot is available for the date entered";
	public static final String CANCEL_REFUND_MSG = "Your booking has been cancelled and your money will be refunded as per terms and conditions";
	public static final String BOOKING_ACCEPTED_MSG = "Booking Accepted";
	public static final String BOOKING_REJECTED_MSG = "Booking Rejected";
	public static final String INVALID_APPROVAL_MSG = "Please select either accept or reject";
	public static final String CANCEL_REFUND_MSG1 = "Your booking has been cancelled and ";
	public static final String CANCEL_REFUND_MSG2 = " will be refunded within 5 working days. \nPlease contact support team in case of any queries";
	
}
