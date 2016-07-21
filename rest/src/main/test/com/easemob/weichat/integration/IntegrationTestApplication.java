package com.easemob.weichat.integration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.easemob.weichat.integration.listener.TestEventListener;


@EnableAutoConfiguration
@EnableTransactionManagement
@EntityScan("com.easemob.weichat.integration.rest.mvc.growingio.jpa")
@EnableJpaRepositories("com.easemob.weichat.integration.rest.mvc.growingio.jpa")
@EnableFeignClients(value="com.easemob.weichat.integration.rest.mvc.growingio.remote")
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.easemob.weichat")
public class IntegrationTestApplication {

	@Bean
	public TestEventListener testEventListener() {
		return new TestEventListener();
	}	
}
