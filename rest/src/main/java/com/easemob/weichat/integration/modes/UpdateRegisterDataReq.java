package com.easemob.weichat.integration.modes;

import lombok.Data;

@Data
public class UpdateRegisterDataReq {
	private String clientId ;
	private String clientSecret ; 
	private long timestamp ;
	private String grantType ;
	private String refreshToken ;
}
