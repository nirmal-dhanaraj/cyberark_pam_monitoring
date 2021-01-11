package com.pam.monitoring.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AuthUtil {
	public static String authlogin() {
		RestTemplate restTemplate = new RestTemplate();
		System.out.println("====================== ");
		ResponseEntity<String> response= restTemplate.postForEntity("http://localhost:8080/getAccessToken",null,String.class);
		
		System.out.println("New Authentication Response ");
		return response.getBody().toString();
		
	}		
}
