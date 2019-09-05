package com.kuliza.mmtraverl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/* 
 * Integration with swagger UI. 
 * We can easily test Rest rest Service using Swagger.
 * Once Application up and running, try to hit below url to see swagger dashboard 
 * 
 * http://localhost:8080/managedmethods/swagger-ui.html
 * 
 * url pattern below
 * 
 * {Protocol}://{IP}:{Port}/[context-path]/swagger-ui.html 
 *  
 * */ 

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
    public Docket api() { 
		
		/*
		 * Swagger will scan all Rest service with below configuration. 
		 */
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build();                                           
    }
	
	
}
