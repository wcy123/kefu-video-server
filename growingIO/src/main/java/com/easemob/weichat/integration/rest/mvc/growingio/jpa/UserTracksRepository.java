package com.easemob.weichat.integration.rest.mvc.growingio.jpa;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity.UserTracks;

@Repository
public interface UserTracksRepository extends CassandraRepository<UserTracks> {
	@Query( "select * from growing_visitor_trace where visitor_id=?1 and tenant_id=?2")
	UserTracks find(String visitorId ,  int tenantId);
}