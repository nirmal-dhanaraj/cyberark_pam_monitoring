package com.pam.monitoring.util;


import com.pam.monitoring.model.AuthRequest;

public class AuthRequestBuilder {
	public AuthRequest buildAuthRequest(String username, String password)
	{
		AuthRequest authReq = new AuthRequest();
		authReq.setUsername(username);
		authReq.setPassword(password);
		authReq.setConcurrentSession("false");
		authReq.setSecureMode(false);
		return authReq;
		
	}
}
