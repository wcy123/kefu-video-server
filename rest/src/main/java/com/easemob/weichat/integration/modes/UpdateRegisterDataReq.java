package com.easemob.weichat.integration.modes;

import lombok.Data;

@Data
public class UpdateRegisterDataReq {
	private String client_id ;
	private String client_secret ; 
	private long timestamp ;
	private String grant_type ;
	private String refresh_token ;
}
