package com.easemob.weichat.integration.rest.mvc.growingio.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easemob.weichat.integration.constants.IntegrationStatus;
import com.easemob.weichat.integration.modes.IntegrationResp;
import com.easemob.weichat.integration.paging.PagingRequest;
import com.easemob.weichat.integration.paging.PagingResult;
import com.easemob.weichat.integration.persistence.UserTracksCassandraTemplate;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity.UserTracks;
import com.easemob.weichat.integration.rest.mvc.growingio.service.IGrowingService;
import com.easemob.weichat.rest.mvc.AbstractController;
import com.netflix.servo.util.Strings;

/**
 * @author likai
 * @date 2016年6月21日
 */

@RestController
public class GrowingIOServiceController extends AbstractController  {
	
	
	@Autowired
    private IGrowingService growingService ;
	
	@Autowired
	private UserTracksCassandraTemplate userTracksCassandraTemplate;
	
	@RequestMapping(value="/v1/integration/tenants/{tenantId}/js", method = RequestMethod.GET)
	public ResponseEntity<IntegrationResp> getGrowingJS(@PathVariable("tenantId") int tenantId) {
		
		assertLogin();
        assertTenantAdminLogin(tenantId);
        
        IntegrationResp respdata = new  IntegrationResp();
        IntegrationStatus status = growingService.getGrowingIOJS(tenantId, respdata);
        
        return getReturnValue(status,respdata);
		
	}
	
	
	@RequestMapping(value="/v1/integration/tenants/{tenantId}/dashboard", method = RequestMethod.GET)
	public ResponseEntity<IntegrationResp> getDashboard(@PathVariable("tenantId") int tenantId) {
		
		assertLogin();
        assertTenantAdminLogin(tenantId);
        
        IntegrationResp respdata = new  IntegrationResp();
        IntegrationStatus status = growingService.getGrowingDashBoard(tenantId, respdata);
        
        return getReturnValue(status,respdata);
		
	}
	
	@RequestMapping(value="/v1/integration/tenants/{tenantId}/regedit", method = RequestMethod.GET)
	public ResponseEntity<IntegrationResp> doRegedit(@PathVariable("tenantId") int tenantId) {
		
		assertLogin();
        assertTenantAdminLogin(tenantId);
        
        IntegrationResp respdata = new  IntegrationResp();
        IntegrationStatus status = growingService.doGrowingIORegedit(tenantId, respdata);
        
        return getReturnValue(status,respdata);
		
	}
	
	@RequestMapping(value="/v1/integration/tenants/{tenantId}/authority", method = RequestMethod.GET)
	public ResponseEntity<IntegrationResp> authority(@PathVariable("tenantId") int tenantId) {
		
		assertLogin();
        assertTenantAdminLogin(tenantId);
        
        IntegrationResp respdata = new  IntegrationResp();
        IntegrationStatus status = growingService.doGrowingIORegedit(tenantId, respdata);
        
        return getReturnValue(status,respdata);
		
	}
	
	
	@RequestMapping(value="/v1/integration/tenants/{tenantId}/userinfo", method = RequestMethod.GET)
	public ResponseEntity<IntegrationResp> isGrowingIOUserInfo(HttpServletRequest request,
            @PathVariable("tenantId") int tenantId) {
		
		assertLogin();
        assertTenantAdminLogin(tenantId);
        
        IntegrationResp respdata = new  IntegrationResp();
        IntegrationStatus status = growingService.isGrowingIOUserInfo(tenantId, respdata);
        
        return getReturnValue(status,respdata);
		
	}
	
	@RequestMapping(value="/v1/integration/tenants/{tenantId}/servicesessions/{serviceSessionId}/tracks", method = RequestMethod.GET)
	public ResponseEntity<IntegrationResp> getGrowingIOUserTrack(
            @PathVariable("tenantId") int tenantId,@PathVariable("serviceSessionId") String serviceSessionId) {
		
		assertLogin();
        assertTenantAdminLogin(tenantId);
        
        IntegrationResp respdata = new  IntegrationResp();
        IntegrationStatus status = growingService.getGrowingIOTracksUser(tenantId, serviceSessionId, respdata);
        
        return IntegrationStatus.SUCCESS==status
            ?createSucResponse(respdata.getEntity(), HttpStatus.OK)
            :createFailResponseByStatus(status, HttpStatus.FORBIDDEN,tenantId,serviceSessionId);
		
	}
	
	private ResponseEntity<IntegrationResp> createFailResponseByStatus(IntegrationStatus integrationStatus,
      HttpStatus httpStatus, int tenantId, String serviceSessionId) {
	  IntegrationResp response = new IntegrationResp();
      response.setStatus(IntegrationResp.STATUS_FAIL);
      String errorDesc=integrationStatus.getDescription();
      switch(integrationStatus){
        case GROWING_TENANTID_REMOTE_ERROR:
        case GROWING_TENANTID_REGEDIT_ERROR:
        case GROWING_TENANTID_REGEDIT_CONN_ERROR:
            errorDesc=String.format(errorDesc, tenantId);
            break;
        case GROWING_TENANTID_USER_ERROR:
        case GROWING_TENANTID_EVENT_ERROR:
            errorDesc=String.format(errorDesc, tenantId,serviceSessionId);
            break;
        default:
          break;
        
      }
      response.setErrorDescription(errorDesc);
      return new ResponseEntity<IntegrationResp>(response, httpStatus);
  }


  /**
	 * 获取访客的访问轨迹
	 * @param request
	 * @param tenantId
	 * @param visitorUserId
	 * @param pagingRequest
	 * @return
	 */
	@RequestMapping(value="/v1/integration/tenants/{tenantId}/visitorusers/{visitorUserId}/tracks", method = RequestMethod.GET)
	public ResponseEntity<PagingResult<UserTracks>> getVisitorTracks(
			@PathVariable("tenantId") int tenantId, @PathVariable("visitorUserId") String visitorUserId,
			@RequestParam PagingRequest pagingRequest){
        assertTenantAdminLogin(tenantId);
        PagingResult<UserTracks> pageingResult=userTracksCassandraTemplate.findVisitorTracks(tenantId, visitorUserId, pagingRequest);
        return  new ResponseEntity<PagingResult<UserTracks>>(pageingResult,HttpStatus.OK);
        
	}
	
	private ResponseEntity<IntegrationResp> getReturnValue(IntegrationStatus status,IntegrationResp respdata ){
		if(status.equals(IntegrationStatus.SUCCESS)){
        	return createSucResponse(respdata,HttpStatus.OK);
        }else{
        	if(Strings.isNullOrEmpty(status.getTempStr())){
        		return createFailResponse(status.getDescription(),HttpStatus.FORBIDDEN);
        	}else{
        		return createFailResponse(status.getTempStr(),HttpStatus.FORBIDDEN);
        	}
        }
	}
	
	private ResponseEntity<IntegrationResp> createFailResponse(String errorDesc, HttpStatus httpStatus) {
		IntegrationResp response = new IntegrationResp();
	        response.setStatus(IntegrationResp.STATUS_FAIL);
	        response.setErrorDescription(errorDesc);
	        return new ResponseEntity<IntegrationResp>(response, httpStatus);
	    }

	 
	private ResponseEntity<IntegrationResp> createSucResponse(Object entity, HttpStatus httpStatus) {
	    	IntegrationResp response = new IntegrationResp();
	        response.setStatus(IntegrationResp.STATUS_OK);
	        if(entity.getClass().equals(Page.class)){
	        	response.setEntity(createResponseEntity(entity));
	        }else{
	        	response.setEntity(entity);
	        }
	        
	        return new ResponseEntity<IntegrationResp>(response, httpStatus);
	    }
	
}
