package com.easemob.weichat.integration.rest.mvc.growingio.remote;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.easemob.weichat.integration.modes.DelegateRegisterDataReq;
import com.easemob.weichat.integration.modes.DelegateRegisterDataResp;
import com.easemob.weichat.integration.modes.UpdateRegisterDataReq;
import com.easemob.weichat.integration.modes.UpdateRegisterDataResp;

/**
 * growingio的账户管理类接口
 * 
 * @author likai
 *
 */                                          
@FeignClient(name = "${kefu.growingio.name}Regedit", url = "${kefu.growingio.url.regedit}")
public interface IGrowingRemoteRegeditService {
	
	 @RequestMapping(method = RequestMethod.POST,value = "api/v1/users/delegate-register")			 
	 ResponseEntity<DelegateRegisterDataResp> delegateRegister( @RequestBody DelegateRegisterDataReq req, @RequestHeader("Authorization") String authorization );
	 
	 
	 @RequestMapping(method = RequestMethod.POST,value = "api/v1/oauth/access_token")			 
	 ResponseEntity<UpdateRegisterDataResp> updateRegister(@RequestBody UpdateRegisterDataReq req,@RequestHeader("Authorization") String authorization);
	 
}
