package com.RecomendationService.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

	@Id
	private int roomId;
	private String roomType;
	private int roomPrice;
	private List<String> roomFacilities;

}
