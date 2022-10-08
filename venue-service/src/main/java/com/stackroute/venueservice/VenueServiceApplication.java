package com.stackroute.venueservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
//@EnableEurekaClient
public class VenueServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VenueServiceApplication.class, args);
	}
}
