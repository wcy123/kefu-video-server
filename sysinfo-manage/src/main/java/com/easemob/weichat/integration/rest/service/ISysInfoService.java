package com.easemob.weichat.integration.rest.service;

import com.easemob.weichat.integration.data.NewVersionInfo;

public interface ISysInfoService {
    NewVersionInfo getVersionInfo();

    void saveVersionInfo(NewVersionInfo newVersionInfoData);

    void deleteVersionInfo();

    void deleteAgentUserFromSet();
    
    void addTenantIdIntoSet(String tenantId);

    boolean checkAgentidExistInSet(Integer tenantId, String agentId);

    void addAgentidIntoSet(Integer tenantId, String agentId);
}
