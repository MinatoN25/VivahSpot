package com.RecomendationService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.RecomendationService.auth.JwtFilter;

@SpringBootApplication
@EnableEurekaClient
public class RecomendationServiceApplication {
	
	private static final String APP_PATH="/api/v1/*";

	public static void main(String[] args) {
		SpringApplication.run(RecomendationServiceApplication.class, args);
	}
	
	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilter() {
		FilterRegistrationBean<JwtFilter> filterRegistrationBean=new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new JwtFilter());
		filterRegistrationBean.addUrlPatterns(APP_PATH);
		return filterRegistrationBean;
	}

}
