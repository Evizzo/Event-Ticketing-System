package com.eventticketingsystem.eventticketingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages = "com.eventticketingsystem.eventticketingsystem.entities")
@EnableScheduling
public class EventTicketingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventTicketingSystemApplication.class, args);
	}

}
