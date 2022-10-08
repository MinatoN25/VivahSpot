package com.authenticationservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authenticationservice.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUserEmail(String userEmail);

  Boolean existsByUserEmail(String userEmail);

 
}
