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
import com.easemob.weichat.integration.rest.service.ISysInfoService;
import com.easemob.weichat.models.data.ApiResponse;
import com.easemob.weichat.rest.mvc.AbstractController;

import lombok.Data;
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

    @RequestMapping(value = "/tenant/{tenantId}/agents/{agentId}/news/latest",method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> checkVersionInfoRead (@PathVariable("tenantId") Integer tenantId,
            @PathVariable("agentId") String agentId){
        ApiResponse apiResponse = new ApiResponse();
        NewVersionInfo newVersionInfoData = sysInfoService.getVersionInfo();

        if ( newVersionInfoData.getId() == null || newVersionInfoData.getId().length() == 0){
            // 查看hash中的ID是否有值，如果为空，返回false，不需要发布版本信息通知
            apiResponse.setStatus(ApiResponse.STATUS_FAIL);
            return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        sysInfoService.addTenantIdIntoSet(tenantId.toString());

        if (sysInfoService.checkAgentidExistInSet(tenantId, agentId)) {
            // 如果存在，则不需要向该agent推送
            apiResponse.setStatus(ApiResponse.STATUS_FAIL);
            return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        apiResponse.setStatus(ApiResponse.STATUS_OK);
        apiResponse.setEntity(newVersionInfoData);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/tenant/{tenantId}/agents/{agentId}/news", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> agentUserRead(@PathVariable("tenantId") Integer tenantId,
            @PathVariable("agentId") String agentId, @RequestBody ReceivedId receivedId) {
        boolean result = false;
        ApiResponse apiResponse = new ApiResponse();
        NewVersionInfo savedVersionInfoData = sysInfoService.getVersionInfo();
        
        if ( !savedVersionInfoData.getId().equals(receivedId.getId()) ){
            // 判断收到前端的ID是否当前发布的ID相同。如果不同，临界异常情况，已发布新版本，但用户一直未刷新界面
            // 则用户还是用的旧版本发布的ID，不操作
            apiResponse.setStatus(ApiResponse.STATUS_FAIL);
            apiResponse.setEntity(result);
            return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        if ( savedVersionInfoData.getId() == null || savedVersionInfoData.getId().length() == 0){
            // 查看hash中的ID是否有值，如果为空，返回false，不需要发布版本信息通知
            apiResponse.setStatus(ApiResponse.STATUS_FAIL);
            apiResponse.setEntity(result);
            return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        
        sysInfoService.addTenantIdIntoSet(tenantId.toString());

        if (!sysInfoService.checkAgentidExistInSet(tenantId, agentId)) {
            // 如果该agentId不在SET:agent中，收到该POST请求，往SET:agent中添加该agentId。
            sysInfoService.addAgentidIntoSet(tenantId, agentId);
            result = true;
        }

        apiResponse.setStatus(ApiResponse.STATUS_OK);
        apiResponse.setEntity(result);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/news", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> addNewVersion(@RequestBody NewVersionInfo newVersionInfoData) {
        ApiResponse apiResponse = new ApiResponse();
        NewVersionInfo savedVersionInfoData = sysInfoService.getVersionInfo();

        log.info("===== new Version info ======  id:{}, Content:{}", newVersionInfoData.getId(), newVersionInfoData.getContent());

        // 检查REDIS中是否有上次保存的hash key：id的值存在
        if ( savedVersionInfoData.getId() != null && savedVersionInfoData.getId().length() != 0){
            // 判断是否与REDIS上次保存的key:id的值相同，如果相同，则跳过本次操作
            if (savedVersionInfoData.getId().equals(newVersionInfoData.getId()))
            {
                apiResponse.setStatus(ApiResponse.STATUS_FAIL);
                return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
            }

            // 先删除已经保存版本ID的 SET:agentId
            sysInfoService.deleteAgentUserFromSet();
            
            // 从hash中删除旧的ID值和content值
            sysInfoService.deleteVersionInfo();
        }

        // 保存newVersionInfoData到hash中
        sysInfoService.saveVersionInfo(newVersionInfoData);

        apiResponse.setStatus(ApiResponse.STATUS_OK);
        apiResponse.setEntity(newVersionInfoData);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }
    
    @Data
    private static class ReceivedId {
        private String id;
    }
}
