package com.easemob.weichat.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import com.easemob.weichat.models.entity.UserExportFile;

import lombok.extern.slf4j.Slf4j;

@FeignClient(name = "${service.client.webappreadwrite.feign.name}", url = "${service.client.webappreadwrite.feign.url:}", fallback = WebappReadWriteClient.HystrixClientFallback.class)
public interface WebappReadWriteClient extends IWebappReadWriteClient {
	@Slf4j
	@Component
	public class HystrixClientFallback implements WebappReadWriteClient {
		@Override
		public UserExportFile saveUserExportFile(String tenantId, String agentUserId, UserExportFile exportFile) {
			log.warn(" webappreadwrite server fallback, post:tenantId={},agentUserId={},exportfiledata={} ", tenantId,agentUserId,exportFile);
			return null;
		}

	}
}
