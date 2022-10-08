package com.RecomendationService.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.Hidden;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringSwagger {
	
	  @Bean
	    @Hidden
	    public Docket api() {
	        return new Docket(DocumentationType.SWAGGER_2)
	        		.useDefaultResponseMessages(false)
	        		.securityContexts(Arrays.asList(securityContext()))
					.securitySchemes(Arrays.asList(apiKey())).select()
	                .apis(RequestHandlerSelectors.basePackage("com.RecomendationService.controller"))
	                .paths(PathSelectors.regex("/api/v1/.*"))
	                .build()
	                .apiInfo(apiInfo());
	        
	        

	  }
	  private ApiInfo apiInfo() {
	        ApiInfo apiInfo = new ApiInfo(
	                "Vivah Spot Application",
	                "An application to search venue from a venue repository by venueId",
	                "VivahSpotApplication v1",
	                "Terms of service",
	                "vivahSpot07@gmail.com",
	                "License of API",
	                "https://swagger.io/docs/");
	        return apiInfo;
	    }
	  
	  private ApiKey apiKey() {
			return new ApiKey("JWT", "Authorization", "header");
		}

		private SecurityContext securityContext() {
			return SecurityContext.builder().securityReferences(defaultAuth()).build();
		}

		private List<SecurityReference> defaultAuth() {
			AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
			AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
			authorizationScopes[0] = authorizationScope;
			return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
		}
}