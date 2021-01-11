package com.pam.monitoring.service;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
//import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pam.monitoring.model.AuthRequest;
import com.pam.monitoring.util.AuthRequestBuilder;
import com.pam.monitoring.util.AuthUtil;

@Service
public class GetSystemHealthSummaryService {
	@Value("${application.logon.url}")
	private String access_token_url;
	@Value("${application.logout.url}")
	private String logOff_url;
	@Value("${auth.username}")
	private String username;
	@Value("${auth.pass}")
	private String password;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private GetSystemHealthService getSystemHealthService;
	
	public ResponseEntity<String> getSystemHealthSummary() throws IOException
	{		
		String authorizationToken1= AuthUtil.authlogin();		
		
		RestTemplate restTemplate = new RestTemplate();

	    HttpHeaders headers = new HttpHeaders();
	    String authorizationToken=authorizationToken1.substring(1, authorizationToken1.length()-1);
	    System.out.println("Token : "+authorizationToken); 
	    
	    headers.setContentType(MediaType.APPLICATION_JSON);		
		headers.add("Authorization", authorizationToken);
	    // build the request
	    HttpEntity<String> request = new HttpEntity<String>(null,headers);	
		  
		ResponseEntity<String> response= restTemplate.exchange("http://compsrv1.corpad.com/PasswordVault/api/ComponentsMonitoringSummary", HttpMethod.GET, request, String.class);
		System.out.println("Monitoring Summary Response Code : " +response.getStatusCodeValue());
		System.out.println("Monitoring Summary Response : "+response.getBody());
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.convertValue(response.getBody(), JsonNode.class);
		 JSONObject obj = new JSONObject(response.getBody());
		 System.out.println("");
		 
		 JSONArray arr = obj.getJSONArray("Components");
	        for (int i = 0; i < arr.length(); i++) {
	            String comp_id = arr.getJSONObject(i).getString("ComponentID");
	            String comp_name = arr.getJSONObject(i).getString("ComponentName");
	            System.out.println("Components ID = "+comp_id);
	            System.out.println("Components Name = "+comp_name);
	            System.out.println("");
	            try {
					ResponseEntity<String> compResponse =getSystemHealthService.getSystemHealth(comp_id);
					// System.out.println("comp Response = "+compResponse);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	           
	            System.out.println("");
	        }
			 JSONArray arrOfVaults = obj.getJSONArray("Vaults");
		        for (int i = 0; i < arrOfVaults.length(); i++) {
		        	String vault_ip = arrOfVaults.getJSONObject(i).getString("IP");
		            boolean vault_status = arrOfVaults.getJSONObject(i).getBoolean("IsLoggedOn");
		            System.out.println("The Vault IP is = "+vault_ip);
		            System.out.println("Is Vault is Active? = "+vault_status);
		        }
		        System.out.println("");
			
		ResponseEntity<String> logoutResponse = authService.logoutAccessToken(authorizationToken1);
  
		return response;

	}
}
