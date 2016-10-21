package com.easemob.weichat.integration.rest.mvc.growingio.service;

import com.easemob.weichat.service.message.BlockingRedisMessageConsumer.Worker;

import lombok.extern.slf4j.Slf4j;



@Slf4j
public class IntegrationWorker implements Worker {

	private IGrowingService growingService ; 
	
	public IntegrationWorker(IGrowingService growingService){
		this.growingService = growingService;
	}
	
    @Override
    public void run(String message) throws Exception {
        log.info("IntegrationWorker run message:{}", message);
        growingService.loadGrowingIOInfo(message);
    }

}