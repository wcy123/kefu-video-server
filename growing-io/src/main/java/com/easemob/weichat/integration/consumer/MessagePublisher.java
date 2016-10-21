package com.easemob.weichat.integration.consumer;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.easemob.weichat.integration.modes.IntgerationGrowingInfo;
import com.easemob.weichat.models.util.JSONUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessagePublisher {
  private final String topic;
  private final StringRedisTemplate redisTemplate;

  public MessagePublisher(StringRedisTemplate redisTemplate, String topic) {
    this.redisTemplate = redisTemplate;
    this.topic = topic;
  }

  public void publish(String serviceSessionId, int tenantId, String userId, Object message) {
    try {
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
