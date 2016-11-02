package com.easemob.kefu.rtcmedia.configuration;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.easemob.kefu.rtcmedia.dep.MediaServiceApi;
import com.easemob.kefu.rtcmedia.dep.WebAppApi;

@Configuration
@EnableJpaRepositories("com.easemob.kefu.rtcmedia.model")
@EnableTransactionManagement
@EnableFeignClients(clients = {
        WebAppApi.class,
        MediaServiceApi.class
})
@EnableDiscoveryClient
@Profile("production")
public class ProductionConfig {
}
