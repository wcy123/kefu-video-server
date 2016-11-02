package com.easemob.kefu.rtcmedia.service;

import com.easemob.kefu.rtcmedia.protocol.AgentJid;

@FunctionalInterface
public interface ImUserService {
    AgentJid.Response getJidResponse(String agentName, String orgName, String appName,
            String imServiceNumber);
}
