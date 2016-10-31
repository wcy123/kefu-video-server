package com.easemob.weichat.integration.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.easemob.weichat.integration.listener.PushServiceApplicationEventListener;

@Configuration
public class ServerConfigeration {

  @Bean
  public PushServiceApplicationEventListener pushServiceApplicationEventListener(StringRedisTemplate redisTemplate, @Value("${consumer.pushservice.topic}") String topic) {
      return new PushServiceApplicationEventListener(redisTemplate, topic);
  }
  
}
