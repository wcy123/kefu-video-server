package com.easemob.weichat.integration.rest.service;

import org.springframework.http.ResponseEntity;

import com.easemob.weichat.integration.data.NewVersionInfo;
import com.easemob.weichat.integration.data.ReceivedId;
import com.easemob.weichat.models.data.ApiResponse;

public interface ISysInfoService {
    ResponseEntity<ApiResponse> DoAddNewVersion(NewVersionInfo newVersionInfoData);

    ResponseEntity<ApiResponse> doCheckVerInfoRead(Integer tenantId, String agentId);

    ResponseEntity<ApiResponse> doAgentUserRead(Integer tenantId, String agentId, ReceivedId receivedId);

    NewVersionInfo getVersionInfo();

    void saveVersionInfo(NewVersionInfo newVersionInfoData);

    void deleteVersionInfo();

    void deleteAgentUserFromSet();

    void addTenantIdIntoSet(String tenantId);

    boolean checkAgentidExistInSet(Integer tenantId, String agentId);

    void addAgentidIntoSet(Integer tenantId, String agentId);
}
