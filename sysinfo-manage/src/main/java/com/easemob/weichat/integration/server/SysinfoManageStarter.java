package com.easemob.weichat.integration.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.easemob.weichat")
public class SysinfoManageStarter {
    public static void main(String[] args) {
        SpringApplication.run(SysinfoManageStarter.class, args);
    }
} 