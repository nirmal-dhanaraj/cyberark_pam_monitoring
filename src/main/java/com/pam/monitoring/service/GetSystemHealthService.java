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
public class GetSystemHealthService {
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
	
	public ResponseEntity<String> getSystemHealth(String compId) throws Exception
	{		
		String authorizationToken1= AuthUtil.authlogin();		
		
		RestTemplate restTemplate = new RestTemplate();

	    HttpHeaders headers = new HttpHeaders();
	    String authorizationToken=authorizationToken1.substring(1, authorizationToken1.length()-1);
	    System.out.println("Token : "+authorizationToken); 

	    // set `content-type` header
	    headers.setContentType(MediaType.APPLICATION_JSON);		
		headers.add("Authorization", authorizationToken);
	    // build the request
	    HttpEntity<String> request = new HttpEntity<String>(null,headers);	
		  
		ResponseEntity<String> response= restTemplate.exchange("http://compsrv1.corpad.com/PasswordVault/api/ComponentsMonitoringDetails/"+ compId, HttpMethod.GET, request, String.class);
		System.out.println(" Health Monitoring Details Response Code : " +response.getStatusCodeValue());
		System.out.println("Monitoring  Details Response : "+response.getBody());
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.convertValue(response.getBody(), JsonNode.class);
		 JSONObject obj = new JSONObject(response.getBody());
		 System.out.println("");
		 
		 JSONArray arr = obj.getJSONArray("ComponentsDetails");
	        for (int i = 0; i < arr.length(); i++) {
	            String comp_ip = arr.getJSONObject(i).getString("ComponentIP");
	            Boolean comp_status = arr.getJSONObject(i).getBoolean("IsLoggedOn");
	            System.out.println("Components IP = "+comp_ip);
	            System.out.println("Components Connected? = "+comp_status);
	            if (comp_status == false)
	            {
	            	System.out.println("Create ServiceNow Incident ");
	            	try {
	            	    // request url
	            	    String url2 = "https://dev63803.service-now.com/api/now/v1/table/incident";

	            	    // create auth credentials
	            	    String authStr = "admin:juSHU5mUGgg2";
	            	    String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());

	            	    // create headers
	            	    HttpHeaders headers2 = new HttpHeaders();
	            	    headers2.setContentType(MediaType.APPLICATION_JSON);	
	            	    headers2.add("Authorization", "Basic " + base64Creds);

	            	    // create request
	            	    HttpEntity request2 = new HttpEntity(headers2);

	            	    // make a request postForObject
	            	  //  ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, request, String.class);
	            	    ResponseEntity<String> response2 = new RestTemplate().exchange(url2,  HttpMethod.POST, request2, String.class);

	            	    // get JSON response
	            	    String json = response2.getBody();
	            	    System.out.println("ServiceNow Response is : "+"\n"+ json);

	            	} catch (Exception ex) {
	            	    ex.printStackTrace();
	            	}
	            }
	            System.out.println("");
	        }


			
			/*
			 * ResponseEntity<String> logoutResponse =
			 * authService.logoutAccessToken(authorizationToken1);
			 * System.out.println("Logout Response : "+logoutResponse.getBody().toString());
			 */
	    
		return response;

	}
}
