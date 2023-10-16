package com.eventticketingsystem.eventticketingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class EventTicketingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventTicketingSystemApplication.class, args);
	}
	@Bean
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurer() {
			public void addCorsMappings(final CorsRegistry registry){
				registry.addMapping("/**")
						.allowedMethods("*")
						.allowedOrigins("http://localhost:5173");
			}
		};
	}

}
