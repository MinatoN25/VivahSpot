//package com.RecomendationService.repositoryTest;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotSame;
//
//import java.util.List;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.RecomendationService.entity.Venue;
//import com.RecomendationService.repository.RecommendationRepository;
//
//
//@RunWith(SpringRunner.class)
//@DataMongoTest
//public class RecommendationRepositoryTest {
//	
//	@Autowired
//	private RecommendationRepository recommendationRepository;
//	private Venue venue;
//	
//	
//	
//	@BeforeEach
//	public void setUp() throws Exception {
//		venue = new Venue();
//		venue.setVenueId(1010);
//		venue.setVenueName("Royal Residence");
//		venue.setVenuePrice(250000);
//		venue.setFoodType("VEG");
//		venue.setAvailable(true);
//		venue.setVenueRating(4);
//		venue.setVerified(true);
//		venue.setVenueCapacity("upto 800 members");
//		venue.setLandmark("NH-1 Highway");
//		recommendationRepository.deleteById(1010);
//		recommendationRepository.deleteById(1011);
//	}
//	
//	@AfterEach
//	public void tearDown() throws Exception {
//		recommendationRepository.deleteById(1010);
//		recommendationRepository.deleteById(1011);
//	}
//	
//	
//	@Test
//	public void saveVenueTest() {
//		recommendationRepository.insert(venue);
//		Venue fetchVenue = recommendationRepository.findById(1010).get();
//		assertEquals(venue.getVenueName(), fetchVenue.getVenueName());
//	}
//	
//	@Test
//	public void findAllVenueTest() {
//		recommendationRepository.insert(venue);
//		venue.setVenueId(1011);
//		recommendationRepository.insert(venue);
//		List<Venue> fetchVenue = recommendationRepository.findAll();
//		assertFalse(fetchVenue.isEmpty());
//		assertNotSame(fetchVenue, null);
//	}
//	
//	@Test
//	public void findAllVenueLowToHighTest() {
//		recommendationRepository.insert(venue);
//		venue.setVenueId(1011);
//		recommendationRepository.insert(venue);
//		List<Venue> fetchVenue = recommendationRepository.findVenueByVenuePriceLowtoHigh("NH-1 Highway");
//		assertFalse(fetchVenue.isEmpty());
//		assertNotSame(fetchVenue, null);
//	}
//	
//	@Test
//	public void findAllVenueHighToLowTest() {
//		recommendationRepository.insert(venue);
//		venue.setVenueId(1011);
//		recommendationRepository.insert(venue);
//		List<Venue> fetchVenue = recommendationRepository.findVenueByVenuePriceHighToLow("NH-1 Highway");
//		assertFalse(fetchVenue.isEmpty());
//		assertNotSame(fetchVenue, null);
//	}
//	
//	@Test
//	public void getVenueByNameTest() {
//		recommendationRepository.insert(venue);
//		Venue fetchedVenue = recommendationRepository.getByVenueName("Royal Residence").get(0);
//		assertEquals(venue.getVenueName(), fetchedVenue.getVenueName());
//	}
//	
//	@Test
//	public void getVenueByNameAndPriceTest() {
//		recommendationRepository.insert(venue);
//		List<Venue> fetchedVenue = recommendationRepository.findByPriceAndCapacity(250000, "upto 800 members");
//		assertEquals(venue.getVenuePrice().intValue(), 250000);
//		assertEquals(venue.getVenueCapacity(), "upto 800 members");
//	}
//}
