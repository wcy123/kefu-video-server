package com.easemob.weichat.integration.modes;

import lombok.Data;

@Data
public class DelegateRegisterDataResp {

	private String user_id ;
	private String account_id ; 
	private String project_id ;
	private String access_token ; 
	private long expires_in;
	private String refresh_token;

}
