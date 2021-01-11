
package com.pam.monitoring.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AuthRequest { 

private String username;

private String password;

private String newpassword;

private String type;

private String additionalInfo;

private Boolean secureMode;

private String concurrentSession;

}

