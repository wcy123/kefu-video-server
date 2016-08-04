package com.easemob.weichat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class XdfVisitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(XdfVisitorApplication.class, args);
	}
	
}
