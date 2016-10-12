package com.easemob.weichat.integration.consumer;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.easemob.weichat.models.util.JSONUtil;
import com.easemob.weichat.service.events.AbstractEvent;
import com.easemob.weichat.service.events.BaseEvent;
import com.easemob.weichat.service.events.EventConsumerCallback;
import com.easemob.weichat.service.events.EventServer;
import com.easemob.weichat.service.events.agent.AgentUserCreationEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shengyp
 * @date 09/28/2016
 */
@Slf4j
@Component
public class SysinfoManageKafkaEventConsumer implements EventConsumerCallback<BaseEvent>{
    @Autowired
    private EventServer eventServer;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Value("${kefu.kafka.topic}")
    private String eventTopic;
    
    @PostConstruct
    public void init() {
        eventServer.consume(eventTopic, this);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void consume(BaseEvent data, int partitionId, long offset) {
        if (data == null || data.getBody() == null || StringUtils.isEmpty(data.getType())) {
            return;
        }
        if (!isSupported(data.getType()) || !isVersionCompatibile(data)) {
            return;
        }
        try {
            Class clazz = Class.forName(data.getType());
            Object object = JSONUtil.getObjectMapper().convertValue(data.getBody(), clazz);
            if (object instanceof AbstractEvent) {
                AgentUserCreationEvent event = (AgentUserCreationEvent)object;
                event.setShouldSkipKafka(true);
                eventPublisher.publishEvent(event);
                log.info("receive the kafka event: publish AgentUserCreationEvent. tenantId={}", event.getTenantId());
            }
        } catch (ClassNotFoundException e) {
            log.warn("failed to find class for event type {}", data.getType(), e);
        }
    }
    
    /**
     * 检查事件的版本号, 确保这里只从kafka中消费新的代码产生的事件, 而忽略老的
     * 因为老版的代码中, 还有旧的listener在监听着同样的事件
     */
    @SuppressWarnings("rawtypes")
    private static boolean isVersionCompatibile(BaseEvent event){
        if(event == null || event.getBody()==null || !(event.getBody() instanceof Map)){
            return false;
        }
        Map map = (Map)event.getBody();
        if(map.isEmpty() || !map.containsKey("version")){
            return false;
        }
        String version = map.get("version").toString();
        if(StringUtils.isEmpty(version)){
            return false;
        }
        double v = Double.parseDouble(version);
        return v >= 1.1;
    }
    
    private boolean isSupported(String type) {
        if (type.equalsIgnoreCase(AgentUserCreationEvent.class.getName())) {
            return true;
        }
        return false;
    }
}
