package com.easemob.weichat.client;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author shawn
 *
 */
@Configuration
@EnableFeignClients(value = "com.easemob.weichat.client")
public class FeignClientConfiguration {

}
