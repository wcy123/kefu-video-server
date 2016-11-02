package com.easemob.kefu.rtcmedia.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "/v1/rtcmedia/Agent")
public interface RtcMediaAgentJidRepository extends JpaRepository<RtcMediaAgentJid, Long> {
    RtcMediaAgentJid findByAgentName(String agentName);
}
