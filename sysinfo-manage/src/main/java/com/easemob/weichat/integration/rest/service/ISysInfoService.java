package com.easemob.weichat.integration.rest.service;

import com.easemob.weichat.integration.data.NewVersionInfo;
import com.easemob.weichat.integration.data.ReceivedId;

public interface ISysInfoService {
    NewVersionInfo doCheckVerInfoRead(Integer tenantId, String agentId);

    boolean doAgentUserRead(Integer tenantId, String agentId, ReceivedId receivedId);

    boolean doAddNewVersion(NewVersionInfo newVersionInfoData);

    NewVersionInfo getVersionInfo();

    void saveVersionInfo(NewVersionInfo newVersionInfoData);

    void deleteVersionInfo();

    void deleteAgentUserFromSet();

    void addTenantIdIntoSet(String tenantId);

    boolean checkAgentidExistInSet(Integer tenantId, String agentId);

    void addAgentidIntoSet(Integer tenantId, String agentId);
}