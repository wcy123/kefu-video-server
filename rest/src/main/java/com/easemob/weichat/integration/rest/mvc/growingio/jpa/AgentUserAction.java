package com.easemob.weichat.integration.rest.mvc.growingio.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.easemob.weichat.models.entity.User;
import com.easemob.weichat.models.enums.AgentOnLineStatus;
import com.easemob.weichat.models.enums.UserStatus;
import com.easemob.weichat.models.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "agentuser", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class AgentUserAction extends User {
	private static final long serialVersionUID = -5801041973793720978L;

	public AgentUserAction(){
		super();
		this.setUserType(UserType.Agent);
	}
	@Column(updatable = false)
	private String password;
	//昵称 手机号码、在线状态、创建时间
	private String username;
	@Column(updatable = false)
	private String roles;	//可以同时拥有多种，用逗号分隔
   
    @Enumerated(EnumType.STRING)
	private UserStatus status = UserStatus.Enable;
    @Enumerated(EnumType.STRING)
    private AgentOnLineStatus onLineState = AgentOnLineStatus.Offline;

	private Integer maxServiceSessionCount = 10;	//最大服务访客数
	private String trueName;		//真实姓名
	private String mobilePhone;		//手机号码
	private Integer agentNumber;	//客服工号
	private String welcomeMessage;	//客服登录欢迎信息(这个按昵称自动生成。
    @JsonIgnore
    @Transient
    private Boolean realTimeOnlineState = false;	//当前是否在线 =faile=离线 true=在线,当客服在线状态为在线，且有激活时间未超时，该状态为在线
    @Transient
    private AgentOnLineStatus currentOnLineState;	//返回当前的在线状态
	
	private String avatar;
	
	
}