package com.pam.monitoring.component;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pam.monitoring.service.GetSystemHealthSummaryService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class HealthScheduler {

	@Autowired
	private GetSystemHealthSummaryService getSystemHealthSummaryService;

	@Scheduled(fixedDelay = 5000)
	public void scheduleHealthSummary() {
		try {
			System.out.println("========== ============= ");
			log.info(" Health Check Starting ");
			ResponseEntity<String> response = getSystemHealthSummaryService.getSystemHealthSummary();
			System.out.println("Response -----------------"+response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
