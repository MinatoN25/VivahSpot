package com.stackroute.venueservice.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Room {

	@Id
	private int roomId;
	private String roomType;
	private int roomPrice;
	private List<String> roomFacilities;

}
