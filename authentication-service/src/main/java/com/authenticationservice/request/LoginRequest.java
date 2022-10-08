package com.authenticationservice.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
	@NotBlank
  private String userEmail;

	@NotBlank
	private String password;

	public String getUserEmail() {
		return userEmail;
	}

	public void setUsername(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
