package com.pam.monitoring.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pam.monitoring.service.AuthService;
import com.pam.monitoring.service.GetSystemHealthService;
import com.pam.monitoring.service.GetSystemHealthSummaryService;


@Controller
public class LoginController {
	
private final AuthService authService;
private final GetSystemHealthSummaryService getSystemHealthSummaryService;
private GetSystemHealthService getSystemHealthService;
	
	@Autowired
	LoginController(final AuthService authService, final GetSystemHealthService getSystemHealthService, final GetSystemHealthSummaryService getSystemHealthSummaryService){
		this.authService= authService;
		this.getSystemHealthSummaryService=getSystemHealthSummaryService;
		this.getSystemHealthService=getSystemHealthService;
	}
	
	@RequestMapping("/getAccessToken")
	public ResponseEntity<String> getAccessToken() throws Exception {
		return authService.getAccessToken();
	}
	/*
	 * @RequestMapping("/getHealthMonitoring") public ResponseEntity<String>
	 * getSystemHealth(@RequestParam String compId) throws Exception { return
	 * getSystemHealthService.getSystemHealth(compId); }
	 */
	
	@RequestMapping("/getMonitoringSummary")
	public ResponseEntity<String> getSystemHealthSummary() throws Exception {
		return getSystemHealthSummaryService.getSystemHealthSummary();
	}


}