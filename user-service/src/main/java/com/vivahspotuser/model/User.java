package com.vivahspotuser.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "User")
public class User {

	@Transient
	public static final String SEQUENCE_NAME = "user_sequence";

	@Id
	private int userId;

	private Role role;

	private Gender gender;
	
	private String name;
	
	private String userEmail;
	
	private String password;

	private Long contact;

	private String city;

}
