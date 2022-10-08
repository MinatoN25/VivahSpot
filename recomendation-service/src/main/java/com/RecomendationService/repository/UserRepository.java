package com.RecomendationService.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.RecomendationService.entity.User;

public interface UserRepository extends MongoRepository<User, Integer> {
	
	@Query(value = "{userEmail : ?0}")
	User getuserbyEmail(String userEmail);

}
