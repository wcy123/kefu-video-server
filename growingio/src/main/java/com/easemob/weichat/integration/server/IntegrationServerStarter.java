package com.easemob.weichat.integration.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableAutoConfiguration
@EnableTransactionManagement
@EntityScan("com.easemob.weichat.integration.rest.mvc.growingio.jpa")
@EnableJpaRepositories("com.easemob.weichat.integration.rest.mvc.growingio.jpa")
@EnableFeignClients(value="com.easemob.weichat.integration.rest.mvc.growingio.remote")
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.easemob.weichat")
public class IntegrationServerStarter {
    public static void main(String[] args) {
        SpringApplication.run(IntegrationServerStarter.class, args);
    }
} 