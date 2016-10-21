package com.easemob.weichat.integration.constants;


public enum IntegrationStatus{
	

	SUCCESS(0,"0000", "process access"),
	GROWING_TENANTID_NULL(1,"0001", "tenantid is null"),
	GROWING_TENANTID_NOT_ANGET(2,"0002", " the tenant did not contain agent"),
	GROWING_TENANTID_REMOTE_ERROR(3,"0003", "tenant:[%s], remote GrowingIO fail"),
	GROWING_TENANTID_REGEDIT_ERROR(4,"0004", "tenant:[%s],did not GrowingIO regedit fail"),
	GROWING_TENANTID_REGEDIT_CONN_ERROR(5,"0005", "tenant:[%s], remote Growing fail ,detail:[%s]"),
	GROWING_TENANTID_USER_ERROR(6,"0006", "tenant:%d,%s,did not GrowingIO User"),
	GROWING_TENANTID_EVENT_ERROR(7,"0007", "tenant:%d,%s,did not GrowingIO User Tracks "),
	GROWING_TENANTID_REGEDIT_OPTION_ERROR(8,"0008", "did not regedit growingioEnable Option  "),
	USER_TRACK_NOT_UPDATE(9,"0009","did not update user tracks"),
	NOKNOW(1,"9999", "no know");

	
	public String getCode() {
		return code;
	}

	private int id;

	private String description;

	private  String code;
	
	private  String tempStr ; 
	
	private IntegrationStatus(int id , String code, String description) {
		this.code = code;
		this.id=id;
		this.description=description;
	}
 
	
	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}


	public String getTempStr() {
		return tempStr;
	}


	public void setTempStr(String tempStr) {
		this.tempStr = tempStr;
	}
}
