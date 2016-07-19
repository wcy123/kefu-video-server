package com.easemob.weichat.integration.modes;

import lombok.Data;

@Data
public class DashboardReq {
	private String client_id ;
	private long timestamp ; 
	private String user_id ;
	private String sign ; 
}
