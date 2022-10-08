package com.stackroute.booking.model;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import com.stackroute.booking.model.Booking;
import com.stackroute.booking.model.Slot;
import com.stackroute.booking.model.Status;

public class BookingTest {
	private static Booking booking;
	
	private static List<Slot> slots;
	@BeforeAll
	public static void setUp() throws Exception {

		booking = new Booking();
		booking.setBookingId(1);
		booking.setCurrency("USD");
		booking.setSlot(new Slot(1,Status.BOOKED,"Morning"));
		booking.setSlotId(1);
		booking.setUserEmail("abc@gmail.com");
		booking.setVenueId(1);
		booking.setVenuePrice(9.00);
		booking.setBookingDate(LocalDate.now());
	}

	@Test
	void test() {
		BeanTester beanTester=new BeanTester();
		beanTester.getFactoryCollection().addFactory(LocalDate.class,
	            new LocalDateFactory());
		beanTester.testBean(Booking.class);
	}

}
