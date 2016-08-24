package com.easemob.weichat.integration.modes;

import lombok.Data;

@Data
public class DelegateRegisterDataResp {

	private String userId ;
	private String accountId ; 
	private String projectId ;
	private String accessToken ; 
	private long expiresIn;
	private String refreshToken;

}
