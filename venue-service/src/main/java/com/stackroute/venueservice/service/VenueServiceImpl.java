package com.stackroute.venueservice.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.stackroute.venueservice.exception.BookingNotFoundException;
import com.stackroute.venueservice.exception.VenueAlreadyExistException;
import com.stackroute.venueservice.exception.VenueNotBookedException;
import com.stackroute.venueservice.exception.VenueNotFoundException;
import com.stackroute.venueservice.model.Booking;
import com.stackroute.venueservice.model.DatabaseSequence;
import com.stackroute.venueservice.model.Status;
import com.stackroute.venueservice.model.Venue;
import com.stackroute.venueservice.repository.BookingRepository;
import com.stackroute.venueservice.repository.VenueRepository;

@Service
public class VenueServiceImpl implements VenueService {

	private VenueRepository venueRepo;
	
	@Autowired
	private BookingRepository bookingRepo;
	
	@Autowired
	private MongoOperations mongo;
	
	public VenueServiceImpl() {
		super();
	}

	@Autowired
	public VenueServiceImpl(VenueRepository venueRepo) {
		this.venueRepo = venueRepo;
	}

	@Override
	public Venue addVenue(Venue venue) throws VenueAlreadyExistException {
		Venue existingVenue = venueRepo.findById(venue.getVenueId()).orElse(null);
		if (existingVenue == null) {
			venue.setVenueId(getNextSequence(Venue.SEQUENCE_NAME));
			return venueRepo.save(venue);	
		}
		else {
			throw new VenueAlreadyExistException("Venue Already Exist Exception !!");
		}
		}


	public Venue updateVenue(Venue venue) throws VenueNotFoundException {
		Venue existingVenue = venueRepo.findById(venue.getVenueId()).orElse(null);
		if (existingVenue == null) {
			throw new VenueNotFoundException("Venue Not Found !!");
		} else {
			List<Venue> venues = venueRepo.findAll();
			for (Venue venue2 : venues) {
				if (venue2.getVenueId() == venue.getVenueId()) {
					venueRepo.save(venue);
				}
			}
			return venue;
		}

	}

	public Optional<Venue> deleteVenue(int venueId) throws VenueNotFoundException {
		Optional<Venue> venue = venueRepo.findById(venueId);
		if (venue.isPresent()) {
			venueRepo.deleteById(venueId);
			return venue;
		} else {
			throw new VenueNotFoundException("Venue Not Found");

		}
	}

	public Optional<Venue> getVenueById(int venueId) throws VenueNotFoundException {
		Optional<Venue> venue = venueRepo.findById(venueId);
		if (venue.isPresent()) {
			venueRepo.findById(venueId);
			return venue;
		} else {
			throw new VenueNotFoundException("Venue Not Found");

		}
	}

	public List<Venue> getAllVenues() throws VenueNotFoundException{
		List<Venue> venue = venueRepo.findAll();
		if(venue.isEmpty()) {
			throw new VenueNotFoundException("Venue Not Found");
		} else {
			venueRepo.findAll();
		}
		return venue;
	}
	
	@Override
	public List<Venue> listVenuesByName(String query) {
		List<Venue> venues = null;

		venues = venueRepo.findByName(query);

		return venues;
	}
	
	@Override
	public  Venue giveRatingToBookedVenue(int venueId, int venueRating, String userEmail) {
		List<Booking> booking1 = bookingRepo.getBookingByUserEmail(userEmail);
		Optional<Venue> venue=venueRepo.findById(venueId);
		if(venue.isEmpty()) {
			throw new VenueNotFoundException("Venue Not Found");
		}
		boolean flag =  false;
		if(booking1.isEmpty()) {
			throw new BookingNotFoundException("Booking Not Found");
		}
		else {
			for (Booking booking : booking1) {
				if(booking.getVenueId() ==venueId && booking.getSlot().getSlotStatus().equals(Status.BOOKED)) {
							venue.get().setVenueRating(venueRating);
							venueRepo.save(venue.get());
							flag = true;
				}
			}
			if(!flag) {
				throw new VenueNotBookedException("You are not allowed to rate this venue as venue not booked by you");
			}
			return venue.get();
		}
	}
	
	public int getNextSequence(String seqName) {
		DatabaseSequence counter = mongo.findAndModify(query(where("_id").is(seqName)), new Update().inc("seq", 1),
				options().returnNew(true).upsert(true), DatabaseSequence.class);
		return counter.getSeq();
	}

}
