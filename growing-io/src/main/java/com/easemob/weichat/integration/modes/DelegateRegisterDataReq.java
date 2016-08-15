package com.easemob.weichat.integration.modes;

import lombok.Data;

@Data
public class DelegateRegisterDataReq {
	private long timestamp ;
	private String company ; 
	private String email ;
	private String domain ; 
	private int scale ;
	private int industry ;
}
