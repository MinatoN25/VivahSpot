package com.stackroute.venueservice.test.repository;

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
import com.stackroute.venueservice.model.Venue;
import com.stackroute.venueservice.repository.VenueRepository;

@RunWith(SpringRunner.class)
@DataMongoTest
public class VenueRepositoryTest {

	@Autowired
	private VenueRepository venueRepository;
	private Venue venue;

	@BeforeEach
	public void setUp() throws Exception {
		venue = new Venue();
		venue.setAvailable(true);
		venue.setCuisine(new ArrayList<>());
		venue.setFoodType("Veg");
		venue.setCity("Nh-Highway");
		venue.setRoomDetails(new ArrayList<>());
		venue.setSlots(new ArrayList<>());
		venue.setVenueCapacity(1000);
		venue.setVenueFacilities(new ArrayList<>());
		venue.setVenueId(1001);
		venue.setVenueName("City Roy Grand Park");
		venue.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue.setVenuePrice(1);
		venue.setCurrency("INR");
		venue.setVenueRating(1);
		venue.setVenueSpaceType(new ArrayList<>());
		venue.setVerified(true);
	}

	@AfterEach
	public void tearDown() throws Exception {

		venueRepository.deleteById(1001);
		venueRepository.deleteById(1007);

	}

	@Test
	void saveVenueTest() {
		venueRepository.insert(venue);
		Venue fetchVenue = venueRepository.findById(1001).get();
		Assert.assertEquals(venue.getVenueName(), fetchVenue.getVenueName());
	}

	@Test
	void deleteVenueByIdTest() {
		venueRepository.insert(venue);
		Venue fetchVenue = venueRepository.findById(1001).get();
		Assert.assertEquals(venue.getVenueName(), fetchVenue.getVenueName());
		venueRepository.deleteById(fetchVenue.getVenueId());
		assertThrows(NoSuchElementException.class, () -> {
			venueRepository.findById(1001).get();
		});
	}

	@Test
	void findAllVenueTest() {
		venueRepository.insert(venue);
		venue.setVenueId(1013);
		venueRepository.insert(venue);
		List<Venue> fetchVenue = venueRepository.findAll();
		Assert.assertFalse(fetchVenue.isEmpty());
		Assert.assertNotEquals(fetchVenue, null);
	}

	@Test
	void updateVenueTest() {
		venueRepository.insert(venue);
		Venue fetchedVenue = venueRepository.findById(1001).get();
		fetchedVenue.setVenuePrice(250000);
		venueRepository.save(fetchedVenue);
		fetchedVenue = venueRepository.findById(1001).get();
		Assert.assertEquals(250000, fetchedVenue.getVenuePrice());
	}

	@Test
	void getVenueByIdTest() {
		venueRepository.insert(venue);
		Venue fetchedVenue = venueRepository.findById(1001).get();
		Assert.assertEquals(venue.getVenueId(), fetchedVenue.getVenueId());

	}

	@Test
	void getVenueByNameTest() {
		venueRepository.insert(venue);
		Venue fetchedVenue = venueRepository.findByName("City Roy Grand Park").get(0);
		Assert.assertEquals(venue.getVenueName(), fetchedVenue.getVenueName());

	}

}
