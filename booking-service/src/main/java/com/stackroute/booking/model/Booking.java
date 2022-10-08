package com.stackroute.booking.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "BookingDetails")
public class Booking {

	@Transient
    public static final String SEQUENCE_NAME = "booking_sequence";
	
	@Id
	private int bookingId;
	private int venueId;
	private String venueName;
	private String userEmail;
	private String currency;
	private Double venuePrice;
	@JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class)  
	private LocalDate bookingDate;
	private Integer slotId;
	private Slot slot;
	private String venueOwnerEmail;
	private double refundAmount;

}
