package com.vivahspotuser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    @Bean
    @Hidden
    @Parameter(hidden=true)
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.vivahspotuser"))
                .paths(PathSelectors.regex("/api/v1/.*"))
                .build()
                .apiInfo(apiInfo());
                
                
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Vivah Sot Application",
                "An application to search venue from a venue repository by venueId",
                "VivahSpotApplication v1",
                "Terms of service",
                "vivahSpot@gmail.com",
                "License of API",
                "https://swagger.io/docs/");
        return apiInfo;
    }
    
    
}
