package com.pvb.springboot.veeru.lead_capture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class LeadCaptureScoringDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeadCaptureScoringDashboardApplication.class, args);
	}
	 @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }

}
