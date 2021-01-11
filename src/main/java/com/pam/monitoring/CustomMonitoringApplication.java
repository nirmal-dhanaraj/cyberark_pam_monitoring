package com.pam.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CustomMonitoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomMonitoringApplication.class, args);
	}

}
