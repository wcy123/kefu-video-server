package com.easemob.weichat.integration.rest.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.easemob.weichat.integration.data.NewUserTaskData.AgentUserTaskDone;
import com.easemob.weichat.integration.rest.service.INewUserTaskService;
import com.easemob.weichat.models.data.ApiResponse;
import com.easemob.weichat.rest.mvc.AbstractController;

import lombok.extern.slf4j.Slf4j;
/**
 * @author shengyp
 * @since 09/28/16
 */

@Slf4j
@RestController
public class NewRegisterUserTaskController extends AbstractController{
    @Autowired
    private INewUserTaskService newUserTaskService;

    @RequestMapping(value = "/v1/tenants/{tenantId}/agents/{agentId}/checkisnewuser",method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> getNewUserTasks (@PathVariable("tenantId") Integer tenantId,
            @PathVariable("agentId") String agentId){
        assertTenantLogin(tenantId);
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setStatus(ApiResponse.STATUS_OK);
        apiResponse.setEntity(newUserTaskService.doGetNewUserTaskList(tenantId, agentId));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/tenants/{tenantId}/agents/{agentId}/tasksdone", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ApiResponse> agentUserTaskDone(@PathVariable("tenantId") Integer tenantId,
            @PathVariable("agentId") String agentId, @RequestBody AgentUserTaskDone agentUserTaskDone) {
        assertTenantLogin(tenantId);
        ApiResponse apiResponse = new ApiResponse();
        newUserTaskService.doAgentUserTaskDone(tenantId, agentId, agentUserTaskDone.getTaskDone());
        
        apiResponse.setStatus(ApiResponse.STATUS_OK);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
