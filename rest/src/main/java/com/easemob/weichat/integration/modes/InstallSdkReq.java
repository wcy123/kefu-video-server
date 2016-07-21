package com.easemob.weichat.integration.modes;

import lombok.Data;

@Data
public class InstallSdkReq {
	private String clientId ;
	private long timestamp ; 
	private String userId ;
	private String sign ; 
}
