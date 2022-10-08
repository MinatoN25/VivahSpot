package com.authenticationservice.conrollers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
   

  @GetMapping("/bookie")
  @PreAuthorize("hasRole('BOOKIE')")
  public String userAccess() {
    return "welcome to vivahspot homepage.";
  }

 

  @GetMapping("/venueowner")
  @PreAuthorize("hasRole('VENUEOWNER')")
  public String operatorAccess() {
    return "welcome to vivahspot venueowner homepage";
  }
}
