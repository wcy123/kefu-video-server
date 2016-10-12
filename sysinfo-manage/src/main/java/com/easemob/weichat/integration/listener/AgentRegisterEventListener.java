package com.easemob.weichat.integration.listener;

import com.easemob.weichat.integration.rest.service.INewUserTaskService;
import com.easemob.weichat.service.events.agent.AgentUserCreationEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 监听AgentUserCreationEvent事件，生成该agent的redis中任务记录
 * @author shengyp
 * @since 10/10/16
 */

@Slf4j
@Component
public class AgentRegisterEventListener implements ApplicationListener<AgentUserCreationEvent> {
    @Autowired
    private INewUserTaskService newUserTaskService;

    @Override
    public void onApplicationEvent(AgentUserCreationEvent event) {
        if (event.getAgentUser().getRoles().contains("admin")) {
            Integer tenantId = event.getTenantId();
            String agentId= event.getUserId();
            log.info("receive the published event: AgentUserCreationEvent. tenantId={}", event.getTenantId());
            newUserTaskService.doReceiveNewUserRegisterEvent(tenantId, agentId);
        }
    }
}
