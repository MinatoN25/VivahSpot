package com.authenticationservice.conrollers;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authenticationservice.jwt.JwtUtils;
import com.authenticationservice.repository.RoleRepository;
import com.authenticationservice.repository.UserRepository;
import com.authenticationservice.request.LoginRequest;
import com.authenticationservice.response.JwtResponse;
import com.authenticationservice.services.UserDetailsImpl;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController{
	 
	
	 @Autowired
	 UserRepository userRepository;
	 
	 @Autowired
	 AuthenticationManager authenticationManager;
	 
	 @Autowired
	 RoleRepository roleRepository;
	 
	 @Autowired
	 PasswordEncoder encoder;
	 
	 @Autowired
	 JwtUtils jwtUtils;
	 
	 @PostMapping("/signin")
	  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,HttpSession session) {

	    Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(loginRequest.getUserEmail(), loginRequest.getPassword()));

	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    String jwt = jwtUtils.generateJwtToken(authentication);
	    
	    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
	    List<String> roles = userDetails.getAuthorities().stream()
	        .map(item -> item.getAuthority())
	        .collect(Collectors.toList());
	    session.setAttribute("loggedInUserId", userDetails.getId());
	    return ResponseEntity.ok(new JwtResponse(jwt, 
	                         userDetails.getId(), 
	                         userDetails.getUsername(), 
	                         roles));
	  }
	  
	  @GetMapping("/logout")
	  public ResponseEntity<?> logout(HttpSession session){
			if(session!=null && session.getAttribute("loggedInUserId")!=null) {
				session.invalidate();
				return new ResponseEntity<String>("logged out successfully", HttpStatus.OK);
			}else {
				return new ResponseEntity<String>("log out failed", HttpStatus.BAD_REQUEST);
			}	
		}

 }

