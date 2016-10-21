package com.easemob.weichat.integration.rest.mvc.growingio.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * growingio的信息获取接口
 * 
 * @author likai
 *
 */  

@FeignClient(name = "${kefu.growingio.name}Event", url = "${kefu.growingio.url.event}")
public interface IGrowingRemoteEventService {
	 @RequestMapping(method = RequestMethod.GET,value = "_private/projects/{project_id}/users/{user_id}/events") 
	 ResponseEntity<String> event(@RequestHeader("cookie")String cookie ,@PathVariable("project_id") String project_id,@PathVariable("user_id") String user_id); 
}