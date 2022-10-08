package com.stackroute.booking.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

@ContextConfiguration(classes = { BookingServiceImpl.class })
@ExtendWith(SpringExtension.class)
class BookingServiceImplTest {
	@MockBean
	private BookingRepository bookingRepository;

	@MockBean
	private MongoOperations mongoOperations;

	@MockBean
	private VenueRepository venueRepository;

	@MockBean
	private Booking booking;

	@Autowired
	private BookingServiceImpl bookingServiceImpl;

	List<Slot> slots;

	@MockBean
	private Venue venue;

	@BeforeEach
	public void setup() {
		booking = new Booking();
		booking.setBookingDate(LocalDate.ofEpochDay(1L));
		booking.setBookingId(123);
		booking.setCurrency("USD");
		booking.setSlotId(123);
		booking.setSlot(new Slot(123, Status.PENDING_PAYMENT, "Morning"));
		booking.setUserEmail("manish@gmail.com");
		booking.setVenueId(123);
		booking.setVenuePrice(10.0d);

		venue = new Venue();
		slots = new ArrayList<>();
		slots.add(new Slot(0, Status.PENDING_PAYMENT, "Morning"));
		slots.add(new Slot(123, Status.PENDING_PAYMENT, "Morning"));
		venue.setVenueId(123);
		venue.setSlots(slots);
	}

	@Test
	void testSlotBookingDateBeforeCurrentDateException() {

		assertThrows(DateBeforeCurrentDateException.class, () -> bookingServiceImpl.slotBooking(booking));
	}

	@Test
	void testSlotBookingInvalidBookingRequestException() {
		booking.setSlotId(0);
		Optional<Venue> ofResult = Optional.of(venue);
		when(venueRepository.findById((Integer) any())).thenReturn(ofResult);

		ArrayList<Booking> bookingList = new ArrayList<>();
		booking.setBookingDate(LocalDate.now().plusDays(2));
		bookingList.add(booking);

		when(bookingRepository.getBookingForUserByVenueStatusAndBookingDate(booking.getVenueId(),
				booking.getBookingDate(), booking.getSlotId(), booking.getUserEmail(),
				Status.PENDING_FOR_APPROVAL.name())).thenReturn(bookingList);
		DatabaseSequence databaseSequence = new DatabaseSequence();
		databaseSequence.setId("42");
		databaseSequence.setSeq(1);
		when(mongoOperations.findAndModify((Query) any(), (UpdateDefinition) any(), (FindAndModifyOptions) any(),
				(Class<DatabaseSequence>) any())).thenReturn(databaseSequence);

		assertThrows(InvalidBookingRequestException.class,
				() -> bookingServiceImpl.slotBooking(booking).getSlot().getSlotStatus());
	}

	@Test
	void testSlotBookingSuccess() {
		booking.setSlotId(0);
		Optional<Venue> ofResult = Optional.of(venue);
		when(venueRepository.findById((Integer) any())).thenReturn(ofResult);

		ArrayList<Booking> bookingList = new ArrayList<>();
		booking.setBookingDate(LocalDate.now().plusDays(2));

		when(bookingRepository.findAll()).thenReturn(bookingList);
		when(bookingRepository.save((Booking) any())).thenReturn(booking);
		DatabaseSequence databaseSequence = new DatabaseSequence();
		databaseSequence.setId("43");
		databaseSequence.setSeq(2);
		when(mongoOperations.findAndModify((Query) any(), (UpdateDefinition) any(), (FindAndModifyOptions) any(),
				(Class<DatabaseSequence>) any())).thenReturn(databaseSequence);

		assertEquals(Status.PENDING_FOR_APPROVAL, bookingServiceImpl.slotBooking(booking).getSlot().getSlotStatus());
	}

	@Test
	void testSlotBookingVenueAlreadyBookedException() {
		booking.setSlot(new Slot(123, Status.BOOKED, "Morning"));
		booking.setBookingDate(LocalDate.now().plusDays(1));
		ArrayList<Booking> bookingList = new ArrayList<>();
		bookingList.add(booking);
		when(bookingRepository.getBookingByVenueStatusAndBookingDate(123, booking.getBookingDate(), booking.getSlotId(),
				Status.BOOKED.name())).thenReturn(bookingList);
		Optional<Venue> ofResult = Optional.of(venue);
		when(venueRepository.findById((Integer) any())).thenReturn(ofResult);
		assertThrows(VenueAlreadyBookedException.class, () -> bookingServiceImpl.slotBooking(booking));
	}

	@Test
	void testSlotBookingSlotNotFoundException() {
		booking.setSlotId(4);
		booking.setSlot(new Slot(123, Status.BOOKED, "Morning"));
		booking.setBookingDate(LocalDate.now().plusDays(1));
		ArrayList<Booking> bookingList = new ArrayList<>();
		bookingList.add(booking);
		when(bookingRepository.findAll()).thenReturn(bookingList);
		Optional<Venue> ofResult = Optional.of(venue);
		when(venueRepository.findById((Integer) any())).thenReturn(ofResult);
		assertThrows(SlotNotFoundException.class, () -> bookingServiceImpl.slotBooking(booking));
	}

	@Test
	void testCancelBookingSuccess() {
		booking.setSlot(new Slot(123, Status.BOOKED, "Morning"));
		booking.setBookingDate(LocalDate.now().plusDays(2));
		Optional<Booking> ofResult = Optional.of(booking);
		when(bookingRepository.findById((Integer) any())).thenReturn(ofResult);
		Booking cancelBooking = bookingServiceImpl.cancelBooking(123, booking.getUserEmail());
		assertEquals(Status.CANCELLED, cancelBooking.getSlot().getSlotStatus());

	}

	@Test
	void testCancelBookingSuccessOneDayBefore() {
		booking.setSlot(new Slot(123, Status.BOOKED, "Morning"));
		Optional<Booking> ofResult = Optional.of(booking);
		when(bookingRepository.findById((Integer) any())).thenReturn(ofResult);
		assertThrows(NotEligibleForRefundException.class,
				() -> bookingServiceImpl.cancelBooking(123, booking.getUserEmail()));
	}

	@Test
	void testCancelBookingVenueNotBookedException() {
		Optional<Booking> ofResult = Optional.of(booking);
		when(bookingRepository.findById((Integer) any())).thenReturn(ofResult);
		assertThrows(VenueNotBookedException.class,
				() -> bookingServiceImpl.cancelBooking(123, booking.getUserEmail()));
	}

	@Test
	void testCancelUnauthorizedException() {
		Optional<Booking> ofResult = Optional.of(booking);
		when(bookingRepository.findById((Integer) any())).thenReturn(ofResult);
		assertThrows(UnauthorizedException.class, () -> bookingServiceImpl.cancelBooking(123, "owner@gmail.com"));
	}

	@Test
	void testCancelBookingBookingNotFoundException() {
		when(bookingRepository.findById((Integer) any())).thenReturn(Optional.empty());
		assertThrows(BookingNotFoundException.class,
				() -> bookingServiceImpl.cancelBooking(123, booking.getUserEmail()));
	}

	@Test
	void testGetBookingByIdSuccess() {
		Optional<Booking> ofResult = Optional.of(booking);
		when(bookingRepository.findById((Integer) any())).thenReturn(ofResult);
		assertSame(booking, bookingServiceImpl.getBookingById(123));
		verify(bookingRepository).findById((Integer) any());
	}

	@Test
	void testGetBookingByIdBookingNotFoundException() {
		when(bookingRepository.findById((Integer) any())).thenReturn(Optional.empty());
		assertThrows(BookingNotFoundException.class, () -> bookingServiceImpl.getBookingById(123));
		verify(bookingRepository).findById((Integer) any());
	}

	@Test
	void testCheckAvailabilitySuccess() {
		Optional<Venue> venueo = Optional.of(venue);
		when(venueRepository.findById(123)).thenReturn(venueo);
		booking.setBookingDate(LocalDate.now().plusDays(2));
		when(bookingRepository.getBookingByVenueStatusAndBookingDate(123, booking.getBookingDate(), booking.getSlotId(),
				Status.BOOKED.name())).thenReturn(new ArrayList<>());
		assertTrue(bookingServiceImpl.checkAvailability(booking.getVenueId(), booking.getSlotId(),
				booking.getBookingDate()));
		verify(bookingRepository).getBookingByVenueStatusAndBookingDate(123, booking.getBookingDate(),
				booking.getSlotId(), Status.BOOKED.name());
	}

	@Test
	void testCheckAvailabilityVenueNotFound() {
		booking.setBookingDate(LocalDate.now().plusDays(2));
		when(venueRepository.findById(123)).thenReturn(Optional.empty());
		assertThrows(VenueNotFoundException.class, () -> bookingServiceImpl.checkAvailability(booking.getVenueId(),
				booking.getSlotId(), booking.getBookingDate()));
	}

	@Test
	void testCheckAvailabilityDateBeforeCurrentDateException() {
		when(venueRepository.findById(123)).thenReturn(Optional.empty());
		assertThrows(DateBeforeCurrentDateException.class, () -> bookingServiceImpl
				.checkAvailability(booking.getVenueId(), booking.getSlotId(), booking.getBookingDate()));
	}

	@Test
	void testCheckAvailabilitySlotNotFoundException() {
		Optional<Venue> venueo = Optional.of(venue);
		when(venueRepository.findById(123)).thenReturn(venueo);
		booking.setSlotId(654);
		booking.setBookingDate(LocalDate.now().plusDays(2));
		booking.setSlot(new Slot(654, Status.PENDING_PAYMENT, "Morning"));
		assertThrows(SlotNotFoundException.class, () -> bookingServiceImpl.checkAvailability(booking.getVenueId(),
				booking.getSlotId(), booking.getBookingDate()));

	}

	@Test
	void testCheckAvailabilityNotAvailable() {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.now().plusDays(2));
		booking1.setBookingId(123);
		booking1.setCurrency("GBP");
		booking1.setSlot(new Slot(123, Status.BOOKED, "Evening"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);

		ArrayList<Booking> bookingList = new ArrayList<>();
		bookingList.add(booking1);
		booking.setBookingDate(LocalDate.now().plusDays(2));
		bookingList.add(booking);
		Optional<Venue> venueo = Optional.of(venue);
		when(venueRepository.findById(123)).thenReturn(venueo);
		when(bookingRepository.getBookingByVenueStatusAndBookingDate(123, booking.getBookingDate(), booking.getSlotId(),
				Status.BOOKED.name())).thenReturn(bookingList);

		assertFalse(bookingServiceImpl.checkAvailability(booking1.getVenueId(), booking1.getSlotId(),
				booking1.getBookingDate()));
	}

	@Test
	void testGetAllVenuesForVenueOwner() {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("GBP");
		booking1.setSlot(new Slot(123, Status.BOOKED, "Evening"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		booking.setVenueOwnerEmail("owner@gmail.com");

		ArrayList<Booking> bookingList = new ArrayList<>();
		bookingList.add(booking1);
		bookingList.add(booking);
		when(bookingRepository.getBookingsForVenueOwner("owner@gmail.com")).thenReturn(bookingList);
		assertEquals(bookingList, bookingServiceImpl.getAllBookingsForVenueOwner("owner@gmail.com"));

	}

	@Test
	void testGetAllVenuesForVenueOwnerBookingNotFoundException() {
		when(bookingRepository.getBookingsForVenueOwner("owner@gmail.com")).thenReturn(new ArrayList<>());
		assertThrows(BookingNotFoundException.class,
				() -> bookingServiceImpl.getAllBookingsForVenueOwner("owner1@gmail.com"));

	}

	@Test
	void testGetAllBookingsForVenueOwnerByStatus() {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("GBP");
		booking1.setSlot(new Slot(123, Status.BOOKED, "Evening"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		booking.setVenueOwnerEmail("owner@gmail.com");

		ArrayList<Booking> bookingList = new ArrayList<>();
		bookingList.add(booking1);
		bookingList.add(booking);
		when(bookingRepository.getBookingForVenueOwnerByStatus("owner@gmail.com", Status.BOOKED.name()))
				.thenReturn(bookingList);
		assertEquals(booking1,
				bookingServiceImpl.getAllBookingsForVenueOwnerByStatus("owner@gmail.com", Status.BOOKED).get(0));

	}

	@Test
	void testGetAllBookingsForVenueOwnerByStatusBookingNotFoundException() {
		when(bookingRepository.getBookingsForVenueOwner("owner@gmail.com")).thenReturn(new ArrayList<>());
		assertThrows(BookingNotFoundException.class,
				() -> bookingServiceImpl.getAllBookingsForVenueOwnerByStatus("owner1@gmail.com", Status.BOOKED));

	}

	@Test
	void testGetAllBookingsForVenueOwnerByVenueIdSuccess() {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("GBP");
		booking1.setSlot(new Slot(123, Status.BOOKED, "Evening"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		booking.setVenueOwnerEmail("owner@gmail.com");

		ArrayList<Booking> bookingList = new ArrayList<>();
		bookingList.add(booking1);
		bookingList.add(booking);
		when(bookingRepository.getBookingForVenueOwnerByVenueId("owner@gmail.com", 123)).thenReturn(bookingList);
		assertEquals(booking1, bookingServiceImpl.getAllBookingsForVenueOwnerByVenueId("owner@gmail.com", 123).get(0));

	}

	@Test
	void testGetAllBookingsForVenueOwnerByVenueIdBookingNotFoundException() {
		when(bookingRepository.getBookingForVenueOwnerByVenueId("owner@gmail.com", 123)).thenReturn(new ArrayList<>());
		assertThrows(BookingNotFoundException.class,
				() -> bookingServiceImpl.getAllBookingsForVenueOwnerByVenueId("owner1@gmail.com", 123));

	}

	@Test
	void testGetAllBookingsForVenueOwnerByVenueIdAndStatusSuccess() {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("GBP");
		booking1.setSlot(new Slot(123, Status.BOOKED, "Evening"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		booking.setVenueOwnerEmail("owner@gmail.com");

		ArrayList<Booking> bookingList = new ArrayList<>();
		bookingList.add(booking1);
		bookingList.add(booking);
		when(bookingRepository.getBookingForVenueOwnerByVenueIdAndStatus("owner@gmail.com", 123,
				Status.PENDING_PAYMENT.name())).thenReturn(bookingList);
		assertEquals(booking1, bookingServiceImpl
				.getAllBookingsForVenueOwnerByVenueIdAndStatus("owner@gmail.com", 123, Status.PENDING_PAYMENT).get(0));

	}

	@Test
	void testGetAllBookingsForVenueOwnerByVenueIdAndStatusBookingNotFoundException() {
		when(bookingRepository.getBookingForVenueOwnerByVenueIdAndStatus("owner@gmail.com", 123, Status.BOOKED.name()))
				.thenReturn(new ArrayList<>());
		assertThrows(BookingNotFoundException.class, () -> bookingServiceImpl
				.getAllBookingsForVenueOwnerByVenueIdAndStatus("owner1@gmail.com", 123, Status.BOOKED));

	}

	@Test
	void testGetAllVenuesForUser() {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("GBP");
		booking1.setSlot(new Slot(123, Status.BOOKED, "Evening"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		booking.setVenueOwnerEmail("owner@gmail.com");

		ArrayList<Booking> bookingList = new ArrayList<>();
		bookingList.add(booking1);
		bookingList.add(booking);
		when(bookingRepository.getBookingsForUser("manish@gmail.com")).thenReturn(bookingList);
		assertEquals(bookingList, bookingServiceImpl.getAllBookingsForUser("manish@gmail.com"));

	}

	@Test
	void testGetAllVenuesForUserBookingNotFoundException() {
		when(bookingRepository.getBookingsForUser("manish@gmail.com")).thenReturn(new ArrayList<>());
		assertThrows(BookingNotFoundException.class,
				() -> bookingServiceImpl.getAllBookingsForUser("manish@gmail.com"));

	}

	@Test
	void testGetAllBookingsForUserByStatus() {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("GBP");
		booking1.setSlot(new Slot(123, Status.BOOKED, "Evening"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		booking.setVenueOwnerEmail("owner@gmail.com");

		ArrayList<Booking> bookingList = new ArrayList<>();
		bookingList.add(booking1);
		bookingList.add(booking);
		when(bookingRepository.getBookingForUserByStatus("manish@gmail.com", Status.BOOKED.name()))
				.thenReturn(bookingList);
		assertEquals(booking1,
				bookingServiceImpl.getAllBookingsForUserByStatus("manish@gmail.com", Status.BOOKED).get(0));

	}

	@Test
	void testGetAllBookingsForUserByStatusBookingNotFoundException() {
		when(bookingRepository.getBookingsForUser("manish@gmail.com")).thenReturn(new ArrayList<>());
		assertThrows(BookingNotFoundException.class,
				() -> bookingServiceImpl.getAllBookingsForUserByStatus("manish1@gmail.com", Status.BOOKED));

	}

	@Test
	void testGetAllBookingsForUserByVenueIdSuccess() {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("GBP");
		booking1.setSlot(new Slot(123, Status.BOOKED, "Evening"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		booking.setVenueOwnerEmail("owner@gmail.com");

		ArrayList<Booking> bookingList = new ArrayList<>();
		bookingList.add(booking1);
		bookingList.add(booking);
		when(bookingRepository.getBookingForUserByVenueId("manish@gmail.com", 123)).thenReturn(bookingList);
		assertEquals(booking1, bookingServiceImpl.getAllBookingsForUserByVenueId("manish@gmail.com", 123).get(0));

	}

	@Test
	void testGetAllBookingsForUserByVenueIdBookingNotFoundException() {
		when(bookingRepository.getBookingForUserByVenueId("manish@gmail.com", 123)).thenReturn(new ArrayList<>());
		assertThrows(BookingNotFoundException.class,
				() -> bookingServiceImpl.getAllBookingsForUserByVenueId("manish1@gmail.com", 123));

	}

	@Test
	void testGetAllBookingsForUserByVenueIdAndStatusSuccess() {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("GBP");
		booking1.setSlot(new Slot(123, Status.BOOKED, "Evening"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		booking.setVenueOwnerEmail("owner@gmail.com");

		ArrayList<Booking> bookingList = new ArrayList<>();
		bookingList.add(booking1);
		bookingList.add(booking);
		when(bookingRepository.getBookingForUserByVenueIdAndStatus("manish@gmail.com", 123,
				Status.PENDING_PAYMENT.name())).thenReturn(bookingList);
		assertEquals(booking1, bookingServiceImpl
				.getAllBookingsForUserByVenueIdAndStatus("manish@gmail.com", 123, Status.PENDING_PAYMENT).get(0));

	}

	@Test
	void testGetAllBookingsForUserByVenueIdAndStatusBookingNotFoundException() {
		when(bookingRepository.getBookingForUserByVenueIdAndStatus("manish@gmail.com", 123, Status.BOOKED.name()))
				.thenReturn(new ArrayList<>());
		assertThrows(BookingNotFoundException.class, () -> bookingServiceImpl
				.getAllBookingsForUserByVenueIdAndStatus("manish1@gmail.com", 123, Status.BOOKED));

	}

	@Test
	void testBookingApprovalBookingNotFoundException() {
		when(bookingRepository.findById(123)).thenReturn(Optional.empty());

		assertThrows(BookingNotFoundException.class,
				() -> bookingServiceImpl.bookingApproval(Approval.ACCEPT, 12, "owner@gmail.com"));
	}

	@Test
	void testBookingApprovalApprovalNotRequiredException() {
		booking.setVenueOwnerEmail("owner@gmail.com");
		Optional<Booking> bookingo = Optional.of(booking);
		when(bookingRepository.findById(123)).thenReturn(bookingo);

		assertThrows(ApprovalNotRequiredException.class,
				() -> bookingServiceImpl.bookingApproval(Approval.ACCEPT, 123, "owner@gmail.com"));
	}

	@Test
	void testBookingApprovalUnauthorizedException() {
		booking.setVenueOwnerEmail("owne@gmail.com");
		Optional<Booking> bookingo = Optional.of(booking);
		when(bookingRepository.findById(123)).thenReturn(bookingo);

		assertThrows(UnauthorizedException.class,
				() -> bookingServiceImpl.bookingApproval(Approval.ACCEPT, 123, "owner@gmail.com"));
	}

	@Test
	void testBookingApprovalACCEPT() {
		booking.setSlot(new Slot(123, Status.PENDING_FOR_APPROVAL, "Evening"));
		booking.setVenueOwnerEmail("owner@gmail.com");
		Optional<Booking> bookingo = Optional.of(booking);
		when(bookingRepository.findById(123)).thenReturn(bookingo);
		when(bookingRepository.save(booking)).thenReturn(booking);
		assertEquals(Status.PENDING_PAYMENT,
				bookingServiceImpl.bookingApproval(Approval.ACCEPT, 123, "owner@gmail.com").getSlot().getSlotStatus());
	}

	@Test
	void testBookingApprovalREJECT() {
		booking.setSlot(new Slot(123, Status.PENDING_FOR_APPROVAL, "Evening"));
		booking.setVenueOwnerEmail("owner@gmail.com");
		Optional<Booking> bookingo = Optional.of(booking);
		when(bookingRepository.findById(123)).thenReturn(bookingo);
		when(bookingRepository.save(booking)).thenReturn(booking);
		assertEquals(Status.REJECTED_BY_VENUEOWNER,
				bookingServiceImpl.bookingApproval(Approval.REJECT, 123, "owner@gmail.com").getSlot().getSlotStatus());
	}

}
