package com.easemob.weichat.integration.conf;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.easemob.weichat.integration.consumer.IntegrationEventConsumer;
import com.easemob.weichat.integration.listener.IntegrationEventListener;
import com.easemob.weichat.integration.rest.mvc.growingio.service.IGrowingService;
import com.easemob.weichat.integration.rest.mvc.growingio.service.IntegrationWorker;
import com.easemob.weichat.service.events.EventServer;
import com.easemob.weichat.service.message.BlockingRedisMessageConsumer;
import com.easemob.weichat.service.message.IMessageConsumer;

//@Configuration
public class ConsumerConfiguration {

	@Autowired
    private MetricRegistry metricRegistry;
	
	@Value("${kf.integration.consumer.num}")
    private int consumerNum;
		
	@Value("${kf.integration.loaddata.topic}") 
	private String topic;
	
	@Bean
    public IMessageConsumer integrationEventConsumer(StringRedisTemplate redisTemplate,IGrowingService growingService) throws URISyntaxException {
    	IntegrationWorker worker = new IntegrationWorker(growingService);
        Timer timer = metricRegistry.timer("integration:topic.consumer.timer");
        BlockingRedisMessageConsumer cs = new BlockingRedisMessageConsumer(
                redisTemplate,
                topic, consumerNum, timer);
        cs.start(worker);
        return cs;
    }
	
	@Bean
	public IntegrationEventConsumer serviceSessionMessageEventConsumer(ApplicationEventPublisher eventPublisher, EventServer eventServer, @Value("${kefu.kafka.topic}") String eventTopic) {
		return new IntegrationEventConsumer(eventPublisher, eventServer, eventTopic);
	}
	
	@Bean
	public IntegrationEventListener integrationEventListener(StringRedisTemplate redisTemplate) {
		return new IntegrationEventListener(redisTemplate, topic);
	}
	
}
