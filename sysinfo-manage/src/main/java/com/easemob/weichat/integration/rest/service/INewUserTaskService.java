package com.easemob.weichat.integration.rest.service;

import com.easemob.weichat.integration.data.NewUserTaskData.NewUserTasksResponseData;

public interface INewUserTaskService {
    boolean doReceiveNewUserRegisterEvent(Integer tenantId, String agentId);

    NewUserTasksResponseData doGetNewUserTaskList(Integer tenantId, String agentId);

    boolean doAgentUserTaskDone(Integer tenantId, String agentId, String taskDone);
}
