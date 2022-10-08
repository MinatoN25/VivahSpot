package com.stackroute.booking.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.stackroute.booking.model.Slot;
import com.stackroute.booking.model.Status;
import com.stackroute.booking.model.Venue;

@RunWith(SpringRunner.class)
@DataMongoTest
class VenueRepositoryTest {

	@Autowired
	private VenueRepository venueRepository;
	private Venue venue;
	private List<Slot> slots;

	@BeforeEach
	public void setUp() throws Exception {
		venue = new Venue();
		venue.setVenueId(12341);
		slots = new ArrayList<>();
		slots.add(new Slot(0, Status.BOOKED, "Morning"));
		slots.add(new Slot(1, Status.BOOKED, "Evening"));
		venue.setSlots(slots);
		venueRepository.deleteById(12331);
		venueRepository.deleteById(12341);
	}

	@AfterEach
	public void tearDown() throws Exception {

		venueRepository.deleteById(12341);

	}

	@Test
	void saveVenueTest() {

		venueRepository.insert(venue);
		Venue fetchvenue = venueRepository.findById(12341).get();
		Assert.assertEquals(venue.getSlots(), fetchvenue.getSlots());
	}

	@Test
	void deleteVenueTest() {
		venueRepository.insert(venue);
		Venue fetchvenue = venueRepository.findById(12341).get();
		Assert.assertEquals(venue.getSlots(), fetchvenue.getSlots());
		venueRepository.delete(fetchvenue);
		assertThrows(NoSuchElementException.class, () -> {
			venueRepository.findById(1001).get();
		});
	}

	@Test
	void updateVenueTest() {
		venueRepository.insert(venue);
		Venue fetchedvenue = venueRepository.findById(12341).get();
		slots.get(0).setSlotStatus(Status.BOOKED);
		fetchedvenue.setSlots(slots);
		venueRepository.save(fetchedvenue);
		fetchedvenue = venueRepository.findById(12341).get();
		Assert.assertEquals(Status.BOOKED, fetchedvenue.getSlots().get(0).getSlotStatus());
	}

	@Test
	void getVenueByIdTest() {
		venueRepository.insert(venue);
		Venue fetchedvenue = venueRepository.findById(12341).get();
		Assert.assertEquals(venue.getVenueId(), fetchedvenue.getVenueId());

	}

}
