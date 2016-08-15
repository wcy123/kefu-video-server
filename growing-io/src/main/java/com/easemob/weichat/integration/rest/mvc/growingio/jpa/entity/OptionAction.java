package com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author likai
 * @date 2016年5月25日 下午4:48:06
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "options")
public class OptionAction {
	
	@Id
	private String optionId;
	
	@Column(name = "tenantId")
	private int tenantId;
	
	@Column(name = "optionName")
	private String optionName;
	
	@Column(name = "optionValue")
	private String optionValue;
	
}
