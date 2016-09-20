package com.easemob.weichat.integration.rest.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.easemob.weichat.integration.data.NewVersionInfo;
import com.easemob.weichat.integration.data.ReceivedId;
import com.easemob.weichat.integration.rest.service.ISysInfoService;
import com.easemob.weichat.models.data.ApiResponse;
import com.easemob.weichat.rest.mvc.AbstractController;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shengyp
 * @since 09/08/16
 */
@Slf4j
@RestController
public class NewSysInfoNotificationController extends AbstractController{
    
    @Autowired
    private ISysInfoService sysInfoService;

    @RequestMapping(value = "/v1/tenants/{tenantId}/agents/{agentId}/news/latest",method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> checkVersionInfoRead (@PathVariable("tenantId") Integer tenantId,
            @PathVariable("agentId") String agentId){
        assertTenantLogin(tenantId);
        ApiResponse apiResponse = new ApiResponse();

        // 检查客服是否已经读取新版本发布信息
        NewVersionInfo newVersionInfoData = sysInfoService.doCheckVerInfoRead(tenantId, agentId);

        apiResponse.setEntity(newVersionInfoData);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/tenants/{tenantId}/agents/{agentId}/news", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> agentUserRead(@PathVariable("tenantId") Integer tenantId,
            @PathVariable("agentId") String agentId, @RequestBody ReceivedId receivedId) {
        assertTenantLogin(tenantId);
        ApiResponse apiResponse = new ApiResponse();
        boolean result = false;

        // 客服已经读取新版本信息，记录在redis数据库中
        result =  sysInfoService.doAgentUserRead(tenantId, agentId, receivedId);

        apiResponse.setEntity(result);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/news", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> addNewVersion(@RequestBody NewVersionInfo newVersionInfoData) {
        ApiResponse apiResponse = new ApiResponse();
        
        log.info("===== new Version info ======  id:{}, Content:{}", newVersionInfoData.getId(), newVersionInfoData.getContent());
        // 新版本信息发布，通知系统
        boolean flag = sysInfoService.doAddNewVersion(newVersionInfoData);
        
        if (flag == true){
            newVersionInfoData.setFlag(true);
        }
        else
        {
            newVersionInfoData.setFlag(false);
        }
        apiResponse.setEntity(newVersionInfoData);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }
}
