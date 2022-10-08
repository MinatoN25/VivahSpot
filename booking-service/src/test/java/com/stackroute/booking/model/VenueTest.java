package com.stackroute.booking.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import com.stackroute.booking.model.Slot;
import com.stackroute.booking.model.Status;
import com.stackroute.booking.model.Venue;


public class VenueTest {
	private static Venue venue;
	private static List<Slot> slots;
	@BeforeAll
	public static void setUp() throws Exception {

		venue = new Venue();
		venue.setVenueId(1001);
		slots = new ArrayList<>();
		
		slots.add(new Slot(0,Status.BOOKED, "Morning"));
		slots.add(new Slot(1,Status.BOOKED, "Evening"));
		venue.setSlots(slots);
		venue.setApprovalRequired(false);
		venue.setVenueOwnerEmail("owner@gmail.com");
		
	}

	@Test
	void test() {
		new BeanTester().testBean(Venue.class);
	}

}
