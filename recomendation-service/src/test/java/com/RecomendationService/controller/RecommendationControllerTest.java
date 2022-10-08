//package com.RecomendationService.controller;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import com.RecomendationService.entity.Venue;
//import com.RecomendationService.exception.VenueNotFoundException;
//import com.RecomendationService.repository.RecommendationRepository;
//import com.RecomendationService.service.RecommendationService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@RunWith(SpringRunner.class)
//@WebMvcTest(RecommendationController.class)
//
//public class RecommendationControllerTest {
//
//	@Autowired
//	private MockMvc mock;
//
//	@MockBean
//	private RecommendationService recommendationService;
//
//	@MockBean
//	private RecommendationRepository recommendationRepository;
//
//	@InjectMocks
//	private RecommendationController recommendationController;
//
//	private Venue venue;
//	private Venue venue1;
//
//	private List<Venue> venues;
//
//	@BeforeEach
//	public void setup() {
//		MockitoAnnotations.initMocks(this);
//		venue = new Venue();
//		venue.setVenueId(1);
//		venue.setVenueName("abc");
//		venue.setLandmark("Bidar");
//		venue.setVenuePrice(5000);
//		venue.setVenueCapacity("500");
//		venues = new ArrayList<>();
//		venues.add(venue);
//		
//		venue1=new Venue();
//		venue1.setVenueId(2);
//		venue1.setVenueName("abcd");
//		venue1.setLandmark("Bidar1");
//		venue1.setVenuePrice(2000);
//		venue1.setVenueCapacity("600");
//		venues.add(venue1);
//
//	}
//	
//	@Test
//	void testlistOfVenues() throws Exception {
//		when(recommendationService.listVenues((String) any())).thenReturn(venues);
//		mock.perform(get("/api/v1/search?query=abc")).andExpect(status().isOk())
//		        .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//				.andExpect(MockMvcResultMatchers.content().string(
//						"[{\"venueId\":1,\"venueName\":\"abc\",\"landmark\":\"Bidar\",\"venueSpaceType\":null,\"venueCapacity\":\"500\",\"cuisine\":null,\"venueFacilities\":null,\"venuePrice\":5000,\"foodType\":null,\"venueRating\":0,\"roomDetails\":null,\"verified\":false,\"available\":false}]"))
//				.andDo(print());
//	}
//	
//	@Test
//	void testlistOfVenuesFromLowToHigh() throws Exception {
//		when(recommendationService.listVenuesFromLowToHigh((String) any())).thenReturn(venues);
//		mock.perform(get("/api/v1/searchlowtohigh?query=abc")).andExpect(status().isOk())
//		        .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//				.andExpect(MockMvcResultMatchers.content().string(
//						"[{\"venueId\":1,\"venueName\":\"abc\",\"landmark\":\"Bidar\",\"venueSpaceType\":null,\"venueCapacity\":\"500\",\"cuisine\":null,\"venueFacilities\":null,\"venuePrice\":5000,\"foodType\":null,\"venueRating\":0,\"roomDetails\":null,\"verified\":false,\"available\":false}]"))
//				.andDo(print());
//	}
//	
//	
//	@Test
//	void testlistOfVenuesFromHighToLow() throws Exception {
//		when(recommendationService.listVenuesFromHighToLOw((String) any())).thenReturn(venues);
//		mock.perform(get("/api/v1/searchhightolow?query=abc")).andExpect(status().isOk())
//		        .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//				.andExpect(MockMvcResultMatchers.content().string(
//						"[{\"venueId\":1,\"venueName\":\"abc\",\"landmark\":\"Bidar\",\"venueSpaceType\":null,\"venueCapacity\":\"500\",\"cuisine\":null,\"venueFacilities\":null,\"venuePrice\":5000,\"foodType\":null,\"venueRating\":0,\"roomDetails\":null,\"verified\":false,\"available\":false}]"))
//				.andDo(print());
//	}
//	
//	@Test
//	void testGetVenuesByName() throws Exception {
//		when(recommendationService.listVenueByVenueName((String) any())).thenReturn(venues);
//		mock.perform(get("/api/v1/venue/{venueName}","abc")).andExpect(status().isOk())
//				.andExpect(MockMvcResultMatchers.content().string(
//						"[{\"venueId\":1,\"venueName\":\"abc\",\"landmark\":\"Bidar\",\"venueSpaceType\":null,\"venueCapacity\":\"500\",\"cuisine\":null,\"venueFacilities\":null,\"venuePrice\":5000,\"foodType\":null,\"venueRating\":0,\"roomDetails\":null,\"verified\":false,\"available\":false}]"))
//				.andDo(print());
//	}
//	
//	@Test
//	void testGetVenuesByNameVenueNotFoundException() throws Exception {
//		when(recommendationService.listVenueByVenueName((String) any()))
//				.thenThrow(new VenueNotFoundException("Venue not found for the as per VenueName"));
//		mock.perform(get("/api/v1/venue/{venueName}","abc"))
//		.andExpect(status().isConflict())
//				.andExpect(MockMvcResultMatchers.content().string("Venue not found for the as per VenueName")).andDo(print());
//	}
//	
//	
//	@Test
//	void testfindByPriceAndCapacity() throws Exception {
//
//		when(recommendationService.findByPriceAndCapacity((Integer) any(), (String) any())).thenReturn(venues);
//
//		mock.perform(get("/api/v1/search1/{venuePrice}/{venueCapacity}",5000,"500"))
//		.andExpect(status().isOk())
//				.andExpect(MockMvcResultMatchers.content().string(
//						"[{\"venueId\":1,\"venueName\":\"abc\",\"landmark\":\"Bidar\",\"venueSpaceType\":null,\"venueCapacity\":\"500\",\"cuisine\":null,\"venueFacilities\":null,\"venuePrice\":5000,\"foodType\":null,\"venueRating\":0,\"roomDetails\":null,\"verified\":false,\"available\":false}]"))
//				.andDo(print());
//	}
//	
//	@Test
//	void testfindByPriceAndCapacityVenueNotFoundException() throws Exception {
//		when(recommendationService.findByPriceAndCapacity((Integer) any(), (String) any()))
//				.thenThrow(new VenueNotFoundException("Venue not found for the as per VenuePriceAndCapacity"));
//		mock.perform(get("/api/v1/search1/{venuePrice}/{venueCapacity}",4000,"400"))
//		.andExpect(status().isConflict())
//				.andExpect(MockMvcResultMatchers.content().string("Venue not found for the as per VenuePriceAndCapacity")).andDo(print());
//	}
//}
