package com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity;

import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import lombok.Data;

@Data
@Table(value = "service_session_track")
public class ServicesessionTrack {
	@Column(value = "tenant_id")
	private int tenantId;
	
	@PrimaryKey(value = "servicesession_id")
	private UUID servicesessionId;
	
	@Column(value = "growingio_id")
	private String growingioId;
	
	@Column(value = "visitor_id")
	private UUID visitorId;
	
	@Column(value = "context")
	private String context;
}
