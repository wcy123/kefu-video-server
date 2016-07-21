package com.easemob.weichat.integration.modes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;



@Data
@JsonInclude(value=Include.NON_NULL)
public class EventAttr {
	@JsonProperty("定义")
	private String define;
	
	@JsonProperty("事件时间")
	private long eventTime;
	
	@JsonProperty("域名")
	private String domian;
	
	@JsonProperty("终端")
	private String terminal;
	
	@JsonProperty("访问来源")
	private String accessSource;
	
	@JsonProperty("标题")
	private String title;
	
	@JsonProperty("页面")
	private String page;
	
	@JsonProperty("截图")
	private String printscreen;
}
