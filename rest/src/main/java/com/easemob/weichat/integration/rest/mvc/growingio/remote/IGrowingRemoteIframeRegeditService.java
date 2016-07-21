package com.easemob.weichat.integration.rest.mvc.growingio.remote;


import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.easemob.weichat.integration.modes.DashboardResp;
import com.easemob.weichat.integration.modes.InstallSdkResp;

       
/**
 * growingio的初始化类接口
 * 
 * @author likai
 *
 */  
@FeignClient(name = "${kefu.growingio.name}Regedit", url = "${kefu.growingio.url.iframe}")
public interface IGrowingRemoteIframeRegeditService {
	
	 @RequestMapping(method = RequestMethod.GET,value = "widgets/dashboard") 
	 ResponseEntity<DashboardResp> dashboard(@RequestParam Map<String,String> req,@RequestHeader("Authorization") String authorization);
	 
	 @RequestMapping(method = RequestMethod.GET,value = "widgets/projects/{project_id}/install_sdk")			 
	 ResponseEntity<InstallSdkResp> installSdk( @RequestParam Map<String,String> req ,@RequestHeader("Authorization") String authorization, @PathVariable("project_id") String project_id);
	 
}
