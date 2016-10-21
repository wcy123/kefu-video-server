package com.easemob.weichat.integration.rest.mvc.growingio.jpa;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity.ServicesessionTrack;

@Repository
public interface ServicesessionTrackRepository extends CassandraRepository<ServicesessionTrack> {
	@Query( "select * from service_session_track where servicesession_id=?0 ")
	ServicesessionTrack findbyServicesessionId(String servicesessionId );
}