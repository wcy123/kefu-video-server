package com.easemob.weichat.integration.rest.mvc.growingio.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name = "growing_io_company")
public class GrowingTenantAction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "tenantId")
	private Long tenantId;
	
	@Column(name = "userId")
	private String userId;
	
	@Column(name = "accountId")
	private String accountId;
	
	@Column(name = "projectId")
	private String projectId;
	
	@Column(name = "refreshToken")
	private String refreshToken;
	
	@Column(name = "mail")
	private String mail;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "createDateTime")
	private Date createDateTime;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "status", length = 100)
    private GrowingTenantType status; 
}
