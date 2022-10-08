package com.stackroute.payment.repository;

import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.stackroute.payment.model.Booking;
import com.stackroute.payment.model.Slot;
import com.stackroute.payment.model.Status;

@RunWith(SpringRunner.class)
@DataMongoTest
class BookingRepositoryTest {

	@Autowired
	private BookingRepository bookngRepository;

	private Booking booking;

	@BeforeEach
	public void setUp() throws Exception {
		booking = new Booking();
		booking.setBookingId(12341);
		booking.setCurrency("USD");
		booking.setSlotId(1);
		booking.setUserEmail("abc@gmail.com");
		booking.setBookingDate(LocalDate.now().plusDays(2));
		booking.setVenueId(1);
		booking.setVenuePrice(9.00);
		booking.setSlot(new Slot(1, Status.BOOKED, "Morning"));
		booking.setVenueOwnerEmail("owner@gmail.com");

		bookngRepository.deleteById(12341);
		bookngRepository.deleteById(12345);
	}

	@AfterEach
	public void tearDown() throws Exception {

		bookngRepository.deleteById(12341);
		bookngRepository.deleteById(12345);

	}

	@Test
	void saveBookingTest() {
		bookngRepository.insert(booking);
		Booking fetchbooking = bookngRepository.findById(12341).get();
		Assert.assertEquals(booking.getUserEmail(), fetchbooking.getUserEmail());
	}

	@Test
	void findAllBookingTest() {
		bookngRepository.insert(booking);
		booking.setBookingId(12345);
		bookngRepository.insert(booking);
		List<Booking> fetchbooking = bookngRepository.findAll();
		Assert.assertFalse(fetchbooking.isEmpty());
		Assert.assertNotEquals(fetchbooking, null);
	}
	

	@Test
	void updateBookingTest() {
		bookngRepository.insert(booking);
		Booking fetchedbooking = bookngRepository.findById(12341).get();
		fetchedbooking.setCurrency("INR");
		bookngRepository.save(fetchedbooking);
		fetchedbooking = bookngRepository.findById(12341).get();
		Assert.assertEquals("INR", fetchedbooking.getCurrency());
	}

	@Test
	void getBookingByIdTest() {
		bookngRepository.insert(booking);
		Booking fetchedbooking = bookngRepository.findById(12341).get();
		Assert.assertEquals(booking.getBookingId(), fetchedbooking.getBookingId());

	}
}
