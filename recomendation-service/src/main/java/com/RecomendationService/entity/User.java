package com.RecomendationService.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;


@Document(collection = "User")
@Data
public class User {
	
	@Transient
	public static final String SEQUENCE_NAME = "booking_sequence";

	@Id
	private int userId;
	private String userEmail;
	private String city;

}
