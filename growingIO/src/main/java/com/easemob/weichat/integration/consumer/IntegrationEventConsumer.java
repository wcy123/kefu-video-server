package com.easemob.weichat.integration.consumer;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationEventPublisher;

import com.easemob.weichat.models.util.JSONUtil;
import com.easemob.weichat.service.events.AbstractEvent;
import com.easemob.weichat.service.events.BaseEvent;
import com.easemob.weichat.service.events.EventConsumerCallback;
import com.easemob.weichat.service.events.EventServer;
import com.easemob.weichat.service.events.session.ServiceSessionCreatedEvent;

import lombok.extern.slf4j.Slf4j;



@Slf4j
public class IntegrationEventConsumer implements EventConsumerCallback<BaseEvent>{

	private final ApplicationEventPublisher eventPublisher;
	private final EventServer eventServer;
	private final String eventTopic;
	
	public IntegrationEventConsumer(ApplicationEventPublisher eventPublisher, EventServer eventServer, String eventTopic) {
		this.eventPublisher = eventPublisher;
		this.eventServer = eventServer;
		this.eventTopic = eventTopic;
	}
	
	@PostConstruct
	public void init() {
		eventServer.consume(eventTopic, this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void consume(BaseEvent data, int partitionId, long offset) {
        if (data == null || data.getBody() == null || StringUtils.isEmpty(data.getType())) {
            log.debug("skip null data {} consumed from kafka", data);
            return;
        }
        try {
            if (!isSupported(data.getType())) {
                log.debug("got unsupported type {}", data);
                return;
            }
            if(!isVersionCompatibile(data)) {
                log.debug("event {} is not compatible with this version server", data);
                return;
            }
            log.debug("got event {}", data);
            Class clazz = Class.forName(data.getType());
            Object object = JSONUtil.getObjectMapper().convertValue(data.getBody(), clazz);
            if (object instanceof AbstractEvent) {

                AbstractEvent event = (AbstractEvent)object;

                //这里是为了避免把从kafka中读取的事件再发到kafka一次, 造成重复
                event.setShouldSkipKafka(true);
                log.debug("convert to event {}", event);
                eventPublisher.publishEvent(event);
            }else{
                log.warn("unknown data consumed from kafka {}", data);
            }
        } catch (ClassNotFoundException e) {
            log.warn("failed to find class for event type {}", data.getType(), e);
        }
    }

    /**
     * 检查事件的版本号, 确保这里只从kafka中消费新的代码产生的事件, 而忽略老的
     *
     * 这是因为老版的代码中, 还有旧的listener在监听着同样的事件
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
        return v >= 1.0;
    }

    private boolean isSupported(String type) {
        return type.equalsIgnoreCase(ServiceSessionCreatedEvent.class.getName());
    }
	
}
