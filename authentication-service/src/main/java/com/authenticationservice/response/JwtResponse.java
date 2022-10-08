package com.authenticationservice.response;

import java.util.List;

public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String userEmail;
 
  private List<String> roles;

  public JwtResponse(String accessToken, Long id, String userEmail, List<String> roles) {
    this.token = accessToken;
    this.id = id;
    this.userEmail = userEmail;
    
    this.roles = roles;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

 

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public List<String> getRoles() {
    return roles;
  }
}
