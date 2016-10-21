package com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity;

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
	private String servicesessionId;
	
	@Column(value = "growingio_id")
	private String growingioId;
	
	@Column(value = "visitor_id")
	private String visitorId;
	
	@Column(value = "context")
	private String context;
	
}
