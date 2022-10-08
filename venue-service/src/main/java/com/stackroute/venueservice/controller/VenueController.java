package com.stackroute.venueservice.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.venueservice.constants.VenueConstants;
import com.stackroute.venueservice.exception.VenueAlreadyExistException;
import com.stackroute.venueservice.exception.VenueNotFoundException;
import com.stackroute.venueservice.model.Venue;
import com.stackroute.venueservice.service.VenueService;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(path = VenueConstants.ROOT_PATH)
@Api(value = "API to search Venue from a Venue Repository by different search parameters",
description = "This API provides the capability to search Venue from a Venue Repository", produces = "application/json")
@ApiResponses(value = {
		  @ApiResponse(code = 200, message = "Successfully retrieved"),
		  @ApiResponse(code = 404, message = "Not found - The venue was not found")
		})
public class VenueController {

	private VenueService venueService;


	@Autowired
	public VenueController(VenueService venueService) {
		this.venueService = venueService;
	}

	
	@ApiOperation(value = "Create venue by providing required details", produces = "application/json")
	@PostMapping(value = "/registerVenue")
	public ResponseEntity<?> create(@RequestBody Venue venue, HttpSession session) {
		try {
			if(venue.getVenueName() == null || venue.getCity() == null || venue.getVenueCapacity() == 0 || venue.getFoodType() ==null  || venue.getVenuePrice() == 0 || venue.getSlots() == null || venue.getVenueOwnerEmail() == null || venue.getCurrency() == null)  {
				return new ResponseEntity<>(VenueConstants.VENUE_INFO_MSG, HttpStatus.BAD_GATEWAY);
			}
			
			venueService.addVenue(venue);
		}
		catch(VenueAlreadyExistException venueExist) {
			return new ResponseEntity<String>("Venue Exists Already", HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Venue>(venue, HttpStatus.CREATED);

	}

	


	@ApiOperation(value = "Update venue details by providing venue id", produces = "application/json")
	@PutMapping(value = "/updateVenue")
	public ResponseEntity<?> update(@RequestBody Venue venue, HttpSession session) {
		try {
			return new ResponseEntity<Venue>(venueService.updateVenue(venue), HttpStatus.CREATED);
		
		} catch (VenueNotFoundException venueNotFound) {
			return new ResponseEntity<String>("Venue Not Found", HttpStatus.NOT_FOUND);
		}
	}
	
	@ApiOperation(value = "Delete venue details by id", produces = "application/json")
	@DeleteMapping(value="/deleteVenue/{venueId}")
	public ResponseEntity<?> delete(@PathVariable int venueId,HttpSession session){
		ResponseEntity<?> response;
		try {
			response = new ResponseEntity<>(venueService.deleteVenue(venueId), HttpStatus.OK);
		} catch (VenueNotFoundException venueNotFound){
			return new ResponseEntity<String>("Venue Not Found", HttpStatus.NOT_FOUND);
		}
		return response;
	}
	
	@ApiOperation(value = "Get venue details by id", produces = "application/json")
	@GetMapping(value = "/getVenueById/{venueId}")
	public ResponseEntity<?> getVenueById(@PathVariable int venueId,HttpSession session){
		ResponseEntity<?> response;
		try {
			response = new ResponseEntity<>(venueService.getVenueById(venueId), HttpStatus.OK);
			
		} catch (VenueNotFoundException venueNotFound){
			return new ResponseEntity<String>("Venue Not Found", HttpStatus.NOT_FOUND);
		}
		return response;
	}
	@ApiOperation(value = "Get all venues details", produces = "application/json")
	@GetMapping("/getAllVenues")
	public ResponseEntity<?> fetchAllVenues(HttpSession session) {
		ResponseEntity<?> response;
		try {
			response = new ResponseEntity<>(venueService.getAllVenues(), HttpStatus.OK);
			
		} catch (VenueNotFoundException venueNotFound){
			return new ResponseEntity<String>("Venue Not Found", HttpStatus.NOT_FOUND);
		}
		return response;
	

		}

	@GetMapping("/getVenueByName/{venueName}")
    public ResponseEntity<?> getVenueByVenueName(@PathVariable("venueName") String venueName){
        List<Venue> venues =venueService.listVenuesByName(venueName);
        return new ResponseEntity<>(venues, HttpStatus.OK);

    }
	
	@PutMapping(value = "/add/Rating")
	public ResponseEntity<?> giveRatingToBookedVenue(@PathVariable("venueId") int venueId,@PathVariable("venueRating") int venueRating,@RequestAttribute(name="claims") Claims claims) {
		try {
			return new ResponseEntity<Venue>(venueService.giveRatingToBookedVenue(venueId,venueRating, claims.getSubject()), HttpStatus.CREATED);
		
		} catch (VenueNotFoundException venueNotFound) {
			return new ResponseEntity<String>("Venue Not Found", HttpStatus.NOT_FOUND);
		}
	}

}

	

