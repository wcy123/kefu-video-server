package com.easemob.weichat.integration.listener;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;

import com.easemob.weichat.integration.modes.IntgerationGrowingInfo;
import com.easemob.weichat.models.entity.ServiceSession;
import com.easemob.weichat.models.util.JSONUtil;
import com.easemob.weichat.service.events.session.ServiceSessionCreatedEvent;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;



@Slf4j
public class IntegrationEventListener {
  
	private final MessagePublisher messagePusher;

    public IntegrationEventListener(StringRedisTemplate redisTemplate, String topic) {
        this.messagePusher = new MessagePublisher(redisTemplate, topic);
    }
	
	@EventListener
    @Async
    public void serviceSessionCreatedEvent(ServiceSessionCreatedEvent e) {
        ServiceSession serviceSession = e.getServiceSession();
        log.debug("serviceSessionCreatedEvent tenantId:{} userId:{} serviceSessionId:{}", serviceSession.getTenantId(), serviceSession.getVisitorUser().getUserId(), serviceSession.getServiceSessionId());
        if (StringUtils.isNotBlank(serviceSession.getVisitorUser().getUserId()) && serviceSession.getTenantId() != null) {
        	ObjectNode ext = JSONUtil.getObjectMapper().convertValue(e.getEasemobMessage().getExt(), ObjectNode.class);
        	ObjectNode node = (ObjectNode)ext.get("weichat").get("visitor") ;
        	
        	if(node!=null&& node.get("gr_user_id")!=null){
        		publishToRedis(serviceSession.getServiceSessionId(),serviceSession.getTenantId(), serviceSession.getVisitorUser().getUserId(), node.get("gr_user_id").asText(),messagePusher);
        	}else{
        		log.debug(String.format("TenantId:%d,UserId:%s is not growing user ", serviceSession.getTenantId(),serviceSession.getVisitorUser().getUserId()));
        	}
        	
        }
    }
	
	
	private void publishToRedis(String serviceSessionId,int tenantId, String userId, Object data, MessagePublisher messagePusher) {
        if(tenantId>0){
            messagePusher.publish(serviceSessionId,tenantId,userId,data );
        } else {
            log.warn("error tenantId or agentUserId . tenantId {}. userId {}. data {}", tenantId, userId, data);
        }
    }

    @Slf4j
    private static class MessagePublisher  {
        private final String topic;
        private final StringRedisTemplate redisTemplate;

        public MessagePublisher(StringRedisTemplate redisTemplate, String topic) {
            this.redisTemplate = redisTemplate;
            this.topic = topic;
        }

        public void publish(String serviceSessionId,int tenantId, String userId, Object message) {
            try{
            	IntgerationGrowingInfo data = new IntgerationGrowingInfo();
            	data.setTimestamp(System.currentTimeMillis());
            	data.setUserId(userId);
            	data.setGrowingioId(message.toString());
            	data.setTenantId(tenantId);
            	data.setServiceSessionId(serviceSessionId);
            	
                String value = JSONUtil.getObjectMapper().writeValueAsString(data);
                
                redisTemplate.boundListOps(topic).rightPush(value);
                
            } catch (Exception e) {
                log.error("userId is sending to {} with {}", userId, message, e);
            }
        }

        

    }
}
