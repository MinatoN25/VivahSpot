package com.authenticationservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.authenticationservice.models.User;
import com.authenticationservice.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
    User user = userRepository.findByUserEmail(userEmail)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with useremail: " + userEmail));

    return UserDetailsImpl.build(user);
  }

}
