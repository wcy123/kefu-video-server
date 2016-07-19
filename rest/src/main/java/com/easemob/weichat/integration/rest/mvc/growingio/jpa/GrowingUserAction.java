package com.easemob.weichat.integration.rest.mvc.growingio.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "growing_io_user")
public class GrowingUserAction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "tenantId")
	private Long tenantId;
	
	@Column(name = "userName")
	private String userName;
	
	@Column(name = "growingUserId")
	private String growingUserId;
	
	@Column(name = "projectId")
	private String projectId;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "createDateTime")
	private Date createDateTime;
	
}
