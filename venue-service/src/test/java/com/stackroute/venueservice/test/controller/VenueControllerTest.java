package com.stackroute.venueservice.test.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.venueservice.controller.VenueController;
import com.stackroute.venueservice.exception.VenueAlreadyExistException;
import com.stackroute.venueservice.exception.VenueNotFoundException;
import com.stackroute.venueservice.model.Venue;
import com.stackroute.venueservice.repository.VenueRepository;
import com.stackroute.venueservice.service.VenueService;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = { VenueController.class })
@ExtendWith(SpringExtension.class)
class VenueControllerTest {
	@Autowired
	private VenueController venueController;

	@MockBean
	private VenueRepository venueRepository;

	@MockBean
	private VenueService venueService;

	@Test
	void testDeleteVenue_Success() throws Exception {
		Venue venue = new Venue();
		venue.setAvailable(true);
		venue.setCuisine(new ArrayList<>());
		venue.setFoodType("Veg");
		venue.setCity("Mumbai");
		venue.setRoomDetails(new ArrayList<>());
		venue.setSlots(new ArrayList<>());
		venue.setVenueCapacity(1000);
		venue.setVenueFacilities(new ArrayList<>());
		venue.setVenueId(123);
		venue.setVenueName("City Roy Grand Park");
		venue.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue.setVenuePrice(1);
		venue.setCurrency("INR");
		venue.setVenueRating(1);
		venue.setVenueSpaceType(new ArrayList<>());
		venue.setVerified(true);
		Optional<Venue> ofResult = Optional.of(venue);
		when(venueService.deleteVenue(anyInt())).thenReturn(ofResult);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/deleteVenue/{venueId}",
				123);
		MockMvcBuilders.standaloneSetup(venueController).build().perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"venueId\":123,\"venueName\":\"City Roy Grand Park\",\"city\":\"Mumbai\",\"venueSpaceType\":[],\"venueCapacity\":1000,\"cuisine\":[],\"venueFacilities\":[],\"venuePrice\":1,\"currency\":\"INR\",\"foodType\":\"Veg\",\"venueRating\":1,\"roomDetails\":[],\"slots\":[],\"venueOwnerEmail\":\"rod.johnson@gmail.com\",\"available\":true,\"verified\":true}"));
	}

	@Test
	void testDelete_VenueNotFoundException() throws Exception {
		when(venueService.deleteVenue(anyInt())).thenThrow(new VenueNotFoundException("An error occurred"));
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/deleteVenue/{venueId}",
				123);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(venueController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("Venue Not Found"));
	}

	@Test
	void testGetVenueById_success() throws Exception {
		Venue venue = new Venue();
		venue.setAvailable(true);
		venue.setCuisine(new ArrayList<>());
		venue.setFoodType("Veg");
		venue.setCity("Mumbai");
		venue.setRoomDetails(new ArrayList<>());
		venue.setSlots(new ArrayList<>());
		venue.setVenueCapacity(1000);
		venue.setVenueFacilities(new ArrayList<>());
		venue.setVenueId(123);
		venue.setVenueName("City Roy Grand Park");
		venue.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue.setVenuePrice(1);
		venue.setCurrency("INR");
		venue.setVenueRating(1);
		venue.setVenueSpaceType(new ArrayList<>());
		venue.setVerified(true);
		Optional<Venue> ofResult = Optional.of(venue);
		when(venueService.getVenueById(anyInt())).thenReturn(ofResult);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/getVenueById/{venueId}",
				123);
		MockMvcBuilders.standaloneSetup(venueController).build().perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"venueId\":123,\"venueName\":\"City Roy Grand Park\",\"city\":\"Mumbai\",\"venueSpaceType\":[],\"venueCapacity\":1000,\"cuisine\":[],\"venueFacilities\":[],\"venuePrice\":1,\"currency\":\"INR\",\"foodType\":\"Veg\",\"venueRating\":1,\"roomDetails\":[],\"slots\":[],\"venueOwnerEmail\":\"rod.johnson@gmail.com\",\"available\":true,\"verified\":true}"));
	}

	@Test
	void testGetVenueById_failure() throws Exception {
		when(venueService.getVenueById(anyInt())).thenThrow(new VenueNotFoundException("An error occurred"));
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/getVenueById/{venueId}",
				123);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(venueController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("Venue Not Found"));
	}

	@Test
	void testAddVenue_Success() throws Exception {
		Venue venue = new Venue();
		venue.setAvailable(true);
		venue.setCuisine(new ArrayList<>());
		venue.setFoodType("Veg");
		venue.setCity("Mumbai");
		venue.setRoomDetails(new ArrayList<>());
		venue.setSlots(new ArrayList<>());
		venue.setVenueCapacity(1000);
		venue.setVenueFacilities(new ArrayList<>());
		venue.setVenueId(123);
		venue.setVenueName("City Roy Grand Park");
		venue.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue.setVenuePrice(1);
		venue.setCurrency("INR");
		venue.setVenueRating(1);
		venue.setVenueSpaceType(new ArrayList<>());
		venue.setVerified(true);
		String content = (new ObjectMapper()).writeValueAsString(venue);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/registerVenue")
				.contentType(MediaType.APPLICATION_JSON).content(content);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(venueController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"venueId\":123,\"venueName\":\"City Roy Grand Park\",\"city\":\"Mumbai\",\"venueSpaceType\":[],\"venueCapacity\":1000,\"cuisine\":[],\"venueFacilities\":[],\"venuePrice\":1,\"currency\":\"INR\",\"foodType\":\"Veg\",\"venueRating\":1,\"roomDetails\":[],\"slots\":[],\"venueOwnerEmail\":\"rod.johnson@gmail.com\",\"available\":true,\"verified\":true}"));
	}

	@Test
	void testAddVenue_Validation_Check_FoodType() throws Exception {
		Venue venue = new Venue();
		venue.setAvailable(true);
		venue.setCuisine(new ArrayList<>());
		venue.setFoodType("Veg");
		venue.setCity("Mumbai");
		venue.setRoomDetails(new ArrayList<>());
		venue.setSlots(new ArrayList<>());
		venue.setVenueCapacity(1000);
		venue.setVenueFacilities(new ArrayList<>());
		venue.setVenueId(123);
		venue.setVenueName("City Roy Grand Park");
		venue.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue.setVenuePrice(1);
		venue.setCurrency("INR");
		venue.setVenueRating(1);
		venue.setVenueSpaceType(new ArrayList<>());
		venue.setVerified(true);
		when(venueService.addVenue((Venue) any())).thenReturn(venue);

		Venue venue1 = new Venue();
		venue1.setAvailable(true);
		venue1.setCuisine(new ArrayList<>());
		venue1.setFoodType(null);
		venue1.setCity("Mumbai");
		venue1.setRoomDetails(new ArrayList<>());
		venue1.setSlots(new ArrayList<>());
		venue1.setVenueCapacity(1000);
		venue1.setVenueFacilities(new ArrayList<>());
		venue1.setVenueId(123);
		venue1.setVenueName("City Roy Grand Park");
		venue1.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue1.setVenuePrice(1);
		venue1.setCurrency("INR");
		venue1.setVenueRating(1);
		venue1.setVenueSpaceType(new ArrayList<>());
		venue1.setVerified(true);
		String content = (new ObjectMapper()).writeValueAsString(venue1);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/registerVenue")
				.contentType(MediaType.APPLICATION_JSON).content(content);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(venueController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().is(502))
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("Please enter all the required fields"));
	}

	@Test
	void testAddVenue_Validation_Check_City() throws Exception {
		Venue venue = new Venue();
		venue.setAvailable(true);
		venue.setCuisine(new ArrayList<>());
		venue.setFoodType("Veg");
		venue.setCity("Mumbai");
		venue.setRoomDetails(new ArrayList<>());
		venue.setSlots(new ArrayList<>());
		venue.setVenueCapacity(1000);
		venue.setVenueFacilities(new ArrayList<>());
		venue.setVenueId(123);
		venue.setVenueName("City Roy Grand Park");
		venue.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue.setVenuePrice(1);
		venue.setCurrency("INR");
		venue.setVenueRating(1);
		venue.setVenueSpaceType(new ArrayList<>());
		venue.setVerified(true);
		when(venueService.addVenue((Venue) any())).thenReturn(venue);

		Venue venue1 = new Venue();
		venue1.setAvailable(true);
		venue1.setCuisine(new ArrayList<>());
		venue1.setFoodType("Veg");
		venue1.setCity(null);
		venue1.setRoomDetails(new ArrayList<>());
		venue1.setSlots(new ArrayList<>());
		venue1.setVenueCapacity(1000);
		venue1.setVenueFacilities(new ArrayList<>());
		venue1.setVenueId(123);
		venue1.setVenueName("City Roy Grand Park");
		venue1.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue1.setVenuePrice(1);
		venue1.setCurrency("INR");
		venue1.setVenueRating(1);
		venue1.setVenueSpaceType(new ArrayList<>());
		venue1.setVerified(true);
		String content = (new ObjectMapper()).writeValueAsString(venue1);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/registerVenue")
				.contentType(MediaType.APPLICATION_JSON).content(content);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(venueController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().is(502))
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("Please enter all the required fields"));
	}

	@Test
	void testAddVenue_Validation_Check_VenueCapacity() throws Exception {
		Venue venue = new Venue();
		venue.setAvailable(true);
		venue.setCuisine(new ArrayList<>());
		venue.setFoodType("Veg");
		venue.setCity("Mumbai");
		venue.setRoomDetails(new ArrayList<>());
		venue.setSlots(new ArrayList<>());
		venue.setVenueCapacity(1000);
		venue.setVenueFacilities(new ArrayList<>());
		venue.setVenueId(123);
		venue.setVenueName("City Roy Grand Park");
		venue.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue.setVenuePrice(1);
		venue.setCurrency("INR");
		venue.setVenueRating(1);
		venue.setVenueSpaceType(new ArrayList<>());
		venue.setVerified(true);
		when(venueService.addVenue((Venue) any())).thenReturn(venue);

		Venue venue1 = new Venue();
		venue1.setAvailable(true);
		venue1.setCuisine(new ArrayList<>());
		venue1.setFoodType("Veg");
		venue1.setCity("Mumbai");
		venue1.setRoomDetails(new ArrayList<>());
		venue1.setSlots(new ArrayList<>());
		venue1.setVenueCapacity(0);
		venue1.setVenueFacilities(new ArrayList<>());
		venue1.setVenueId(123);
		venue1.setVenueName("City Roy Grand Park");
		venue1.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue1.setVenuePrice(1);
		venue1.setCurrency("INR");
		venue1.setVenueRating(1);
		venue1.setVenueSpaceType(new ArrayList<>());
		venue1.setVerified(true);
		String content = (new ObjectMapper()).writeValueAsString(venue1);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/registerVenue")
				.contentType(MediaType.APPLICATION_JSON).content(content);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(venueController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().is(502))
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("Please enter all the required fields"));
	}

	@Test
	void testAddVenue_Validation_Check_VenueName() throws Exception {
		Venue venue = new Venue();
		venue.setAvailable(true);
		venue.setCuisine(new ArrayList<>());
		venue.setFoodType("Veg");
		venue.setCity("Mumbai");
		venue.setRoomDetails(new ArrayList<>());
		venue.setSlots(new ArrayList<>());
		venue.setVenueCapacity(1000);
		venue.setVenueFacilities(new ArrayList<>());
		venue.setVenueId(123);
		venue.setVenueName("City Roy Grand Park");
		venue.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue.setVenuePrice(1);
		venue.setCurrency("INR");
		venue.setVenueRating(1);
		venue.setVenueSpaceType(new ArrayList<>());
		venue.setVerified(true);
		when(venueService.addVenue((Venue) any())).thenReturn(venue);

		Venue venue1 = new Venue();
		venue1.setAvailable(true);
		venue1.setCuisine(new ArrayList<>());
		venue1.setFoodType("Veg");
		venue1.setCity("Mumbai");
		venue1.setRoomDetails(new ArrayList<>());
		venue1.setSlots(new ArrayList<>());
		venue1.setVenueCapacity(1000);
		venue1.setVenueFacilities(new ArrayList<>());
		venue1.setVenueId(123);
		venue1.setVenueName(null);
		venue1.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue1.setVenuePrice(1);
		venue1.setCurrency("INR");
		venue1.setVenueRating(1);
		venue1.setVenueSpaceType(new ArrayList<>());
		venue1.setVerified(true);
		String content = (new ObjectMapper()).writeValueAsString(venue1);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/registerVenue")
				.contentType(MediaType.APPLICATION_JSON).content(content);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(venueController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().is(502))
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("Please enter all the required fields"));
	}

	@Test
	void testAddVenue_Validation_Check_VenuePrice() throws Exception {
		Venue venue = new Venue();
		venue.setAvailable(true);
		venue.setCuisine(new ArrayList<>());
		venue.setFoodType("Veg");
		venue.setCity("Mumbai");
		venue.setRoomDetails(new ArrayList<>());
		venue.setSlots(new ArrayList<>());
		venue.setVenueCapacity(1000);
		venue.setVenueFacilities(new ArrayList<>());
		venue.setVenueId(123);
		venue.setVenueName("City Roy Grand Park");
		venue.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue.setVenuePrice(1);
		venue.setCurrency("INR");
		venue.setVenueRating(1);
		venue.setVenueSpaceType(new ArrayList<>());
		venue.setVerified(true);
		when(venueService.addVenue((Venue) any())).thenReturn(venue);

		Venue venue1 = new Venue();
		venue1.setAvailable(true);
		venue1.setCuisine(new ArrayList<>());
		venue1.setFoodType("Veg");
		venue1.setCity("Mumbai");
		venue1.setRoomDetails(new ArrayList<>());
		venue1.setSlots(new ArrayList<>());
		venue1.setVenueCapacity(1000);
		venue1.setVenueFacilities(new ArrayList<>());
		venue1.setVenueId(123);
		venue1.setVenueName("City Roy Grand Park");
		venue1.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue1.setVenuePrice(0);
		venue1.setCurrency("INR");
		venue1.setVenueRating(1);
		venue1.setVenueSpaceType(new ArrayList<>());
		venue1.setVerified(true);
		String content = (new ObjectMapper()).writeValueAsString(venue1);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/registerVenue")
				.contentType(MediaType.APPLICATION_JSON).content(content);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(venueController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().is(502))
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("Please enter all the required fields"));
	}

	@Test
	void testAddVenue_Validation_Check_Currency() throws Exception {
		Venue venue = new Venue();
		venue.setAvailable(true);
		venue.setCuisine(new ArrayList<>());
		venue.setFoodType("Veg");
		venue.setCity("Mumbai");
		venue.setRoomDetails(new ArrayList<>());
		venue.setSlots(new ArrayList<>());
		venue.setVenueCapacity(1000);
		venue.setVenueFacilities(new ArrayList<>());
		venue.setVenueId(123);
		venue.setVenueName("City Roy Grand Park");
		venue.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue.setVenuePrice(1);
		venue.setCurrency("INR");
		venue.setVenueRating(1);
		venue.setVenueSpaceType(new ArrayList<>());
		venue.setVerified(true);
		when(venueService.addVenue((Venue) any())).thenReturn(venue);

		Venue venue1 = new Venue();
		venue1.setAvailable(true);
		venue1.setCuisine(new ArrayList<>());
		venue1.setFoodType("Veg");
		venue1.setCity("Mumbai");
		venue1.setRoomDetails(new ArrayList<>());
		venue1.setSlots(new ArrayList<>());
		venue1.setVenueCapacity(1000);
		venue1.setVenueFacilities(new ArrayList<>());
		venue1.setVenueId(123);
		venue1.setVenueName("City Roy Grand Park");
		venue1.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue1.setVenuePrice(1);
		venue1.setCurrency(null);
		venue1.setVenueRating(1);
		venue1.setVenueSpaceType(new ArrayList<>());
		venue1.setVerified(true);
		String content = (new ObjectMapper()).writeValueAsString(venue1);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/registerVenue")
				.contentType(MediaType.APPLICATION_JSON).content(content);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(venueController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().is(502))
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("Please enter all the required fields"));
	}

	@Test
	void testAddVenue_Exception() throws Exception {
		when(venueService.addVenue((Venue) any())).thenThrow(new VenueAlreadyExistException("An error occurred"));

		Venue venue = new Venue();
		venue.setAvailable(true);
		venue.setCuisine(new ArrayList<>());
		venue.setFoodType("Veg");
		venue.setCity("Mumbai");
		venue.setRoomDetails(new ArrayList<>());
		venue.setSlots(new ArrayList<>());
		venue.setVenueCapacity(1000);
		venue.setVenueFacilities(new ArrayList<>());
		venue.setVenueId(123);
		venue.setVenueName("City Roy Grand Park");
		venue.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue.setVenuePrice(1);
		venue.setCurrency("INR");
		venue.setVenueRating(1);
		venue.setVenueSpaceType(new ArrayList<>());
		venue.setVerified(true);
		String content = (new ObjectMapper()).writeValueAsString(venue);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/registerVenue")
				.contentType(MediaType.APPLICATION_JSON).content(content);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(venueController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().is(409))
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("Venue Exists Already"));
	}

	@Test
	void testGetAllVenues_Success() throws Exception {
		when(venueService.getAllVenues()).thenReturn(new ArrayList<>());
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/getAllVenues");
		MockMvcBuilders.standaloneSetup(venueController).build().perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string("[]"));
	}

	@Test
	void testGetAllVenues_Exception() throws Exception {
		when(venueService.getAllVenues()).thenThrow(new VenueNotFoundException("An error occurred"));
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/getAllVenues");
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(venueController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("Venue Not Found"));
	}

	@Test
	void testUpdateVenue_Success() throws Exception {
		Venue venue = new Venue();
		venue.setAvailable(true);
		venue.setCuisine(new ArrayList<>());
		venue.setFoodType("Veg");
		venue.setCity("Mumbai");
		venue.setRoomDetails(new ArrayList<>());
		venue.setSlots(new ArrayList<>());
		venue.setVenueCapacity(1000);
		venue.setVenueFacilities(new ArrayList<>());
		venue.setVenueId(123);
		venue.setVenueName("City Roy Grand Park");
		venue.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue.setVenuePrice(1);
		venue.setCurrency("INR");
		venue.setVenueRating(1);
		venue.setVenueSpaceType(new ArrayList<>());
		venue.setVerified(true);
		when(venueService.updateVenue((Venue) any())).thenReturn(venue);

		Venue venue1 = new Venue();
		venue1.setAvailable(true);
		venue1.setCuisine(new ArrayList<>());
		venue1.setFoodType("Veg");
		venue1.setCity("Mumbai");
		venue1.setRoomDetails(new ArrayList<>());
		venue1.setSlots(new ArrayList<>());
		venue1.setVenueCapacity(1000);
		venue1.setVenueFacilities(new ArrayList<>());
		venue1.setVenueId(123);
		venue1.setVenueName("Roy Grand Park");
		venue1.setVenueOwnerEmail("rod.johnson@gmail.com");
		venue1.setVenuePrice(1);
		venue1.setCurrency("INR");
		venue1.setVenueRating(1);
		venue1.setVenueSpaceType(new ArrayList<>());
		venue1.setVerified(true);
		String content = (new ObjectMapper()).writeValueAsString(venue1);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/updateVenue")
				.contentType(MediaType.APPLICATION_JSON).content(content);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(venueController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"venueId\":123,\"venueName\":\"City Roy Grand Park\",\"city\":\"Mumbai\",\"venueSpaceType\":[],\"venueCapacity\":1000,\"cuisine\":[],\"venueFacilities\":[],\"venuePrice\":1,\"currency\":\"INR\",\"foodType\":\"Veg\",\"venueRating\":1,\"roomDetails\":[],\"slots\":[],\"venueOwnerEmail\":\"rod.johnson@gmail.com\",\"available\":true,\"verified\":true}"));
	}

	@Test
	void testGetVenueByVenueName() throws Exception {
		when(venueService.listVenuesByName((String) any())).thenReturn(new ArrayList<>());
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/getVenueByName/{venueName}",
				"City Roy Grand Park");
		MockMvcBuilders.standaloneSetup(venueController).build().perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string("[]"));
	}

}
