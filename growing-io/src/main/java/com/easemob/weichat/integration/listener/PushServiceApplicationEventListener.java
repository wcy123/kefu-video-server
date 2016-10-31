package com.easemob.weichat.integration.listener;

import com.easemob.weichat.models.util.JSONUtil;
import com.easemob.weichat.models.util.ResourceUtil;
import com.easemob.weichat.service.data.QueueMessageData;
import com.easemob.weichat.service.data.UserPK;
import com.easemob.weichat.service.events.QueueMessageEvent;
import com.easemob.weichat.service.pushservice.PushServiceData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.StringRedisTemplate;

@Slf4j
public class PushServiceApplicationEventListener implements ApplicationListener<QueueMessageEvent> {

    private final MessagePublisher messagePusher;

    public PushServiceApplicationEventListener(StringRedisTemplate redisTemplate, String topic) {
        this.messagePusher = new MessagePublisher(redisTemplate, topic);
    }

    @Override
    public void onApplicationEvent(QueueMessageEvent event) {
        if (event == null||event.getQueueMessageData() == null) {
            return;
        }
        QueueMessageData messageData = event.getQueueMessageData();
        log.debug("got QueueMessageEvent with source {} ", messageData);

        UserPK userPK = messageData.getUserPK();
        if (userPK == null || userPK.getTenantId()<1 || messageData.getMessages() == null || messageData.getMessages().isEmpty()) {
            log.debug("queue message data is invalid, ignoring");
            return;
        }
        String userId = messageData.isBroadcast() ? null : userPK.getUserId();
        publishToAgents(userPK.getTenantId(), userId, messageData);
    }


    private void publishToAgents(int tenantId, String agentUserId, QueueMessageData data) {
        if(tenantId>0){
            String buildTenant = ResourceUtil.buildAgent(String.valueOf(tenantId), agentUserId);
            messagePusher.publish(buildTenant, data.getMessages());
        } else {
            log.warn("error tenantId or agentUserId . tenantId {}. agentUserId {}. data {}", tenantId, agentUserId, data);
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

        public void publish(String path, Object message) {
            PushServiceData data = buildPushServiceData(path, message);
            try{
                String value = JSONUtil.getObjectMapper().writeValueAsString(data);
                redisTemplate.boundListOps(topic).rightPush(value  );

                log.debug("message is sending to {} with {}", path, message);
            } catch (Exception e) {
                log.error("message is sending to {} with {}", path, message, e);
            }
        }

        private static PushServiceData buildPushServiceData(String path, Object message) {
            PushServiceData pushdata = new PushServiceData();
            pushdata.setPath(path);
            pushdata.setPayload(message);
            pushdata.setTimestamp(System.currentTimeMillis());
            return pushdata;
        }


    }


}
