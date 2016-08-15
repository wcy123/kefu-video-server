package com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import lombok.Data;

@Data
@Table(value = "growing_visitor_trace")
public class UserTracks {
	@Column(value = "tenant_id")
	private int tenantId;
	
	@PrimaryKey(value = "visitor_id")
	private UUID visitorId;
	
	@Column(value = "growingio_id")
	private String growingioId;
	
	@Column(value = "timestamp")
	private Date timestamp;
	
	@Column(value = "attrs")
	private HashMap<String, String> attributes;
	
	@Column(value = "type")
	private String type;
	
}
