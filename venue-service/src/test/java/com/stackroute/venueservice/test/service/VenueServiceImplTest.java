package com.stackroute.venueservice.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.stackroute.venueservice.exception.BookingNotFoundException;
import com.stackroute.venueservice.exception.VenueAlreadyExistException;
import com.stackroute.venueservice.exception.VenueNotBookedException;
import com.stackroute.venueservice.exception.VenueNotFoundException;
import com.stackroute.venueservice.model.Booking;
import com.stackroute.venueservice.model.DatabaseSequence;
import com.stackroute.venueservice.model.Slot;
import com.stackroute.venueservice.model.Status;
import com.stackroute.venueservice.model.Venue;
import com.stackroute.venueservice.repository.BookingRepository;
import com.stackroute.venueservice.repository.VenueRepository;
import com.stackroute.venueservice.service.VenueServiceImpl;

@ContextConfiguration(classes = {VenueServiceImpl.class})
@ExtendWith(SpringExtension.class)
class VenueServiceImplTest {
    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private MongoOperations mongoOperations;

    @MockBean
    private VenueRepository venueRepository;

    @Autowired
    private VenueServiceImpl venueServiceImpl;

    @Test
    void testAddVenue_VenueAlreadyExist() throws VenueAlreadyExistException, IOException {
        when(venueRepository.save((Venue) any())).thenThrow(new VenueAlreadyExistException("An error occurred"));
        when(venueRepository.findById((Integer) any())).thenReturn(Optional.empty());

        DatabaseSequence databaseSequence = new DatabaseSequence();
        databaseSequence.setId("42");
        databaseSequence.setSeq(1);
        when(mongoOperations.findAndModify((Query) any(), (UpdateDefinition) any(), (FindAndModifyOptions) any(),
                (Class<DatabaseSequence>) any())).thenReturn(databaseSequence);

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
        assertThrows(VenueAlreadyExistException.class, () -> venueServiceImpl.addVenue(venue));
        verify(venueRepository).save((Venue) any());
        verify(venueRepository).findById((Integer) any());
        verify(mongoOperations).findAndModify((Query) any(), (UpdateDefinition) any(), (FindAndModifyOptions) any(),
                (Class<DatabaseSequence>) any());
    }
    

    @Test
    void testAddVenue_Success() throws VenueAlreadyExistException, IOException {
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
        when(venueRepository.save((Venue) any())).thenReturn(venue);
        when(venueRepository.findById((Integer) any())).thenReturn(Optional.empty());

        DatabaseSequence databaseSequence = new DatabaseSequence();
        databaseSequence.setId("42");
        databaseSequence.setSeq(1);
        when(mongoOperations.findAndModify((Query) any(), (UpdateDefinition) any(), (FindAndModifyOptions) any(),
                (Class<DatabaseSequence>) any())).thenReturn(databaseSequence);
        assertSame(venue, venueServiceImpl.addVenue(venue));
        verify(venueRepository).save((Venue) any());
        verify(venueRepository).findById((Integer) any());
        verify(mongoOperations).findAndModify((Query) any(), (UpdateDefinition) any(), (FindAndModifyOptions) any(),
                (Class<DatabaseSequence>) any());
        assertEquals(1, venue.getVenueId());
    }
    
    @Test
    void testUpdateVenue_Success() throws VenueNotFoundException, UnsupportedEncodingException {
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
        venue1.setCurrency("INR");
        venue1.setVenueRating(1);
        venue1.setVenueSpaceType(new ArrayList<>());
        venue1.setVerified(true);
        when(venueRepository.findById(venue.getVenueId())).thenReturn(Optional.of(venue));
        assertSame(venue, venueServiceImpl.updateVenue(venue));
        verify(venueRepository).findAll();
        verify(venueRepository).findById((Integer) any());
    }
    @Test
    void testUpdateVenue_throwsException() throws VenueNotFoundException, UnsupportedEncodingException {
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
        venue1.setCurrency("INR");
        venue1.setVenueRating(1);
        venue1.setVenueSpaceType(new ArrayList<>());
        venue1.setVerified(true);
             assertThrows(VenueNotFoundException.class, () -> venueServiceImpl.updateVenue(venue1));
    }

    @Test
    void testDeleteVenue_Success() throws VenueNotFoundException, UnsupportedEncodingException {
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
        doNothing().when(venueRepository).deleteById((Integer) any());
        when(venueRepository.findById((Integer) any())).thenReturn(ofResult);
        Optional<Venue> actualDeleteVenueResult = venueServiceImpl.deleteVenue(123);
        assertSame(ofResult, actualDeleteVenueResult);
        assertTrue(actualDeleteVenueResult.isPresent());
        verify(venueRepository).findById((Integer) any());
        verify(venueRepository).deleteById((Integer) any());
    }
    
    @Test
    void testDeleteVenue_ThrowsException() throws VenueNotFoundException, UnsupportedEncodingException {
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
        doThrow(new VenueAlreadyExistException("An error occurred")).when(venueRepository).deleteById((Integer) any());
        when(venueRepository.findById((Integer) any())).thenReturn(ofResult);
        assertThrows(VenueAlreadyExistException.class, () -> venueServiceImpl.deleteVenue(123));
        verify(venueRepository).findById((Integer) any());
        verify(venueRepository).deleteById((Integer) any());
    }
    
    @Test
    void testGetVenueById_Succes() throws VenueNotFoundException, UnsupportedEncodingException {
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
        when(venueRepository.findById((Integer) any())).thenReturn(ofResult);
        Optional<Venue> actualVenueById = venueServiceImpl.getVenueById(123);
        assertSame(ofResult, actualVenueById);
        assertTrue(actualVenueById.isPresent());
        verify(venueRepository, atLeast(1)).findById((Integer) any());
    }

    @Test
    void testGetVenueById_ThrowsException() throws VenueNotFoundException {
        when(venueRepository.findById((Integer) any())).thenReturn(Optional.empty());
        assertThrows(VenueNotFoundException.class, () -> venueServiceImpl.getVenueById(123));
        verify(venueRepository).findById((Integer) any());
    }
    
    @Test
    void testGetAllVenues_ThrowsException() throws VenueNotFoundException {
        when(venueRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(VenueNotFoundException.class, () -> venueServiceImpl.getAllVenues());
        verify(venueRepository).findAll();
    }
    
    @Test
    void testGetAllVenues_Success() throws VenueNotFoundException, UnsupportedEncodingException {
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

        ArrayList<Venue> venueList = new ArrayList<>();
        venueList.add(venue);
        when(venueRepository.findAll()).thenReturn(venueList);
        List<Venue> actualAllVenues = venueServiceImpl.getAllVenues();
        assertSame(venueList, actualAllVenues);
        assertEquals(1, actualAllVenues.size());
        verify(venueRepository, atLeast(1)).findAll();
    }


    @Test
    void testListVenuesByName_Success() {
        ArrayList<Venue> venueList = new ArrayList<>();
        when(venueRepository.findByName((String) any())).thenReturn(venueList);
        List<Venue> actualListVenuesByNameResult = venueServiceImpl.listVenuesByName("Query");
        assertSame(venueList, actualListVenuesByNameResult);
        assertTrue(actualListVenuesByNameResult.isEmpty());
        verify(venueRepository).findByName((String) any());
    }
    
    @Test
    void testGiveRatingToBookedVenue() throws UnsupportedEncodingException {
        when(bookingRepository.getBookingByUserEmail((String) any())).thenReturn(new ArrayList<>());

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
        assertThrows(VenueNotFoundException.class,
                () -> venueServiceImpl.giveRatingToBookedVenue(123 ,4, "jane.doe@example.org"));
        verify(bookingRepository).getBookingByUserEmail((String) any());
    }
    
    @Test
    void testGiveRatingToBookedVenue_BookingNotFoundException() throws UnsupportedEncodingException {
    	 when(bookingRepository.getBookingByUserEmail((String) any())).thenThrow(new BookingNotFoundException("Booking Not Found"));
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
       
        when(venueRepository.findById((Integer)any())).thenReturn(Optional.of(venue));
        assertThrows(BookingNotFoundException.class,
                () -> venueServiceImpl.giveRatingToBookedVenue(123 ,4, "jane.doe@example.org"));
        verify(bookingRepository).getBookingByUserEmail((String) any());
    }
    
    @Test
    void testGiveRatingToBookedVenue_VenueNotBookedException() throws UnsupportedEncodingException {
    	Booking booking =new Booking();
    	List<Booking> bookings=new ArrayList<>();
    	booking.setBookingId(1);
    	booking.setVenueId(123);
    	booking.setSlot(new Slot(1,"evening",Status.PENDING_FOR_APPROVAL));
    	bookings.add(booking);
    	 when(bookingRepository.getBookingByUserEmail((String) any())).thenReturn(bookings);
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
       
        when(venueRepository.findById((Integer)any())).thenReturn(Optional.of(venue));
        assertThrows(VenueNotBookedException.class,
                () -> venueServiceImpl.giveRatingToBookedVenue(123 ,4, "jane.doe@example.org"));
        verify(bookingRepository).getBookingByUserEmail((String) any());
    }
    
    @Test
    void testGiveRatingToBookedVenue_Success() throws UnsupportedEncodingException {
    	Booking booking =new Booking();
    	List<Booking> bookings=new ArrayList<>();
    	booking.setBookingId(1);
    	booking.setVenueId(123);
    	booking.setSlot(new Slot(1,"evening",Status.BOOKED));
    	bookings.add(booking);
    	 when(bookingRepository.getBookingByUserEmail((String) any())).thenReturn(bookings);
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
       
        when(venueRepository.findById((Integer)any())).thenReturn(Optional.of(venue));
        assertEquals(4,venueServiceImpl.giveRatingToBookedVenue(123 ,4, "jane.doe@example.org").getVenueRating());
        verify(bookingRepository).getBookingByUserEmail((String) any());
    }
}

