package com.authenticationservice.request;


import javax.validation.constraints.*;

public class SignupRequest {
  @NotBlank
  @Size(min = 2, max = 120)
  private String userEmail;

  private String role;

  @NotBlank
  @Size(min = 3, max = 120)
  private String password;



  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

 
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return this.role;
  }

  public void setRole(String role) {
    this.role = role;
  }

public String getUserEmail() {

	return userEmail;
}
}
