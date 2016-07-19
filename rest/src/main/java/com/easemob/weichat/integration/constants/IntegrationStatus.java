package com.easemob.weichat.integration.constants;


public enum IntegrationStatus{
	
	success(0,"0000", "process access"),
	growing_tetenantid_null(1,"0001", "传入的租户ID为null"),
	growing_tetenantid_not_anget(2,"0002", "传入的租户没有座席"),
	growing_tenantid_remote_error(3,"0003", "租户:[%s],与Growing通讯失败"),
	growing_tenantid_regedit_error(4,"0004", "租户:[%s],没有在Growing注册"),
	growing_tenantid_regedit_conn_error(5,"0005", "租户:[%s],在Growing通信失败,详细信息:[%s]"),
	growing_tenantid_user_error(5,"0006", "%d,%s,没有对应的Growing用户"),
	growing_tenantid_event_error(5,"0007", "%d,%s,没有得到对应的用户轨迹"),
	noknow(1,"9999", "no know");
	
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
