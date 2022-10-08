package com.stackroute.booking.controller;

import java.time.LocalDate;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.booking.config.MQConfig;
import com.stackroute.booking.constants.BookingConstants;
import com.stackroute.booking.exception.ApprovalNotRequiredException;
import com.stackroute.booking.exception.BookingNotFoundException;
import com.stackroute.booking.exception.DateBeforeCurrentDateException;
import com.stackroute.booking.exception.InvalidBookingRequestException;
import com.stackroute.booking.exception.NotEligibleForRefundException;
import com.stackroute.booking.exception.SlotNotFoundException;
import com.stackroute.booking.exception.UnauthorizedException;
import com.stackroute.booking.exception.VenueAlreadyBookedException;
import com.stackroute.booking.exception.VenueNotBookedException;
import com.stackroute.booking.exception.VenueNotFoundException;
import com.stackroute.booking.model.Approval;
import com.stackroute.booking.model.Booking;
import com.stackroute.booking.model.Status;
import com.stackroute.booking.service.BookingService;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = BookingConstants.ROOT_PATH)
public class BookingController {

	private BookingService bookingservice;
	private RabbitTemplate rabbitTemplate;

	@Autowired
	public BookingController(BookingService bservice, RabbitTemplate rabbitTemplate) {
		this.bookingservice = bservice;
		this.rabbitTemplate = rabbitTemplate;
	}

	@ApiOperation(value = "This method is used to initiate a booking request")
	@PostMapping(path = BookingConstants.BOOKING_PATH)
	public ResponseEntity<?> slotBooking(@RequestParam Integer venueId, @RequestParam Integer slotId,
			@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate bookingDate,
			@RequestAttribute(name = "claims") Claims claims) {

		try {
			Booking booking = new Booking();
			booking.setUserEmail(claims.getSubject());
			booking.setBookingDate(bookingDate);
			booking.setVenueId(venueId);
			booking.setSlotId(slotId);
			return new ResponseEntity<>(bookingservice.slotBooking(booking), HttpStatus.CREATED);
		} catch (DateBeforeCurrentDateException dateEx) {
			return new ResponseEntity<>(dateEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (VenueNotFoundException venueEx) {
			return new ResponseEntity<>(venueEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (SlotNotFoundException slotEx) {
			return new ResponseEntity<>(slotEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (VenueAlreadyBookedException bookedEx) {
			return new ResponseEntity<>(bookedEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (InvalidBookingRequestException invalidBookingEx) {
			return new ResponseEntity<>(invalidBookingEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(BookingConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}

	}

	@ApiOperation(value = "This method is used to cancel a booking request")
	@PutMapping(path = BookingConstants.CANCEL_BOOKING_PATH)
	public ResponseEntity<?> cancelBooking(@PathVariable int bookingId,@RequestAttribute(name = "claims") Claims claims) {
		try {
			Booking booking =bookingservice.cancelBooking(bookingId, claims.getSubject());
			rabbitTemplate.convertAndSend(MQConfig.REFUND_EXCHANGE, MQConfig.REFUND_ROUTING_KEY, booking);
			return new ResponseEntity<>(BookingConstants.CANCEL_REFUND_MSG1+booking.getCurrency()+" "
					+booking.getRefundAmount()+BookingConstants.CANCEL_REFUND_MSG2, HttpStatus.OK);

		} catch (BookingNotFoundException bookingEx) {
			return new ResponseEntity<>(bookingEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (UnauthorizedException authEx) {
			return new ResponseEntity<>(authEx.getErrorMessage(), HttpStatus.UNAUTHORIZED);
		} catch (NotEligibleForRefundException refundEx) {
			return new ResponseEntity<>(refundEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (VenueNotBookedException notBookedEx) {
			return new ResponseEntity<>(notBookedEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(BookingConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}

	}

	@ApiOperation(value = "This method is used to get a booking by id")
	@GetMapping(path = BookingConstants.GET_BOOKING_PATH)
	public ResponseEntity<?> getBookingById(@PathVariable int bookingId) {
		try {
			return new ResponseEntity<>(bookingservice.getBookingById(bookingId), HttpStatus.OK);
		} catch (BookingNotFoundException bookingEx) {
			return new ResponseEntity<>(bookingEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(BookingConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "This method is used to check if booking is available for the date")
	@GetMapping(path = BookingConstants.CHECK_AVAILABLITY_PATH)
	public ResponseEntity<?> checkIfAvailable(@RequestParam int venueId, @RequestParam int slotId,
			@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate bookingDate) {
		try {
			if (bookingservice.checkAvailability(venueId, slotId, bookingDate)) {
				return new ResponseEntity<>(BookingConstants.VENUE_AVAILABLE_MSG, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(BookingConstants.VENUE_BOOKED_MSG, HttpStatus.OK);
			}

		} catch (DateBeforeCurrentDateException dateEx) {
			return new ResponseEntity<>(dateEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (VenueNotFoundException venueEx) {
			return new ResponseEntity<>(venueEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (SlotNotFoundException slotEx) {
			return new ResponseEntity<>(slotEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(BookingConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}

	}

	@ApiOperation(value = "This method is used by venue owner to accept or reject booking")
	@PutMapping(path = BookingConstants.BOOKING_ACCEPT_REJECT_PATH)
	public ResponseEntity<?> bookingApproval(@PathVariable Approval approval, @PathVariable Integer bookingId,@RequestAttribute(name = "claims") Claims claims) {
		try {
			return new ResponseEntity<>(bookingservice.bookingApproval(approval, bookingId, claims.getSubject()), HttpStatus.ACCEPTED);
		} catch (UnauthorizedException authEx) {
			return new ResponseEntity<>(authEx.getErrorMessage(), HttpStatus.UNAUTHORIZED);
		} catch (ApprovalNotRequiredException approvalEx) {
			return new ResponseEntity<>(approvalEx.getErrorMessage(), HttpStatus.UNAUTHORIZED);
		} catch (BookingNotFoundException bookingEx) {
			return new ResponseEntity<>(bookingEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(BookingConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "This method is used by venue owner to get the booking history")
	@GetMapping(path = BookingConstants.GET_OWNER_BOOKING_HISTORY_PATH)
	public ResponseEntity<?> getAllVenuesByVenueOwnerEmail(@RequestAttribute(name = "claims") Claims claims) {
		try {
			return new ResponseEntity<>(bookingservice.getAllBookingsForVenueOwner(claims.getSubject()), HttpStatus.OK);
		} catch (BookingNotFoundException bookingEx) {
			return new ResponseEntity<>(bookingEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(BookingConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}

	}

	@ApiOperation(value = "This method is used by venue owner to filter the booking history by status")
	@GetMapping(path = BookingConstants.GET_OWNER_BOOKINGS_BY_STATUS_PATH)
	public ResponseEntity<?> getAllVenuesByVenueOwnerEmailAndStatus(@PathVariable Status slotStatus,
			@RequestAttribute(name = "claims") Claims claims) {
		try {

			return new ResponseEntity<>(
					bookingservice.getAllBookingsForVenueOwnerByStatus(claims.getSubject(), slotStatus), HttpStatus.OK);
		} catch (BookingNotFoundException bookingEx) {
			return new ResponseEntity<>(bookingEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(BookingConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}

	}

	@ApiOperation(value = "This method is used by user to get the booking history")
	@GetMapping(path = BookingConstants.GET_USER_BOOKING_HISTORY_PATH)
	public ResponseEntity<?> getAllVenuesByUserEmail(@RequestAttribute(name = "claims") Claims claims) {
		try {
			return new ResponseEntity<>(bookingservice.getAllBookingsForUser(claims.getSubject()), HttpStatus.OK);
		} catch (BookingNotFoundException bookingEx) {
			return new ResponseEntity<>(bookingEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(BookingConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}

	}

	@ApiOperation(value = "This method is used by user to filter the booking history by status")
	@GetMapping(path = BookingConstants.GET_USER_BOOKINGS_BY_STATUS_PATH)
	public ResponseEntity<?> getAllVenuesByUserEmailAndStatus(@PathVariable Status slotStatus,
			@RequestAttribute(name = "claims") Claims claims) {
		try {

			return new ResponseEntity<>(bookingservice.getAllBookingsForUserByStatus(claims.getSubject(), slotStatus),
					HttpStatus.OK);
		} catch (BookingNotFoundException bookingEx) {
			return new ResponseEntity<>(bookingEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(BookingConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}

	}

	@ApiOperation(value = "This method is used by user to get the booking history for a particular venue")
	@GetMapping(path = BookingConstants.GET_USER_BOOKINGS_BY_VENUE_ID_PATH)
	public ResponseEntity<?> getAllVenuesByUserEmailAndVenueId(@RequestAttribute(name = "claims") Claims claims,
			@PathVariable int venueId) {
		try {
			return new ResponseEntity<>(bookingservice.getAllBookingsForUserByVenueId(claims.getSubject(), venueId),
					HttpStatus.OK);
		} catch (BookingNotFoundException bookingEx) {
			return new ResponseEntity<>(bookingEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(BookingConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}

	}

	@ApiOperation(value = "This method is used by user to get the booking history for a particular venue by booking status")
	@GetMapping(path = BookingConstants.GET_USER_BOOKINGS_BY_VENUE_STATUS_PATH)
	public ResponseEntity<?> getAllVenuesByUserEmailVenueIdAndStatus(@PathVariable int venueId,
			@PathVariable Status slotStatus, @RequestAttribute(name = "claims") Claims claims) {
		try {

			return new ResponseEntity<>(
					bookingservice.getAllBookingsForUserByVenueIdAndStatus(claims.getSubject(), venueId, slotStatus),
					HttpStatus.OK);
		} catch (BookingNotFoundException bookingEx) {
			return new ResponseEntity<>(bookingEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(BookingConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}

	}

	@ApiOperation(value = "This method is used by venue owner to get the booking history for a particular venue")
	@GetMapping(path = BookingConstants.GET_OWNER_BOOKINGS_BY_VENUE_ID_PATH)
	public ResponseEntity<?> getAllVenuesByVenueOwnerEmailAndVenueId(@RequestAttribute(name = "claims") Claims claims,
			@PathVariable int venueId) {
		try {
			return new ResponseEntity<>(
					bookingservice.getAllBookingsForVenueOwnerByVenueId(claims.getSubject(), venueId), HttpStatus.OK);
		} catch (BookingNotFoundException bookingEx) {
			return new ResponseEntity<>(bookingEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(BookingConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}

	}

	@ApiOperation(value = "This method is used by venue owner to get the booking history for a particular venue by booking status")
	@GetMapping(path = BookingConstants.GET_OWNER_BOOKINGS_BY_VENUE_STATUS_PATH)
	public ResponseEntity<?> getAllVenuesByVenueOwnerEmailVenueIdAndStatus(@PathVariable int venueId,
			@PathVariable Status slotStatus, @RequestAttribute(name = "claims") Claims claims) {
		try {

			return new ResponseEntity<>(bookingservice.getAllBookingsForVenueOwnerByVenueIdAndStatus(
					claims.getSubject(), venueId, slotStatus), HttpStatus.OK);
		} catch (BookingNotFoundException bookingEx) {
			return new ResponseEntity<>(bookingEx.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception ex) {
			return new ResponseEntity<>(BookingConstants.BAD_REQUEST_MSG, HttpStatus.BAD_REQUEST);
		}

	}
}
