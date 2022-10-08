//package com.RecomendationService.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.anyBoolean;
//import static org.mockito.Mockito.anyInt;
//import static org.mockito.Mockito.atLeast;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.RecomendationService.entity.Room;
//import com.RecomendationService.exception.VenueAlreadyExistException;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Disabled;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import com.RecomendationService.entity.Venue;
//import com.RecomendationService.exception.VenueNotFoundException;
//import com.RecomendationService.repository.RecommendationRepository;
//
//@ContextConfiguration(classes = {RecommendationServiceImpl.class})
//@ExtendWith(SpringExtension.class)
//class RecommendationServiceImplTest {
//    @MockBean
//    private RecommendationRepository recommendationRepository;
//
//    @Autowired
//    private RecommendationServiceImpl recommendationServiceImpl;
//
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#addVenue(Venue)}
//     */
//    @Test
//    void testAddVenue() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Food Type");
//        venue.setLandmark("Landmark");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Capacity");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Name");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        Venue venue1 = new Venue();
//        venue1.setAvailable(true);
//        venue1.setCuisine(new ArrayList<>());
//        venue1.setFoodType("Food Type");
//        venue1.setLandmark("Landmark");
//        venue1.setRoomDetails(new ArrayList<>());
//        venue1.setVenueCapacity("Venue Capacity");
//        venue1.setVenueFacilities(new ArrayList<>());
//        venue1.setVenueId(123);
//        venue1.setVenueName("Venue Name");
//        venue1.setVenuePrice(1);
//        venue1.setVenueRating(1);
//        venue1.setVenueSpaceType(new ArrayList<>());
//        venue1.setVerified(true);
//        Optional<Venue> ofResult = Optional.of(venue1);
//        when(recommendationRepository.save((Venue) org.mockito.Mockito.any())).thenReturn(venue);
//        when(recommendationRepository.findById((Integer) org.mockito.Mockito.any())).thenReturn(ofResult);
//
//        Venue venue2 = new Venue();
//        venue2.setAvailable(true);
//        venue2.setCuisine(new ArrayList<>());
//        venue2.setFoodType("Food Type");
//        venue2.setLandmark("Landmark");
//        venue2.setRoomDetails(new ArrayList<>());
//        venue2.setVenueCapacity("Venue Capacity");
//        venue2.setVenueFacilities(new ArrayList<>());
//        venue2.setVenueId(123);
//        venue2.setVenueName("Venue Name");
//        venue2.setVenuePrice(1);
//        venue2.setVenueRating(1);
//        venue2.setVenueSpaceType(new ArrayList<>());
//        venue2.setVerified(true);
//        assertThrows(VenueAlreadyExistException.class, () -> recommendationServiceImpl.addVenue(venue2));
//        verify(recommendationRepository).findById((Integer) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#addVenue(Venue)}
//     */
//    @Test
//    @Disabled("TODO: Complete this test")
//    void testAddVenue2() {
//        // TODO: Complete this test.
//        //   Reason: R013 No inputs found that don't throw a trivial exception.
//        //   Diffblue Cover tried to run the arrange/act section, but the method under
//        //   test threw
//        //   java.lang.NullPointerException
//        //       at com.RecomendationService.service.RecommendationServiceImpl.addVenue(RecommendationServiceImpl.java:19)
//        //   In order to prevent addVenue(Venue)
//        //   from throwing NullPointerException, add constructors or factory
//        //   methods that make it easier to construct fully initialized objects used in
//        //   addVenue(Venue).
//        //   See https://diff.blue/R013 to resolve this issue.
//
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Food Type");
//        venue.setLandmark("Landmark");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Capacity");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Name");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//        when(recommendationRepository.save((Venue) org.mockito.Mockito.any())).thenReturn(venue);
//        when(recommendationRepository.findById((Integer) org.mockito.Mockito.any())).thenReturn(null);
//
//        Venue venue1 = new Venue();
//        venue1.setAvailable(true);
//        venue1.setCuisine(new ArrayList<>());
//        venue1.setFoodType("Food Type");
//        venue1.setLandmark("Landmark");
//        venue1.setRoomDetails(new ArrayList<>());
//        venue1.setVenueCapacity("Venue Capacity");
//        venue1.setVenueFacilities(new ArrayList<>());
//        venue1.setVenueId(123);
//        venue1.setVenueName("Venue Name");
//        venue1.setVenuePrice(1);
//        venue1.setVenueRating(1);
//        venue1.setVenueSpaceType(new ArrayList<>());
//        venue1.setVerified(true);
//        recommendationServiceImpl.addVenue(venue1);
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#addVenue(Venue)}
//     */
//    @Test
//    void testAddVenue3() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Food Type");
//        venue.setLandmark("Landmark");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Capacity");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Name");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//        when(recommendationRepository.save((Venue) org.mockito.Mockito.any())).thenReturn(venue);
//        when(recommendationRepository.findById((Integer) org.mockito.Mockito.any())).thenReturn(Optional.empty());
//
//        Venue venue1 = new Venue();
//        venue1.setAvailable(true);
//        venue1.setCuisine(new ArrayList<>());
//        venue1.setFoodType("Food Type");
//        venue1.setLandmark("Landmark");
//        venue1.setRoomDetails(new ArrayList<>());
//        venue1.setVenueCapacity("Venue Capacity");
//        venue1.setVenueFacilities(new ArrayList<>());
//        venue1.setVenueId(123);
//        venue1.setVenueName("Venue Name");
//        venue1.setVenuePrice(1);
//        venue1.setVenueRating(1);
//        venue1.setVenueSpaceType(new ArrayList<>());
//        venue1.setVerified(true);
//        assertSame(venue, recommendationServiceImpl.addVenue(venue1));
//        verify(recommendationRepository).save((Venue) org.mockito.Mockito.any());
//        verify(recommendationRepository).findById((Integer) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#addVenue(Venue)}
//     */
//    @Test
//    void testAddVenue4() {
//        when(recommendationRepository.save((Venue) org.mockito.Mockito.any()))
//                .thenThrow(new VenueNotFoundException("An error occurred"));
//        when(recommendationRepository.findById((Integer) org.mockito.Mockito.any()))
//                .thenThrow(new VenueNotFoundException("An error occurred"));
//
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Food Type");
//        venue.setLandmark("Landmark");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Capacity");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Name");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.addVenue(venue));
//        verify(recommendationRepository).findById((Integer) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#addVenue(Venue)}
//     */
//    @Test
//    void testAddVenue5() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Food Type");
//        venue.setLandmark("Landmark");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Capacity");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Name");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        Venue venue1 = new Venue();
//        venue1.setAvailable(true);
//        venue1.setCuisine(new ArrayList<>());
//        venue1.setFoodType("Food Type");
//        venue1.setLandmark("Landmark");
//        venue1.setRoomDetails(new ArrayList<>());
//        venue1.setVenueCapacity("Venue Capacity");
//        venue1.setVenueFacilities(new ArrayList<>());
//        venue1.setVenueId(123);
//        venue1.setVenueName("Venue Name");
//        venue1.setVenuePrice(1);
//        venue1.setVenueRating(1);
//        venue1.setVenueSpaceType(new ArrayList<>());
//        venue1.setVerified(true);
//        Optional<Venue> ofResult = Optional.of(venue1);
//        when(recommendationRepository.save((Venue) org.mockito.Mockito.any())).thenReturn(venue);
//        when(recommendationRepository.findById((Integer) org.mockito.Mockito.any())).thenReturn(ofResult);
//
//        Venue venue2 = new Venue();
//        venue2.setAvailable(true);
//        venue2.setCuisine(new ArrayList<>());
//        venue2.setFoodType("Food Type");
//        venue2.setLandmark("Landmark");
//        venue2.setRoomDetails(new ArrayList<>());
//        venue2.setVenueCapacity("Venue Capacity");
//        venue2.setVenueFacilities(new ArrayList<>());
//        venue2.setVenueId(123);
//        venue2.setVenueName("Venue Name");
//        venue2.setVenuePrice(1);
//        venue2.setVenueRating(1);
//        venue2.setVenueSpaceType(new ArrayList<>());
//        venue2.setVerified(true);
//        assertThrows(VenueAlreadyExistException.class, () -> recommendationServiceImpl.addVenue(venue2));
//        verify(recommendationRepository).findById((Integer) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#addVenue(Venue)}
//     */
//    @Test
//    @Disabled("TODO: Complete this test")
//    void testAddVenue6() {
//        // TODO: Complete this test.
//        //   Reason: R013 No inputs found that don't throw a trivial exception.
//        //   Diffblue Cover tried to run the arrange/act section, but the method under
//        //   test threw
//        //   java.lang.NullPointerException
//        //       at com.RecomendationService.service.RecommendationServiceImpl.addVenue(RecommendationServiceImpl.java:21)
//        //   In order to prevent addVenue(Venue)
//        //   from throwing NullPointerException, add constructors or factory
//        //   methods that make it easier to construct fully initialized objects used in
//        //   addVenue(Venue).
//        //   See https://diff.blue/R013 to resolve this issue.
//
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Food Type");
//        venue.setLandmark("Landmark");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Capacity");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Name");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//        when(recommendationRepository.save((Venue) org.mockito.Mockito.any())).thenReturn(venue);
//        when(recommendationRepository.findById((Integer) org.mockito.Mockito.any())).thenReturn(null);
//
//        Venue venue1 = new Venue();
//        venue1.setAvailable(true);
//        venue1.setCuisine(new ArrayList<>());
//        venue1.setFoodType("Food Type");
//        venue1.setLandmark("Landmark");
//        venue1.setRoomDetails(new ArrayList<>());
//        venue1.setVenueCapacity("Venue Capacity");
//        venue1.setVenueFacilities(new ArrayList<>());
//        venue1.setVenueId(123);
//        venue1.setVenueName("Venue Name");
//        venue1.setVenuePrice(1);
//        venue1.setVenueRating(1);
//        venue1.setVenueSpaceType(new ArrayList<>());
//        venue1.setVerified(true);
//        recommendationServiceImpl.addVenue(venue1);
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#addVenue(Venue)}
//     */
//    @Test
//    void testAddVenue7() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Food Type");
//        venue.setLandmark("Landmark");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Capacity");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Name");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//        when(recommendationRepository.save((Venue) org.mockito.Mockito.any())).thenReturn(venue);
//        when(recommendationRepository.findById((Integer) org.mockito.Mockito.any())).thenReturn(Optional.empty());
//
//        Venue venue1 = new Venue();
//        venue1.setAvailable(true);
//        venue1.setCuisine(new ArrayList<>());
//        venue1.setFoodType("Food Type");
//        venue1.setLandmark("Landmark");
//        venue1.setRoomDetails(new ArrayList<>());
//        venue1.setVenueCapacity("Venue Capacity");
//        venue1.setVenueFacilities(new ArrayList<>());
//        venue1.setVenueId(123);
//        venue1.setVenueName("Venue Name");
//        venue1.setVenuePrice(1);
//        venue1.setVenueRating(1);
//        venue1.setVenueSpaceType(new ArrayList<>());
//        venue1.setVerified(true);
//        assertSame(venue, recommendationServiceImpl.addVenue(venue1));
//        verify(recommendationRepository).save((Venue) org.mockito.Mockito.any());
//        verify(recommendationRepository).findById((Integer) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#addVenue(Venue)}
//     */
//    @Test
//    void testAddVenue8() {
//        when(recommendationRepository.save((Venue) org.mockito.Mockito.any()))
//                .thenThrow(new VenueNotFoundException("An error occurred"));
//        when(recommendationRepository.findById((Integer) org.mockito.Mockito.any()))
//                .thenThrow(new VenueNotFoundException("An error occurred"));
//
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Food Type");
//        venue.setLandmark("Landmark");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Capacity");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Name");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.addVenue(venue));
//        verify(recommendationRepository).findById((Integer) org.mockito.Mockito.any());
//    }
//
//    @Test
//    void testListVenues() {
//        ArrayList<Venue> venueList = new ArrayList<>();
//        when(recommendationRepository.getVenue((String) any())).thenReturn(venueList);
//        List<Venue> actualListVenuesResult = recommendationServiceImpl.listVenues("Query");
//        assertSame(venueList, actualListVenuesResult);
//        assertTrue(actualListVenuesResult.isEmpty());
//        verify(recommendationRepository).getVenue((String) any());
//    }
//
//
//    @Test
//    void testListVenues2() {
//        when(recommendationRepository.getVenue((String) any()))
//                .thenThrow(new VenueNotFoundException("An error occurred"));
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.listVenues("Query"));
//        verify(recommendationRepository).getVenue((String) any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenues(String)}
//     */
//    @Test
//    void testListVenues3() {
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any()))
//                .thenThrow(new VenueAlreadyExistException("An error occurred"));
//        assertThrows(VenueAlreadyExistException.class, () -> recommendationServiceImpl.listVenues("Query"));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenues(String)}
//     */
//    @Test
//    void testListVenues4() {
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any())).thenReturn(new ArrayList<>());
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.listVenues("Query"));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenues(String)}
//     */
//    @Test
//    void testListVenues5() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Venue Not Found");
//        venue.setLandmark("Venue Not Found");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Not Found");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Not Found");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        ArrayList<Venue> venueList = new ArrayList<>();
//        venueList.add(venue);
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any())).thenReturn(venueList);
//        List<Venue> actualListVenuesResult = recommendationServiceImpl.listVenues("Query");
//        assertSame(venueList, actualListVenuesResult);
//        assertEquals(1, actualListVenuesResult.size());
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenues(String)}
//     */
//    @Test
//    void testListVenues6() {
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any()))
//                .thenThrow(new VenueAlreadyExistException("An error occurred"));
//        assertThrows(VenueAlreadyExistException.class, () -> recommendationServiceImpl.listVenues("Query"));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//
//    @Test
//    void testListVenueByVenueName() {
//        ArrayList<Venue> venueList = new ArrayList<>();
//        when(recommendationRepository.getByVenueName((String) any())).thenReturn(venueList);
//        List<Venue> actualListVenueByVenueNameResult = recommendationServiceImpl.listVenueByVenueName("Venue Name");
//        assertSame(venueList, actualListVenueByVenueNameResult);
//        assertTrue(actualListVenueByVenueNameResult.isEmpty());
//        verify(recommendationRepository).getByVenueName((String) any());
//    }
//
//
//    @Test
//    void testListVenueByVenueName2() {
//        when(recommendationRepository.getByVenueName((String) any()))
//                .thenThrow(new VenueNotFoundException("An error occurred"));
//        assertThrows(VenueNotFoundException.class,
//                () -> recommendationServiceImpl.listVenueByVenueName("Venue Name"));
//        verify(recommendationRepository).getByVenueName((String) any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenueByVenueName(String)}
//     */
//    @Test
//    void testListVenueByVenueName3() {
//        when(recommendationRepository.getByVenueName((String) org.mockito.Mockito.any()))
//                .thenThrow(new VenueAlreadyExistException("An error occurred"));
//        assertThrows(VenueAlreadyExistException.class,
//                () -> recommendationServiceImpl.listVenueByVenueName("Venue Name"));
//        verify(recommendationRepository).getByVenueName((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenueByVenueName(String)}
//     */
//    @Test
//    void testListVenueByVenueName4() {
//        when(recommendationRepository.getByVenueName((String) org.mockito.Mockito.any())).thenReturn(new ArrayList<>());
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.listVenueByVenueName("Venue Name"));
//        verify(recommendationRepository).getByVenueName((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenueByVenueName(String)}
//     */
//    @Test
//    void testListVenueByVenueName5() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Venue Not Found");
//        venue.setLandmark("Venue Not Found");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Not Found");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Not Found");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        ArrayList<Venue> venueList = new ArrayList<>();
//        venueList.add(venue);
//        when(recommendationRepository.getByVenueName((String) org.mockito.Mockito.any())).thenReturn(venueList);
//        List<Venue> actualListVenueByVenueNameResult = recommendationServiceImpl.listVenueByVenueName("Venue Name");
//        assertSame(venueList, actualListVenueByVenueNameResult);
//        assertEquals(1, actualListVenueByVenueNameResult.size());
//        verify(recommendationRepository).getByVenueName((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenueByVenueName(String)}
//     */
//    @Test
//    void testListVenueByVenueName6() {
//        when(recommendationRepository.getByVenueName((String) org.mockito.Mockito.any()))
//                .thenThrow(new VenueAlreadyExistException("An error occurred"));
//        assertThrows(VenueAlreadyExistException.class,
//                () -> recommendationServiceImpl.listVenueByVenueName("Venue Name"));
//        verify(recommendationRepository).getByVenueName((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#findByPriceAndCapacity(Integer, String)}
//     */
//    @Test
//    void testFindByPriceAndCapacity() {
//        ArrayList<Venue> venueList = new ArrayList<>();
//        when(recommendationRepository.findByPriceAndCapacity(anyInt(), (String) org.mockito.Mockito.any()))
//                .thenReturn(venueList);
//        List<Venue> actualFindByPriceAndCapacityResult = recommendationServiceImpl.findByPriceAndCapacity(1,
//                "Venue Capacity");
//        assertSame(venueList, actualFindByPriceAndCapacityResult);
//        assertTrue(actualFindByPriceAndCapacityResult.isEmpty());
//        verify(recommendationRepository).findByPriceAndCapacity(anyInt(), (String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#findByPriceAndCapacity(Integer, String)}
//     */
//    @Test
//    @Disabled("TODO: Complete this test")
//    void testFindByPriceAndCapacity2() {
//        // TODO: Complete this test.
//        //   Reason: R013 No inputs found that don't throw a trivial exception.
//        //   Diffblue Cover tried to run the arrange/act section, but the method under
//        //   test threw
//        //   java.lang.NullPointerException
//        //       at com.RecomendationService.service.RecommendationServiceImpl.findByPriceAndCapacity(RecommendationServiceImpl.java:50)
//        //   In order to prevent findByPriceAndCapacity(Integer, String)
//        //   from throwing NullPointerException, add constructors or factory
//        //   methods that make it easier to construct fully initialized objects used in
//        //   findByPriceAndCapacity(Integer, String).
//        //   See https://diff.blue/R013 to resolve this issue.
//
//        when(recommendationRepository.findByPriceAndCapacity(anyInt(), (String) org.mockito.Mockito.any()))
//                .thenReturn(new ArrayList<>());
//        recommendationServiceImpl.findByPriceAndCapacity(null, "Venue Capacity");
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#findByPriceAndCapacity(Integer, String)}
//     */
//    @Test
//    void testFindByPriceAndCapacity3() {
//        when(recommendationRepository.findByPriceAndCapacity(anyInt(), (String) org.mockito.Mockito.any()))
//                .thenThrow(new VenueAlreadyExistException("An error occurred"));
//        assertThrows(VenueAlreadyExistException.class,
//                () -> recommendationServiceImpl.findByPriceAndCapacity(1, "Venue Capacity"));
//        verify(recommendationRepository).findByPriceAndCapacity(anyInt(), (String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#findByPriceAndCapacity(Integer, String)}
//     */
//    @Test
//    void testFindByPriceAndCapacity4() {
//        when(recommendationRepository.findByPriceAndCapacity(anyInt(), (String) org.mockito.Mockito.any()))
//                .thenReturn(new ArrayList<>());
//        assertThrows(VenueNotFoundException.class,
//                () -> recommendationServiceImpl.findByPriceAndCapacity(1, "Venue Capacity"));
//        verify(recommendationRepository).findByPriceAndCapacity(anyInt(), (String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#findByPriceAndCapacity(Integer, String)}
//     */
//    @Test
//    void testFindByPriceAndCapacity5() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Venue Not Found");
//        venue.setLandmark("Venue Not Found");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Not Found");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Not Found");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        ArrayList<Venue> venueList = new ArrayList<>();
//        venueList.add(venue);
//        when(recommendationRepository.findByPriceAndCapacity(anyInt(), (String) org.mockito.Mockito.any()))
//                .thenReturn(venueList);
//        List<Venue> actualFindByPriceAndCapacityResult = recommendationServiceImpl.findByPriceAndCapacity(1,
//                "Venue Capacity");
//        assertSame(venueList, actualFindByPriceAndCapacityResult);
//        assertEquals(1, actualFindByPriceAndCapacityResult.size());
//        verify(recommendationRepository).findByPriceAndCapacity(anyInt(), (String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#findByPriceAndCapacity(Integer, String)}
//     */
//    @Test
//    void testFindByPriceAndCapacity6() {
//        when(recommendationRepository.findByPriceAndCapacity(anyInt(), (String) org.mockito.Mockito.any()))
//                .thenThrow(new VenueAlreadyExistException("An error occurred"));
//        assertThrows(VenueAlreadyExistException.class,
//                () -> recommendationServiceImpl.findByPriceAndCapacity(1, "Venue Capacity"));
//        verify(recommendationRepository).findByPriceAndCapacity(anyInt(), (String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesFromLowToHigh(String)}
//     */
//    @Test
//    void testListVenuesFromLowToHigh() {
//        ArrayList<Venue> venueList = new ArrayList<>();
//        when(recommendationRepository.findVenueByVenuePriceLowtoHigh((String) org.mockito.Mockito.any()))
//                .thenReturn(venueList);
//        List<Venue> actualListVenuesFromLowToHighResult = recommendationServiceImpl.listVenuesFromLowToHigh("Query");
//        assertSame(venueList, actualListVenuesFromLowToHighResult);
//        assertTrue(actualListVenuesFromLowToHighResult.isEmpty());
//        verify(recommendationRepository).findVenueByVenuePriceLowtoHigh((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesFromLowToHigh(String)}
//     */
//    @Test
//    void testListVenuesFromLowToHigh2() {
//        when(recommendationRepository.findVenueByVenuePriceLowtoHigh((String) org.mockito.Mockito.any()))
//                .thenThrow(new VenueAlreadyExistException("An error occurred"));
//        assertThrows(VenueAlreadyExistException.class, () -> recommendationServiceImpl.listVenuesFromLowToHigh("Query"));
//        verify(recommendationRepository).findVenueByVenuePriceLowtoHigh((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesFromLowToHigh(String)}
//     */
//    @Test
//    void testListVenuesFromLowToHigh3() {
//        when(recommendationRepository.findVenueByVenuePriceLowtoHigh((String) org.mockito.Mockito.any()))
//                .thenReturn(new ArrayList<>());
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.listVenuesFromLowToHigh("Query"));
//        verify(recommendationRepository).findVenueByVenuePriceLowtoHigh((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesFromLowToHigh(String)}
//     */
//    @Test
//    void testListVenuesFromLowToHigh4() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Venue Not Found");
//        venue.setLandmark("Venue Not Found");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Not Found");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Not Found");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        ArrayList<Venue> venueList = new ArrayList<>();
//        venueList.add(venue);
//        when(recommendationRepository.findVenueByVenuePriceLowtoHigh((String) org.mockito.Mockito.any()))
//                .thenReturn(venueList);
//        List<Venue> actualListVenuesFromLowToHighResult = recommendationServiceImpl.listVenuesFromLowToHigh("Query");
//        assertSame(venueList, actualListVenuesFromLowToHighResult);
//        assertEquals(1, actualListVenuesFromLowToHighResult.size());
//        verify(recommendationRepository).findVenueByVenuePriceLowtoHigh((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesFromLowToHigh(String)}
//     */
//    @Test
//    void testListVenuesFromLowToHigh5() {
//        when(recommendationRepository.findVenueByVenuePriceLowtoHigh((String) org.mockito.Mockito.any()))
//                .thenThrow(new VenueAlreadyExistException("An error occurred"));
//        assertThrows(VenueAlreadyExistException.class, () -> recommendationServiceImpl.listVenuesFromLowToHigh("Query"));
//        verify(recommendationRepository).findVenueByVenuePriceLowtoHigh((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesFromHighToLOw(String)}
//     */
//    @Test
//    void testListVenuesFromHighToLOw() {
//        ArrayList<Venue> venueList = new ArrayList<>();
//        when(recommendationRepository.findVenueByVenuePriceHighToLow((String) org.mockito.Mockito.any()))
//                .thenReturn(venueList);
//        List<Venue> actualListVenuesFromHighToLOwResult = recommendationServiceImpl.listVenuesFromHighToLOw("Query");
//        assertSame(venueList, actualListVenuesFromHighToLOwResult);
//        assertTrue(actualListVenuesFromHighToLOwResult.isEmpty());
//        verify(recommendationRepository).findVenueByVenuePriceHighToLow((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesFromHighToLOw(String)}
//     */
//    @Test
//    void testListVenuesFromHighToLOw2() {
//        when(recommendationRepository.findVenueByVenuePriceHighToLow((String) org.mockito.Mockito.any()))
//                .thenThrow(new VenueAlreadyExistException("An error occurred"));
//        assertThrows(VenueAlreadyExistException.class, () -> recommendationServiceImpl.listVenuesFromHighToLOw("Query"));
//        verify(recommendationRepository).findVenueByVenuePriceHighToLow((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesFromHighToLOw(String)}
//     */
//    @Test
//    void testListVenuesFromHighToLOw3() {
//        when(recommendationRepository.findVenueByVenuePriceHighToLow((String) org.mockito.Mockito.any()))
//                .thenReturn(new ArrayList<>());
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.listVenuesFromHighToLOw("Query"));
//        verify(recommendationRepository).findVenueByVenuePriceHighToLow((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesFromHighToLOw(String)}
//     */
//    @Test
//    void testListVenuesFromHighToLOw4() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Venue Not Found");
//        venue.setLandmark("Venue Not Found");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Not Found");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Not Found");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        ArrayList<Venue> venueList = new ArrayList<>();
//        venueList.add(venue);
//        when(recommendationRepository.findVenueByVenuePriceHighToLow((String) org.mockito.Mockito.any()))
//                .thenReturn(venueList);
//        List<Venue> actualListVenuesFromHighToLOwResult = recommendationServiceImpl.listVenuesFromHighToLOw("Query");
//        assertSame(venueList, actualListVenuesFromHighToLOwResult);
//        assertEquals(1, actualListVenuesFromHighToLOwResult.size());
//        verify(recommendationRepository).findVenueByVenuePriceHighToLow((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesFromHighToLOw(String)}
//     */
//    @Test
//    void testListVenuesFromHighToLOw5() {
//        when(recommendationRepository.findVenueByVenuePriceHighToLow((String) org.mockito.Mockito.any()))
//                .thenThrow(new VenueAlreadyExistException("An error occurred"));
//        assertThrows(VenueAlreadyExistException.class, () -> recommendationServiceImpl.listVenuesFromHighToLOw("Query"));
//        verify(recommendationRepository).findVenueByVenuePriceHighToLow((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesGreaterThan(String, int)}
//     */
//    @Test
//    void testListVenuesGreaterThan() {
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any())).thenReturn(new ArrayList<>());
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.listVenuesGreaterThan("Query", 1));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesGreaterThan(String, int)}
//     */
//    @Test
//    void testListVenuesGreaterThan2() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Venue Not Found");
//        venue.setLandmark("Venue Not Found");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Not Found");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Not Found");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        ArrayList<Venue> venueList = new ArrayList<>();
//        venueList.add(venue);
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any())).thenReturn(venueList);
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.listVenuesGreaterThan("Query", 1));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesGreaterThan(String, int)}
//     */
//    @Test
//    void testListVenuesGreaterThan3() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Venue Not Found");
//        venue.setLandmark("Venue Not Found");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Not Found");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Not Found");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        Venue venue1 = new Venue();
//        venue1.setAvailable(true);
//        venue1.setCuisine(new ArrayList<>());
//        venue1.setFoodType("Venue Not Found");
//        venue1.setLandmark("Venue Not Found");
//        venue1.setRoomDetails(new ArrayList<>());
//        venue1.setVenueCapacity("Venue Not Found");
//        venue1.setVenueFacilities(new ArrayList<>());
//        venue1.setVenueId(123);
//        venue1.setVenueName("Venue Not Found");
//        venue1.setVenuePrice(1);
//        venue1.setVenueRating(1);
//        venue1.setVenueSpaceType(new ArrayList<>());
//        venue1.setVerified(true);
//
//        ArrayList<Venue> venueList = new ArrayList<>();
//        venueList.add(venue1);
//        venueList.add(venue);
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any())).thenReturn(venueList);
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.listVenuesGreaterThan("Query", 1));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesGreaterThan(String, int)}
//     */
//    @Test
//    void testListVenuesGreaterThan4() {
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any()))
//                .thenThrow(new VenueAlreadyExistException("An error occurred"));
//        assertThrows(VenueAlreadyExistException.class, () -> recommendationServiceImpl.listVenuesGreaterThan("Query", 1));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesGreaterThan(String, int)}
//     */
//    @Test
//    void testListVenuesGreaterThan5() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Venue Not Found");
//        venue.setLandmark("Venue Not Found");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Not Found");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Not Found");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        ArrayList<Venue> venueList = new ArrayList<>();
//        venueList.add(venue);
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any())).thenReturn(venueList);
//        assertEquals(1, recommendationServiceImpl.listVenuesGreaterThan("Query", 0).size());
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesLesserThan(String, int)}
//     */
//    @Test
//    void testListVenuesLesserThan() {
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any())).thenReturn(new ArrayList<>());
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.listVenuesLesserThan("Query", 1));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesLesserThan(String, int)}
//     */
//    @Test
//    void testListVenuesLesserThan2() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Venue Not Found");
//        venue.setLandmark("Venue Not Found");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Not Found");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Not Found");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        ArrayList<Venue> venueList = new ArrayList<>();
//        venueList.add(venue);
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any())).thenReturn(venueList);
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.listVenuesLesserThan("Query", 1));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesLesserThan(String, int)}
//     */
//    @Test
//    void testListVenuesLesserThan3() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Venue Not Found");
//        venue.setLandmark("Venue Not Found");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Not Found");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Not Found");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        Venue venue1 = new Venue();
//        venue1.setAvailable(true);
//        venue1.setCuisine(new ArrayList<>());
//        venue1.setFoodType("Venue Not Found");
//        venue1.setLandmark("Venue Not Found");
//        venue1.setRoomDetails(new ArrayList<>());
//        venue1.setVenueCapacity("Venue Not Found");
//        venue1.setVenueFacilities(new ArrayList<>());
//        venue1.setVenueId(123);
//        venue1.setVenueName("Venue Not Found");
//        venue1.setVenuePrice(1);
//        venue1.setVenueRating(1);
//        venue1.setVenueSpaceType(new ArrayList<>());
//        venue1.setVerified(true);
//
//        ArrayList<Venue> venueList = new ArrayList<>();
//        venueList.add(venue1);
//        venueList.add(venue);
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any())).thenReturn(venueList);
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.listVenuesLesserThan("Query", 1));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesLesserThan(String, int)}
//     */
//    @Test
//    void testListVenuesLesserThan4() {
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any()))
//                .thenThrow(new VenueAlreadyExistException("An error occurred"));
//        assertThrows(VenueAlreadyExistException.class, () -> recommendationServiceImpl.listVenuesLesserThan("Query", 1));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesLesserThan(String, int)}
//     */
//    @Test
//    void testListVenuesLesserThan5() {
//        Venue venue = mock(Venue.class);
//        when(venue.getVenuePrice()).thenReturn(0);
//        doNothing().when(venue).setAvailable(anyBoolean());
//        doNothing().when(venue).setCuisine((List<String>) org.mockito.Mockito.any());
//        doNothing().when(venue).setFoodType((String) org.mockito.Mockito.any());
//        doNothing().when(venue).setLandmark((String) org.mockito.Mockito.any());
//        doNothing().when(venue).setRoomDetails((List<Room>) org.mockito.Mockito.any());
//        doNothing().when(venue).setVenueCapacity((String) org.mockito.Mockito.any());
//        doNothing().when(venue).setVenueFacilities((List<String>) org.mockito.Mockito.any());
//        doNothing().when(venue).setVenueId(anyInt());
//        doNothing().when(venue).setVenueName((String) org.mockito.Mockito.any());
//        doNothing().when(venue).setVenuePrice((Integer) org.mockito.Mockito.any());
//        doNothing().when(venue).setVenueRating(anyInt());
//        doNothing().when(venue).setVenueSpaceType((List<String>) org.mockito.Mockito.any());
//        doNothing().when(venue).setVerified(anyBoolean());
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Venue Not Found");
//        venue.setLandmark("Venue Not Found");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Not Found");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Not Found");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        ArrayList<Venue> venueList = new ArrayList<>();
//        venueList.add(venue);
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any())).thenReturn(venueList);
//        assertEquals(1, recommendationServiceImpl.listVenuesLesserThan("Query", 1).size());
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//        verify(venue).getVenuePrice();
//        verify(venue).setAvailable(anyBoolean());
//        verify(venue).setCuisine((List<String>) org.mockito.Mockito.any());
//        verify(venue).setFoodType((String) org.mockito.Mockito.any());
//        verify(venue).setLandmark((String) org.mockito.Mockito.any());
//        verify(venue).setRoomDetails((List<Room>) org.mockito.Mockito.any());
//        verify(venue).setVenueCapacity((String) org.mockito.Mockito.any());
//        verify(venue).setVenueFacilities((List<String>) org.mockito.Mockito.any());
//        verify(venue).setVenueId(anyInt());
//        verify(venue).setVenueName((String) org.mockito.Mockito.any());
//        verify(venue).setVenuePrice((Integer) org.mockito.Mockito.any());
//        verify(venue).setVenueRating(anyInt());
//        verify(venue).setVenueSpaceType((List<String>) org.mockito.Mockito.any());
//        verify(venue).setVerified(anyBoolean());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesInBetween(String, int, int)}
//     */
//    @Test
//    void testListVenuesInBetween() {
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any())).thenReturn(new ArrayList<>());
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.listVenuesInBetween("Query", 1, 1));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesInBetween(String, int, int)}
//     */
//    @Test
//    void testListVenuesInBetween2() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Venue Not Found");
//        venue.setLandmark("Venue Not Found");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Not Found");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Not Found");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        ArrayList<Venue> venueList = new ArrayList<>();
//        venueList.add(venue);
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any())).thenReturn(venueList);
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.listVenuesInBetween("Query", 1, 1));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesInBetween(String, int, int)}
//     */
//    @Test
//    void testListVenuesInBetween3() {
//        Venue venue = new Venue();
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Venue Not Found");
//        venue.setLandmark("Venue Not Found");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Not Found");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Not Found");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        Venue venue1 = new Venue();
//        venue1.setAvailable(true);
//        venue1.setCuisine(new ArrayList<>());
//        venue1.setFoodType("Venue Not Found");
//        venue1.setLandmark("Venue Not Found");
//        venue1.setRoomDetails(new ArrayList<>());
//        venue1.setVenueCapacity("Venue Not Found");
//        venue1.setVenueFacilities(new ArrayList<>());
//        venue1.setVenueId(123);
//        venue1.setVenueName("Venue Not Found");
//        venue1.setVenuePrice(1);
//        venue1.setVenueRating(1);
//        venue1.setVenueSpaceType(new ArrayList<>());
//        venue1.setVerified(true);
//
//        ArrayList<Venue> venueList = new ArrayList<>();
//        venueList.add(venue1);
//        venueList.add(venue);
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any())).thenReturn(venueList);
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.listVenuesInBetween("Query", 1, 1));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesInBetween(String, int, int)}
//     */
//    @Test
//    void testListVenuesInBetween4() {
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any()))
//                .thenThrow(new VenueAlreadyExistException("An error occurred"));
//        assertThrows(VenueAlreadyExistException.class,
//                () -> recommendationServiceImpl.listVenuesInBetween("Query", 1, 1));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//    }
//
//    /**
//     * Method under test: {@link RecommendationServiceImpl#listVenuesInBetween(String, int, int)}
//     */
//    @Test
//    void testListVenuesInBetween5() {
//        Venue venue = mock(Venue.class);
//        when(venue.getVenuePrice()).thenReturn(0);
//        doNothing().when(venue).setAvailable(anyBoolean());
//        doNothing().when(venue).setCuisine((List<String>) org.mockito.Mockito.any());
//        doNothing().when(venue).setFoodType((String) org.mockito.Mockito.any());
//        doNothing().when(venue).setLandmark((String) org.mockito.Mockito.any());
//        doNothing().when(venue).setRoomDetails((List<Room>) org.mockito.Mockito.any());
//        doNothing().when(venue).setVenueCapacity((String) org.mockito.Mockito.any());
//        doNothing().when(venue).setVenueFacilities((List<String>) org.mockito.Mockito.any());
//        doNothing().when(venue).setVenueId(anyInt());
//        doNothing().when(venue).setVenueName((String) org.mockito.Mockito.any());
//        doNothing().when(venue).setVenuePrice((Integer) org.mockito.Mockito.any());
//        doNothing().when(venue).setVenueRating(anyInt());
//        doNothing().when(venue).setVenueSpaceType((List<String>) org.mockito.Mockito.any());
//        doNothing().when(venue).setVerified(anyBoolean());
//        venue.setAvailable(true);
//        venue.setCuisine(new ArrayList<>());
//        venue.setFoodType("Venue Not Found");
//        venue.setLandmark("Venue Not Found");
//        venue.setRoomDetails(new ArrayList<>());
//        venue.setVenueCapacity("Venue Not Found");
//        venue.setVenueFacilities(new ArrayList<>());
//        venue.setVenueId(123);
//        venue.setVenueName("Venue Not Found");
//        venue.setVenuePrice(1);
//        venue.setVenueRating(1);
//        venue.setVenueSpaceType(new ArrayList<>());
//        venue.setVerified(true);
//
//        ArrayList<Venue> venueList = new ArrayList<>();
//        venueList.add(venue);
//        when(recommendationRepository.getVenue((String) org.mockito.Mockito.any())).thenReturn(venueList);
//        assertThrows(VenueNotFoundException.class, () -> recommendationServiceImpl.listVenuesInBetween("Query", 1, 1));
//        verify(recommendationRepository).getVenue((String) org.mockito.Mockito.any());
//        verify(venue, atLeast(1)).getVenuePrice();
//        verify(venue).setAvailable(anyBoolean());
//        verify(venue).setCuisine((List<String>) org.mockito.Mockito.any());
//        verify(venue).setFoodType((String) org.mockito.Mockito.any());
//        verify(venue).setLandmark((String) org.mockito.Mockito.any());
//        verify(venue).setRoomDetails((List<Room>) org.mockito.Mockito.any());
//        verify(venue).setVenueCapacity((String) org.mockito.Mockito.any());
//        verify(venue).setVenueFacilities((List<String>) org.mockito.Mockito.any());
//        verify(venue).setVenueId(anyInt());
//        verify(venue).setVenueName((String) org.mockito.Mockito.any());
//        verify(venue).setVenuePrice((Integer) org.mockito.Mockito.any());
//        verify(venue).setVenueRating(anyInt());
//        verify(venue).setVenueSpaceType((List<String>) org.mockito.Mockito.any());
//        verify(venue).setVerified(anyBoolean());
//    }
//}
//
