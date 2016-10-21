package com.easemob.weichat.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.easemob.weichat.models.entity.UserExportFile;

public interface IWebappReadWriteClient {

	@RequestMapping(value="/v1/tenants/{tenantId}/agents/{agentUserId}/exportfiles",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
	public UserExportFile saveUserExportFile(@PathVariable("tenantId")String tenantId,
											@PathVariable("agentUserId")String agentUserId,
											@RequestBody UserExportFile exportFile);
	
}
