package com.easemob.kefu.rtcmedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.easemob.kefu.rtcmedia")
public class RtcMediaServerStarter {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(RtcMediaServerStarter.class, args);
    }
}
