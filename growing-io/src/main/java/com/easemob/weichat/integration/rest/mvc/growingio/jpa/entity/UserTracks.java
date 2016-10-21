package com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity;

import java.util.Date;
import java.util.Map;

import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import lombok.Data;

@Data
@Table(value = "growing_visitor_trace")
public class UserTracks {
  
    @PrimaryKeyColumn(name = "tenant_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	@Column(value = "tenant_id")
	private int tenantId;
    
    @PrimaryKeyColumn(name = "visitor_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    @Column(value = "visitor_id")
	private String visitorId;
	
	@Column(value = "growingio_id")
	private String growingioId;
	
    @PrimaryKeyColumn(name = "timestamp", ordinal = 2, type = PrimaryKeyType.CLUSTERED,ordering=Ordering.DESCENDING)
	@Column(value = "timestamp")
	private Date timestamp;
	
	@Column(value = "attrs")
	private Map<String, String> attributes;
	
	@Column(value = "type")
	private String type;
	
}
