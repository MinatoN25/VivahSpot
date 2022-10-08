package com.stackroute.booking.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.stackroute.booking.model.Slot;
import com.stackroute.booking.model.Status;
import com.stackroute.booking.service.BookingService;

@RunWith(SpringRunner.class)
@WebMvcTest(BookingController.class)
class BookingControllerTest {

	@Autowired
	private MockMvc mock;

	@MockBean
	private BookingService bookingService;

	@MockBean
	private RabbitTemplate rabbitTemplate;

	@InjectMocks
	private BookingController bookingController;

	private Booking booking;
	private String userToken;
	private String ownerToken;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		booking = new Booking();
		booking.setBookingDate(LocalDate.ofEpochDay(1L));
		booking.setBookingId(123);
		booking.setCurrency("USD");
		booking.setSlot(new Slot(123, Status.PENDING_FOR_APPROVAL, "Morning"));
		booking.setSlotId(123);
		booking.setUserEmail("manish@gmail.com");
		booking.setVenueId(123);
		booking.setVenuePrice(10.0d);
		booking.setVenueOwnerEmail("owner@gmail.com");
		userToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5pc2hAZ21haWwuY29tIiwiaWF0IjoxNjYzNDEwOTYyfQ.jkDylYu-Rf4oEMg39t_aYYqWZ2wQG5aVvwshR3fFoI05sZB_XKbfMqV_JMbVC1UOlpfJOE0l24Qep14uuu1ErQ";
		ownerToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJvd25lckBnbWFpbC5jb20iLCJpYXQiOjE2NjM2NTc5Mjh9.3MA7HsCAe58c75_bbfjaUr00xakryonpjepcPUE3b-CMLTV9HSqRgtQzNbcj6dbaMyqBm2RG578VL_FxdH9Q8g";
	}

	@Test
	void testSlotBooking() throws Exception {

		when(bookingService.slotBooking((Booking) any())).thenReturn(booking);

		mock.perform(post("/api/v1/bookVenue?venueId=123&slotId=123&bookingDate=1970-01-02")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"bookingId\":123,\"venueId\":123,\"venueName\":null,\"userEmail\":\"manish@gmail.com\",\"currency\":\"USD\",\"venuePrice\":10.0,\"bookingDate\":\"1970-01-02\",\"slotId\":123,\"slot\":{\"slotId\":123,\"slotStatus\":\"PENDING_FOR_APPROVAL\",\"slotTime\":\"Morning\"},\"venueOwnerEmail\":\"owner@gmail.com\",\"refundAmount\":0.0}"))
				.andDo(print());

	}

	@Test
	void testSlotBookingDateNull() throws Exception {
		when(bookingService.slotBooking((Booking) any())).thenReturn(booking);
		booking.setBookingDate(null);
		mock.perform(post("/api/v1/bookVenue?venueId=123&slotId=123&bookingDate=")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(MockMvcResultMatchers.content()
						.string("Required LocalDate parameter 'bookingDate' is not present"))
				.andDo(print());
	}

	@Test
	void testSlotBookingVenueIdNull() throws Exception {
		when(bookingService.slotBooking((Booking) any())).thenReturn(booking);
		booking.setBookingDate(null);
		mock.perform(post("/api/v1/bookVenue?venueId=&slotId=123&bookingDate=1970-01-02")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(
						MockMvcResultMatchers.content().string("Required Integer parameter 'venueId' is not present"))
				.andDo(print());
	}

	@Test
	void testSlotBookingSlotIdNull() throws Exception {
		when(bookingService.slotBooking((Booking) any())).thenReturn(booking);
		booking.setBookingDate(null);
		mock.perform(post("/api/v1/bookVenue?venueId=123&slotId=&bookingDate=1970-01-02")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string("Required Integer parameter 'slotId' is not present"))
				.andDo(print());
	}

	@Test
	void testSlotBookingDateBeforeCurrentDateException() throws Exception {
		when(bookingService.slotBooking((Booking) any()))
				.thenThrow(new DateBeforeCurrentDateException("Please enter date after current date"));

		String content = (new ObjectMapper()).writeValueAsString(booking);
		mock.perform(post("/api/v1/bookVenue?venueId=123&slotId=123&bookingDate=1970-01-02")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.content().string("Please enter date after current date"))
				.andDo(print());
	}

	@Test
	void testSlotBookingVenueAlreadyBookedException() throws Exception {
		when(bookingService.slotBooking((Booking) any()))
				.thenThrow(new VenueAlreadyBookedException("Venue is already booked"));
		mock.perform(post("/api/v1/bookVenue?venueId=123&slotId=123&bookingDate=1970-01-02")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.content().string("Venue is already booked")).andDo(print());
	}

	@Test
	void testSlotBookingSlotNotFoundException() throws Exception {
		when(bookingService.slotBooking((Booking) any()))
				.thenThrow(new SlotNotFoundException("Slot not found for the venue"));
		mock.perform(post("/api/v1/bookVenue?venueId=123&slotId=2&bookingDate=1970-01-02")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.content().string("Slot not found for the venue")).andDo(print());
	}

	@Test
	void testSlotBookingInvalidBookingRequestException() throws Exception {
		when(bookingService.slotBooking((Booking) any()))
				.thenThrow(new InvalidBookingRequestException("Not eligible for booking"));
		mock.perform(post("/api/v1/bookVenue?venueId=123&slotId=123&bookingDate=1970-01-02")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.content().string("Not eligible for booking")).andDo(print());
	}

	@Test
	void testSlotBookingVenueNotFoundException() throws Exception {
		when(bookingService.slotBooking((Booking) any())).thenThrow(new VenueNotFoundException("Venue not found"));
		mock.perform(post("/api/v1/bookVenue?venueId=1233&slotId=123&bookingDate=1970-01-02")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict()).andExpect(MockMvcResultMatchers.content().string("Venue not found"))
				.andDo(print());
	}

	@Test
	void testCancelBookingSuccess() throws Exception {
		booking.setSlot(new Slot(123, Status.CANCELLED, "Morning"));
		when(bookingService.cancelBooking(anyInt(), (String) any())).thenReturn(booking);
		mock.perform(put("/api/v1/cancelBooking/{bookingId}", 123)
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(
						"Your booking has been cancelled and USD 0.0 will be refunded within 5 working days. \nPlease contact support team in case of any queries"))
				.andDo(print());

	}

	@Test
	void testCancelBookingSuccessOneDayBefore() throws Exception {
		booking.setSlot(new Slot(123, Status.CANCELLED, "Morning"));
		when(bookingService.cancelBooking(anyInt(), (String) any()))
				.thenThrow(new NotEligibleForRefundException("No refund"));
		mock.perform(put("/api/v1/cancelBooking/{bookingId}", 123).header("Authorization", "Bearer " + userToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.content().string("No refund")).andDo(print());
	}

	@Test
	void testCancelBookingUnauthorizedException() throws Exception {
		booking.setSlot(new Slot(123, Status.BOOKED, "Morning"));
		when(bookingService.cancelBooking(anyInt(), (String) any()))
				.thenThrow(new UnauthorizedException("Unautorized"));
		mock.perform(put("/api/v1/cancelBooking/{bookingId}", 123).header("Authorization", "Bearer " + ownerToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized())
				.andExpect(MockMvcResultMatchers.content().string("Unautorized")).andDo(print());
	}

	@Test
	void testCancelBookingVenueNotBookedException() throws Exception {
		booking.setSlot(new Slot(123, Status.BOOKED, "Morning"));
		when(bookingService.cancelBooking(anyInt(), (String) any()))
				.thenThrow(new VenueNotBookedException("Venue is not booked"));
		mock.perform(put("/api/v1/cancelBooking/{bookingId}", 123).header("Authorization", "Bearer " + userToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.content().string("Venue is not booked")).andDo(print());
	}

	@Test
	void testCancelBookingBookingNotFoundException() throws Exception {
		when(bookingService.cancelBooking(anyInt(), (String) any()))
				.thenThrow(new BookingNotFoundException("Booking not found"));
		mock.perform(put("/api/v1/cancelBooking/{bookingId}", 123).header("Authorization", "Bearer " + userToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.content().string("Booking not found")).andDo(print());
	}

	@Test
	void testGetBookingByIdSuccess() throws Exception {
		when(bookingService.getBookingById(anyInt())).thenReturn(booking);
		mock.perform(get("/api/v1/getBooking/{bookingId}", 123)
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"bookingId\":123,\"venueId\":123,\"venueName\":null,\"userEmail\":\"manish@gmail.com\",\"currency\":\"USD\",\"venuePrice\":10.0,\"bookingDate\":\"1970-01-02\",\"slotId\":123,\"slot\":{\"slotId\":123,\"slotStatus\":\"PENDING_FOR_APPROVAL\",\"slotTime\":\"Morning\"},\"venueOwnerEmail\":\"owner@gmail.com\",\"refundAmount\":0.0}"))
				.andDo(print());
	}

	@Test
	void testGetBookingByIdBookingNotFoundException() throws Exception {
		when(bookingService.getBookingById(anyInt())).thenThrow(new BookingNotFoundException("Booking not found"));
		mock.perform(get("/api/v1/getBooking/{bookingId}", 123).header("Authorization", "Bearer " + userToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.content().string("Booking not found")).andDo(print());
	}

	@Test
	void testCheckIfAvailableSuccess() throws Exception {
		when(bookingService.checkAvailability((Integer) anyInt(), (Integer) anyInt(), (LocalDate) any()))
				.thenReturn(true);
		String content = (new ObjectMapper()).writeValueAsString(booking);
		mock.perform(get("/api/v1/isAvailable?venueId=123&slotId=565&bookingDate=1970-01-01")
				.header("Authorization", "Bearer " + userToken).content(content)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Venue and slot is available for the date entered"))
				.andDo(print());

	}

	@Test
	void testCheckIfAvailableDateBeforeCurrentDateException() throws Exception {
		when(bookingService.checkAvailability((Integer) anyInt(), (Integer) anyInt(), (LocalDate) any()))
				.thenThrow(new DateBeforeCurrentDateException("Please enter date before current date"));
		mock.perform(get("/api/v1/isAvailable?venueId=123&slotId=565&bookingDate=1970-01-01")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.content().string("Please enter date before current date"))
				.andDo(print());

	}

	@Test
	void testCheckIfAvailableVenueNotFoundException() throws Exception {
		when(bookingService.checkAvailability((Integer) anyInt(), (Integer) anyInt(), (LocalDate) any()))
				.thenThrow(new VenueNotFoundException("Venue not found"));
		String content = (new ObjectMapper()).writeValueAsString(booking);
		mock.perform(get("/api/v1/isAvailable?venueId=123&slotId=565&bookingDate=1970-01-01")
				.header("Authorization", "Bearer " + userToken).content(content)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.content().string("Venue not found")).andDo(print());

	}

	@Test
	void testCheckIfAvailableSlotNotFoundException() throws Exception {

		when(bookingService.checkAvailability((Integer) anyInt(), (Integer) anyInt(), (LocalDate) any()))
				.thenThrow(new SlotNotFoundException("Slot not found"));
		String content = (new ObjectMapper()).writeValueAsString(booking);
		mock.perform(get("/api/v1/isAvailable?venueId=123&slotId=565&bookingDate=1970-01-01")
				.header("Authorization", "Bearer " + userToken).content(content)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.content().string("Slot not found")).andDo(print());

	}

	@Test
	void testCheckIfAvailableBooked() throws Exception {
		when(bookingService.checkAvailability((Integer) anyInt(), (Integer) anyInt(), (LocalDate) any()))
				.thenReturn(false);
		String content = (new ObjectMapper()).writeValueAsString(booking);
		mock.perform(get("/api/v1/isAvailable?venueId=123&slotId=565&bookingDate=1970-01-01")
				.header("Authorization", "Bearer " + userToken).content(content)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Venue and slot is booked for the date entered"))
				.andDo(print());
	}

	@Test
	void testBookingApprovalAccept() throws Exception {
		booking.setSlot(new Slot(123, Status.PENDING_PAYMENT, "Morning"));
		when(bookingService.bookingApproval(Approval.ACCEPT, 123, "owner@gmail.com")).thenReturn(booking);
		mock.perform(put("/api/v1/bookingApproval/{bookingId}/{approval}", 123, Approval.ACCEPT)
				.header("Authorization", "Bearer " + ownerToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted()).andExpect(MockMvcResultMatchers.content().string(
						"{\"bookingId\":123,\"venueId\":123,\"venueName\":null,\"userEmail\":\"manish@gmail.com\",\"currency\":\"USD\",\"venuePrice\":10.0,\"bookingDate\":\"1970-01-02\",\"slotId\":123,\"slot\":{\"slotId\":123,\"slotStatus\":\"PENDING_PAYMENT\",\"slotTime\":\"Morning\"},\"venueOwnerEmail\":\"owner@gmail.com\",\"refundAmount\":0.0}"));
	}

	@Test
	void testBookingApprovalUnauthorized() throws Exception {
		booking.setSlot(new Slot(123, Status.PENDING_PAYMENT, "Morning"));
		when(bookingService.bookingApproval(Approval.ACCEPT, 123, "manish@gmail.com"))
				.thenThrow(new UnauthorizedException("Not authorized"));
		mock.perform(put("/api/v1/bookingApproval/{bookingId}/{approval}", 123, Approval.ACCEPT)
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andExpect(MockMvcResultMatchers.content().string("Not authorized"));
	}

	@Test
	void testBookingApprovalApprovalNotRequiredException() throws Exception {
		booking.setSlot(new Slot(123, Status.PENDING_PAYMENT, "Morning"));
		when(bookingService.bookingApproval(Approval.ACCEPT, 123, "owner@gmail.com"))
				.thenThrow(new ApprovalNotRequiredException("Approval not required"));
		mock.perform(put("/api/v1/bookingApproval/{bookingId}/{approval}", 123, Approval.ACCEPT)
				.header("Authorization", "Bearer " + ownerToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andExpect(MockMvcResultMatchers.content().string("Approval not required"));
	}

	@Test
	void testBookingApprovalReject() throws Exception {
		booking.setSlot(new Slot(123, Status.REJECTED_BY_VENUEOWNER, "Morning"));
		when(bookingService.bookingApproval(Approval.REJECT, 123, "owner@gmail.com")).thenReturn(booking);
		mock.perform(put("/api/v1/bookingApproval/123/REJECT")
				.header("Authorization", "Bearer " + ownerToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"bookingId\":123,\"venueId\":123,\"venueName\":null,\"userEmail\":\"manish@gmail.com\",\"currency\":\"USD\",\"venuePrice\":10.0,\"bookingDate\":\"1970-01-02\",\"slotId\":123,\"slot\":{\"slotId\":123,\"slotStatus\":\"REJECTED_BY_VENUEOWNER\",\"slotTime\":\"Morning\"},\"venueOwnerEmail\":\"owner@gmail.com\",\"refundAmount\":0.0}"))
				.andDo(print());
	}

	@Test
	void testGetAllVenuesByVenueOwnerEmailSuccess() throws Exception {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("USD");
		booking1.setSlot(new Slot(456, Status.PENDING_PAYMENT, "Morning"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		List<Booking> bookings = new ArrayList<>();
		bookings.add(booking);
		bookings.add(booking1);
		when(bookingService.getAllBookingsForVenueOwner("owner@gmail.com")).thenReturn(bookings);
		mock.perform(get("/api/v1/getAllBookingsForOwner")
				.header("Authorization", "Bearer " + ownerToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(
						"[{\"bookingId\":123,\"venueId\":123,\"venueName\":null,\"userEmail\":\"manish@gmail.com\",\"currency\":\"USD\",\"venuePrice\":10.0,\"bookingDate\":\"1970-01-02\",\"slotId\":123,\"slot\":{\"slotId\":123,\"slotStatus\":\"PENDING_FOR_APPROVAL\",\"slotTime\":\"Morning\"},\"venueOwnerEmail\":\"owner@gmail.com\",\"refundAmount\":0.0},{\"bookingId\":123,\"venueId\":123,\"venueName\":null,\"userEmail\":\"manish@gmail.com\",\"currency\":\"USD\",\"venuePrice\":10.0,\"bookingDate\":\"1970-01-02\",\"slotId\":123,\"slot\":{\"slotId\":456,\"slotStatus\":\"PENDING_PAYMENT\",\"slotTime\":\"Morning\"},\"venueOwnerEmail\":\"owner@gmail.com\",\"refundAmount\":0.0}]"))
				.andDo(print());
	}

	@Test
	void testGetAllVenuesByVenueOwnerEmailBookingNotFoundException() throws Exception {

		when(bookingService.getAllBookingsForVenueOwner("owner@gmail.com"))
				.thenThrow(new BookingNotFoundException("Booking not found"));
		mock.perform(get("/api/v1/getAllBookingsForOwner").header("Authorization", "Bearer " + ownerToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.content().string("Booking not found")).andDo(print());
	}

	@Test
	void testGetAllVenuesByVenueOwnerEmailAndStatusSuccess() throws Exception {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("USD");
		booking1.setSlot(new Slot(456, Status.PENDING_PAYMENT, "Morning"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		List<Booking> bookings = new ArrayList<>();
		bookings.add(booking1);
		when(bookingService.getAllBookingsForVenueOwnerByStatus("owner@gmail.com", Status.PENDING_PAYMENT))
				.thenReturn(bookings);
		mock.perform(get("/api/v1/getAllBookingsForOwnerByStatus/PENDING_PAYMENT")
				.header("Authorization", "Bearer " + ownerToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(
						"[{\"bookingId\":123,\"venueId\":123,\"venueName\":null,\"userEmail\":\"manish@gmail.com\",\"currency\":\"USD\",\"venuePrice\":10.0,\"bookingDate\":\"1970-01-02\",\"slotId\":123,\"slot\":{\"slotId\":456,\"slotStatus\":\"PENDING_PAYMENT\",\"slotTime\":\"Morning\"},\"venueOwnerEmail\":\"owner@gmail.com\",\"refundAmount\":0.0}]"))
				.andDo(print());
	}

	@Test
	void testGetAllVenuesByVenueOwnerEmailAndStatusBookingNotFoundException() throws Exception {
		when(bookingService.getAllBookingsForVenueOwnerByStatus("owner@gmail.com", Status.PENDING_PAYMENT))
				.thenThrow(new BookingNotFoundException("Booking not found"));
		mock.perform(get("/api/v1/getAllBookingsForOwnerByStatus/PENDING_PAYMENT")
				.header("Authorization", "Bearer " + ownerToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict()).andExpect(MockMvcResultMatchers.content().string("Booking not found"))
				.andDo(print());
	}

	@Test
	void testGetAllVenuesByVenueOwnerEmailVenueIdAndStatusSuccess() throws Exception {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("USD");
		booking1.setSlot(new Slot(456, Status.PENDING_PAYMENT, "Morning"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		List<Booking> bookings = new ArrayList<>();
		bookings.add(booking1);
		when(bookingService.getAllBookingsForVenueOwnerByVenueIdAndStatus("owner@gmail.com", 123,
				Status.PENDING_PAYMENT)).thenReturn(bookings);
		mock.perform(get("/api/v1/getAllBookingsForOwnerByVenueIdAndStatus/123/PENDING_PAYMENT")
				.header("Authorization", "Bearer " + ownerToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(
						"[{\"bookingId\":123,\"venueId\":123,\"venueName\":null,\"userEmail\":\"manish@gmail.com\",\"currency\":\"USD\",\"venuePrice\":10.0,\"bookingDate\":\"1970-01-02\",\"slotId\":123,\"slot\":{\"slotId\":456,\"slotStatus\":\"PENDING_PAYMENT\",\"slotTime\":\"Morning\"},\"venueOwnerEmail\":\"owner@gmail.com\",\"refundAmount\":0.0}]"))
				.andDo(print());
	}

	@Test
	void testGetAllVenuesByVenueOwnerEmailVenueIdAndStatusBookingNotFoundException() throws Exception {
		when(bookingService.getAllBookingsForVenueOwnerByVenueIdAndStatus("owner@gmail.com", 323,
				Status.PENDING_PAYMENT)).thenThrow(new BookingNotFoundException("Booking not found"));
		mock.perform(get("/api/v1/getAllBookingsForOwnerByVenueIdAndStatus/323/PENDING_PAYMENT")
				.header("Authorization", "Bearer " + ownerToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict()).andExpect(MockMvcResultMatchers.content().string("Booking not found"))
				.andDo(print());
	}

	@Test
	void testGetAllVenuesByVenueOwnerEmailVenueIdSuccess() throws Exception {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("USD");
		booking1.setSlot(new Slot(456, Status.PENDING_PAYMENT, "Morning"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		List<Booking> bookings = new ArrayList<>();
		bookings.add(booking1);
		when(bookingService.getAllBookingsForVenueOwnerByVenueId("owner@gmail.com", 123)).thenReturn(bookings);
		mock.perform(get("/api/v1/getAllBookingsForOwnerByVenueId/123")
				.header("Authorization", "Bearer " + ownerToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(
						"[{\"bookingId\":123,\"venueId\":123,\"venueName\":null,\"userEmail\":\"manish@gmail.com\",\"currency\":\"USD\",\"venuePrice\":10.0,\"bookingDate\":\"1970-01-02\",\"slotId\":123,\"slot\":{\"slotId\":456,\"slotStatus\":\"PENDING_PAYMENT\",\"slotTime\":\"Morning\"},\"venueOwnerEmail\":\"owner@gmail.com\",\"refundAmount\":0.0}]"))
				.andDo(print());
	}

	@Test
	void testGetAllVenuesByVenueOwnerEmailVenueIdBookingNotFoundException() throws Exception {
		when(bookingService.getAllBookingsForVenueOwnerByVenueId("owner@gmail.com", 323))
				.thenThrow(new BookingNotFoundException("Booking not found"));
		mock.perform(get("/api/v1/getAllBookingsForOwnerByVenueId/323").header("Authorization", "Bearer " + ownerToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.content().string("Booking not found")).andDo(print());
	}

	@Test
	void testGetAllVenuesByUserEmailSuccess() throws Exception {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("USD");
		booking1.setSlot(new Slot(456, Status.PENDING_PAYMENT, "Morning"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		List<Booking> bookings = new ArrayList<>();
		bookings.add(booking);
		bookings.add(booking1);
		when(bookingService.getAllBookingsForUser("manish@gmail.com")).thenReturn(bookings);
		mock.perform(get("/api/v1/getAllBookingsForUser")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(
						"[{\"bookingId\":123,\"venueId\":123,\"venueName\":null,\"userEmail\":\"manish@gmail.com\",\"currency\":\"USD\",\"venuePrice\":10.0,\"bookingDate\":\"1970-01-02\",\"slotId\":123,\"slot\":{\"slotId\":123,\"slotStatus\":\"PENDING_FOR_APPROVAL\",\"slotTime\":\"Morning\"},\"venueOwnerEmail\":\"owner@gmail.com\",\"refundAmount\":0.0},{\"bookingId\":123,\"venueId\":123,\"venueName\":null,\"userEmail\":\"manish@gmail.com\",\"currency\":\"USD\",\"venuePrice\":10.0,\"bookingDate\":\"1970-01-02\",\"slotId\":123,\"slot\":{\"slotId\":456,\"slotStatus\":\"PENDING_PAYMENT\",\"slotTime\":\"Morning\"},\"venueOwnerEmail\":\"owner@gmail.com\",\"refundAmount\":0.0}]"))
				.andDo(print());
	}

	@Test
	void testGetAllVenuesByUserEmailBookingNotFoundException() throws Exception {

		when(bookingService.getAllBookingsForUser("manish@gmail.com"))
				.thenThrow(new BookingNotFoundException("Booking not found"));
		mock.perform(get("/api/v1/getAllBookingsForUser").header("Authorization", "Bearer " + userToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.content().string("Booking not found")).andDo(print());
	}

	@Test
	void testGetAllVenuesByUserEmailAndStatusSuccess() throws Exception {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("USD");
		booking1.setSlot(new Slot(456, Status.PENDING_PAYMENT, "Morning"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		List<Booking> bookings = new ArrayList<>();
		bookings.add(booking1);
		when(bookingService.getAllBookingsForUserByStatus("manish@gmail.com", Status.PENDING_PAYMENT))
				.thenReturn(bookings);
		mock.perform(get("/api/v1/getAllBookingsForUserByStatus/PENDING_PAYMENT")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(
						"[{\"bookingId\":123,\"venueId\":123,\"venueName\":null,\"userEmail\":\"manish@gmail.com\",\"currency\":\"USD\",\"venuePrice\":10.0,\"bookingDate\":\"1970-01-02\",\"slotId\":123,\"slot\":{\"slotId\":456,\"slotStatus\":\"PENDING_PAYMENT\",\"slotTime\":\"Morning\"},\"venueOwnerEmail\":\"owner@gmail.com\",\"refundAmount\":0.0}]"))
				.andDo(print());
	}

	@Test
	void testGetAllVenuesByUserEmailAndStatusBookingNotFoundException() throws Exception {
		when(bookingService.getAllBookingsForUserByStatus("manish@gmail.com", Status.PENDING_PAYMENT))
				.thenThrow(new BookingNotFoundException("Booking not found"));
		mock.perform(get("/api/v1/getAllBookingsForUserByStatus/PENDING_PAYMENT")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict()).andExpect(MockMvcResultMatchers.content().string("Booking not found"))
				.andDo(print());
	}

	@Test
	void testGetAllVenuesByUserEmailVenueIdAndStatusSuccess() throws Exception {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("USD");
		booking1.setSlot(new Slot(456, Status.PENDING_PAYMENT, "Morning"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		List<Booking> bookings = new ArrayList<>();
		bookings.add(booking1);
		when(bookingService.getAllBookingsForUserByVenueIdAndStatus("manish@gmail.com", 123, Status.PENDING_PAYMENT))
				.thenReturn(bookings);
		mock.perform(get("/api/v1/getAllBookingsForUserByVenueIdAndStatus/123/PENDING_PAYMENT")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(
						"[{\"bookingId\":123,\"venueId\":123,\"venueName\":null,\"userEmail\":\"manish@gmail.com\",\"currency\":\"USD\",\"venuePrice\":10.0,\"bookingDate\":\"1970-01-02\",\"slotId\":123,\"slot\":{\"slotId\":456,\"slotStatus\":\"PENDING_PAYMENT\",\"slotTime\":\"Morning\"},\"venueOwnerEmail\":\"owner@gmail.com\",\"refundAmount\":0.0}]"))
				.andDo(print());
	}

	@Test
	void testGetAllVenuesByUserEmailVenueIdAndStatusBookingNotFoundException() throws Exception {
		when(bookingService.getAllBookingsForUserByVenueIdAndStatus("manish@gmail.com", 323, Status.PENDING_PAYMENT))
				.thenThrow(new BookingNotFoundException("Booking not found"));
		mock.perform(get("/api/v1/getAllBookingsForUserByVenueIdAndStatus/323/PENDING_PAYMENT")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict()).andExpect(MockMvcResultMatchers.content().string("Booking not found"))
				.andDo(print());
	}

	@Test
	void testGetAllVenuesByUserEmailVenueIdSuccess() throws Exception {
		Booking booking1 = new Booking();
		booking1.setBookingDate(LocalDate.ofEpochDay(1L));
		booking1.setBookingId(123);
		booking1.setCurrency("USD");
		booking1.setSlot(new Slot(456, Status.PENDING_PAYMENT, "Morning"));
		booking1.setSlotId(123);
		booking1.setUserEmail("manish@gmail.com");
		booking1.setVenueId(123);
		booking1.setVenuePrice(10.0d);
		booking1.setVenueOwnerEmail("owner@gmail.com");
		List<Booking> bookings = new ArrayList<>();
		bookings.add(booking1);
		when(bookingService.getAllBookingsForUserByVenueId("manish@gmail.com", 123)).thenReturn(bookings);
		mock.perform(get("/api/v1/getAllBookingsForUserByVenueId/123")
				.header("Authorization", "Bearer " + userToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(
						"[{\"bookingId\":123,\"venueId\":123,\"venueName\":null,\"userEmail\":\"manish@gmail.com\",\"currency\":\"USD\",\"venuePrice\":10.0,\"bookingDate\":\"1970-01-02\",\"slotId\":123,\"slot\":{\"slotId\":456,\"slotStatus\":\"PENDING_PAYMENT\",\"slotTime\":\"Morning\"},\"venueOwnerEmail\":\"owner@gmail.com\",\"refundAmount\":0.0}]"))
				.andDo(print());
	}

	@Test
	void testGetAllVenuesByUserEmailVenueIdBookingNotFoundException() throws Exception {
		when(bookingService.getAllBookingsForUserByVenueId("manish@gmail.com", 323))
				.thenThrow(new BookingNotFoundException("Booking not found"));
		mock.perform(get("/api/v1/getAllBookingsForUserByVenueId/323").header("Authorization", "Bearer " + userToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.content().string("Booking not found")).andDo(print());
	}
}
