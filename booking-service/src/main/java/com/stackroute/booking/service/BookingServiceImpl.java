package com.stackroute.booking.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

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
import com.stackroute.booking.model.DatabaseSequence;
import com.stackroute.booking.model.Slot;
import com.stackroute.booking.model.Status;
import com.stackroute.booking.model.Venue;
import com.stackroute.booking.repository.BookingRepository;
import com.stackroute.booking.repository.VenueRepository;

@Service
public class BookingServiceImpl implements BookingService {

	private BookingRepository bookingrepo;
	private VenueRepository venuerepo;
	private MongoOperations mongo;

	@Autowired
	public BookingServiceImpl(BookingRepository bookingrepo, VenueRepository venuerepo, MongoOperations mongo) {
		super();
		this.bookingrepo = bookingrepo;
		this.venuerepo = venuerepo;
		this.mongo = mongo;
	}

	@Override
	public Booking slotBooking(Booking booking) {

		if (booking.getBookingDate().isAfter(LocalDate.now())) {
			Optional<Venue> venue = venuerepo.findById(booking.getVenueId());
			if (venue.isEmpty()) {
				throw new VenueNotFoundException("Venue not found in the system");
			}
			List<Slot> slot = venue.get().getSlots().stream()
					.filter(slot1 -> booking.getSlotId().equals(slot1.getSlotId())).collect(Collectors.toList());

			if (slot.isEmpty()) {
				throw new SlotNotFoundException("Slot not found for the venue");
			}

			if (!bookingrepo.getBookingByVenueStatusAndBookingDate(booking.getVenueId(), booking.getBookingDate(),
					booking.getSlotId(), Status.BOOKED.name()).isEmpty()) {
				throw new VenueAlreadyBookedException("Venue is already booked, please select another slot or date");
			}

			if (!bookingrepo
					.getBookingForUserByVenueStatusAndBookingDate(booking.getVenueId(), booking.getBookingDate(),
							booking.getSlotId(), booking.getUserEmail(), Status.PENDING_FOR_APPROVAL.name())
					.isEmpty()) {

				throw new InvalidBookingRequestException(
						"Booking request has already been sent to the venue owner, please wait until your booking is accepted");

			} else if (!bookingrepo
					.getBookingForUserByVenueStatusAndBookingDate(booking.getVenueId(), booking.getBookingDate(),
							booking.getSlotId(), booking.getUserEmail(), Status.PENDING_PAYMENT.name())
					.isEmpty()) {

				throw new InvalidBookingRequestException(
						"Your booking request is already open and is pending for payment");
			}

			booking.setBookingId(getNextSequence(Booking.SEQUENCE_NAME));
			booking.setVenueOwnerEmail(venue.get().getVenueOwnerEmail());
			booking.setCurrency(venue.get().getCurrency());
			booking.setVenuePrice(venue.get().getVenuePrice());
			booking.setVenueName(venue.get().getVenueName());

			booking.setSlot(new Slot(booking.getSlotId(), Status.PENDING_FOR_APPROVAL, slot.get(0).getSlotTime()));

			return bookingrepo.save(booking);
		} else {
			throw new DateBeforeCurrentDateException("Please select date after current date");
		}

	}

	@Override
	public Booking cancelBooking(int bookingId, String userEmail) {
		Optional<Booking> b = bookingrepo.findById(bookingId);
		if (!b.isPresent()) {
			throw new BookingNotFoundException("Booking not found in the system");
		} else if(!b.get().getUserEmail().equals(userEmail)) {
			throw new UnauthorizedException("You are not authorized to cancel this booking");
		}
			else if (!b.get().getSlot().getSlotStatus().equals(Status.BOOKED)) {
			throw new VenueNotBookedException("Venue is not booked");
		}
		if (LocalDate.now().isAfter(b.get().getBookingDate().minusDays(2))) {
			b.get().getSlot().setSlotStatus(Status.CANCELLED);
			bookingrepo.save(b.get());
			throw new NotEligibleForRefundException("Cancelled!!! Your money will not be refunded");

		} else {
			b.get().getSlot().setSlotStatus(Status.CANCELLED);
			b.get().setRefundAmount((80*b.get().getVenuePrice())/100);
			bookingrepo.save(b.get());
			return bookingrepo.findById(b.get().getBookingId()).get();
		}
	}

	@Override
	public Booking getBookingById(int bookingId) {
		Optional<Booking> b = bookingrepo.findById(bookingId);
		if (!b.isPresent()) {
			throw new BookingNotFoundException("Booking not found in the system");
		} else {
			return b.get();
		}
	}

	@Override
	public boolean checkAvailability(int venueId, Integer slotId, LocalDate bookingDate) {
		if (bookingDate.isAfter(LocalDate.now())) {
			Optional<Venue> venue = venuerepo.findById(venueId);
			if (venue.isEmpty()) {
				throw new VenueNotFoundException("Venue not found in the system");
			}
			List<Slot> slot = venue.get().getSlots().stream().filter(slot1 -> slotId.equals(slot1.getSlotId()))
					.collect(Collectors.toList());

			if (slot == null || slot.isEmpty()) {
				throw new SlotNotFoundException("Slot not found for the venue");
			}

			return bookingrepo.getBookingByVenueStatusAndBookingDate(venueId, bookingDate, slotId, Status.BOOKED.name())
					.isEmpty();
		} else {
			throw new DateBeforeCurrentDateException("Please select date after current date");
		}
	}

	@Override
	public Booking bookingApproval(Approval approval, int bookingId, String ownerEmail) {
		Optional<Booking> booking = bookingrepo.findById(bookingId);
		if (booking.isPresent()) {
			if(!booking.get().getVenueOwnerEmail().equals(ownerEmail)) {
				throw new UnauthorizedException("You are not authorized to accept or reject this booking request");
			}
			if(!booking.get().getSlot().getSlotStatus().equals(Status.PENDING_FOR_APPROVAL)) {
				throw new ApprovalNotRequiredException("Booking is not pending for approval");
			}
			if (approval.equals(Approval.ACCEPT)) {
				booking.get().getSlot().setSlotStatus(Status.PENDING_PAYMENT);

				return bookingrepo.save(booking.get());
			} else {
				booking.get().getSlot().setSlotStatus(Status.REJECTED_BY_VENUEOWNER);

				return bookingrepo.save(booking.get());
			}
		} else
			throw new BookingNotFoundException("Booking not found in the system");
	}

	@Override
	public List<Booking> getAllBookingsForVenueOwner(String venueOwnerEmail) {
		List<Booking> bookings = bookingrepo.getBookingsForVenueOwner(venueOwnerEmail);
		if (bookings == null || bookings.isEmpty()) {
			throw new BookingNotFoundException("There are no bookings to show");
		}
		return bookings;
	}

	@Override
	public List<Booking> getAllBookingsForVenueOwnerByStatus(String venueOwnerEmail, Status slotStatus) {

		List<Booking> filterByStatus = bookingrepo.getBookingForVenueOwnerByStatus(venueOwnerEmail, slotStatus.name());
		if (filterByStatus.isEmpty()) {
			throw new BookingNotFoundException("There are no bookings to show");
		}
		return filterByStatus;
	}

	@Override
	public List<Booking> getAllBookingsForUser(String userEmail) {
		List<Booking> bookings = bookingrepo.getBookingsForUser(userEmail);
		if (bookings == null || bookings.isEmpty()) {
			throw new BookingNotFoundException("There are no bookings to show");
		}
		return bookings;
	}

	@Override
	public List<Booking> getAllBookingsForUserByStatus(String userEmail, Status slotStatus) {

		List<Booking> filterByStatus = bookingrepo.getBookingForUserByStatus(userEmail, slotStatus.name());
		if (filterByStatus.isEmpty()) {
			throw new BookingNotFoundException("There are no bookings to show");
		}
		return filterByStatus;
	}

	public int getNextSequence(String seqName) {
		DatabaseSequence counter = mongo.findAndModify(query(where("_id").is(seqName)), new Update().inc("seq", 1),
				options().returnNew(true).upsert(true), DatabaseSequence.class);
		return counter.getSeq();
	}

	@Override
	public List<Booking> getAllBookingsForUserByVenueIdAndStatus(String userEmail, int venueId, Status slotStatus) {
		List<Booking> filteredVenues = bookingrepo.getBookingForUserByVenueIdAndStatus(userEmail,venueId, slotStatus.name());
		if (filteredVenues.isEmpty()) {
			throw new BookingNotFoundException("There are no bookings to show");
		}
		return filteredVenues;
	}

	@Override
	public List<Booking> getAllBookingsForUserByVenueId(String userEmail, int venueId) {
		List<Booking> filteredVenues = bookingrepo.getBookingForUserByVenueId(userEmail,venueId);
		if (filteredVenues.isEmpty()) {
			throw new BookingNotFoundException("There are no bookings to show");
		}
		return filteredVenues;
	}

	@Override
	public List<Booking> getAllBookingsForVenueOwnerByVenueIdAndStatus(String venueOwnerEmail, int venueId,
			Status slotStatus) {
		List<Booking> filteredVenues = bookingrepo.getBookingForVenueOwnerByVenueIdAndStatus(venueOwnerEmail,venueId,slotStatus.name());
		if (filteredVenues.isEmpty()) {
			throw new BookingNotFoundException("There are no bookings to show");
		}
		return filteredVenues;
	}

	@Override
	public List<Booking> getAllBookingsForVenueOwnerByVenueId(String venueOwnerEmail, int venueId) {
		List<Booking> filteredVenues = bookingrepo.getBookingForVenueOwnerByVenueId(venueOwnerEmail,venueId);
		if (filteredVenues.isEmpty()) {
			throw new BookingNotFoundException("There are no bookings to show");
		}
		return filteredVenues;
	}

}
