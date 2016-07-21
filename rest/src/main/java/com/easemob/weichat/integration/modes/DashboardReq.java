package com.easemob.weichat.integration.modes;

import lombok.Data;

@Data
public class DashboardReq {
	private String clientId ;
	private long timestamp ; 
	private String userId ;
	private String sign ; 
}
