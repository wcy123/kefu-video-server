package com.easemob.kefu.rtcmedia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.easemob.kefu.rtcmedia.dep.WebAppApi;
import com.easemob.kefu.rtcmedia.model.RtcMediaAgentJid;
import com.easemob.kefu.rtcmedia.model.RtcMediaAgentJidRepository;
import com.easemob.kefu.rtcmedia.protocol.AgentJid;
import com.easemob.kefu.rtcmedia.service.ImUserService;

@Component
public class ImUserServiceImpl implements ImUserService {
    @Autowired
    private RtcMediaAgentJidRepository repository;
    @Autowired
    private WebAppApi webAppApi;

    @Override
    public AgentJid.Response getJidResponse(String agentName, String orgName, String appName,
            String imServiceNumber) {
        final RtcMediaAgentJid agentJid = repository.findByAgentName(agentName);
        if (agentJid != null) {
            return buildResponse(agentJid);
        }
        final WebAppApi.ResponseRegisterImUser imUser =
                webAppApi.registerImUser(orgName, appName, imServiceNumber);
        final RtcMediaAgentJid jid = RtcMediaAgentJid.builder()
                .agentName(agentName)
                .orgName(orgName)
                .appName(appName)
                .userName(imUser.getUserId())
                .password(imUser.getUserPassword())
                .build();
        final RtcMediaAgentJid newJid = repository.save(jid);
        return buildResponse(newJid);
    }

    private AgentJid.Response buildResponse(RtcMediaAgentJid agentJid) {
        return AgentJid.Response.builder()
                .orgName(agentJid.getOrgName())
                .appName(agentJid.getAppName())
                .userName(agentJid.getUserName())
                .password(agentJid.getPassword())
                .build();
    }
}
