package com.easemob.weichat.integration.rest.mvc.growingio.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.easemob.weichat.integration.constants.IntegrationStatus;
import com.easemob.weichat.integration.modes.IntegrationResp;
import com.easemob.weichat.integration.rest.mvc.growingio.service.IGrowingService;
import com.easemob.weichat.models.data.ApiResponse;
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
	
	@RequestMapping(value="/v1/integration/tenants/{tenantId}/js", method = RequestMethod.GET)
	public ResponseEntity<IntegrationResp> getGrowingJS(HttpServletRequest request,
            @PathVariable("tenantId") int tenantId) {
		
		assertLogin();
        assertTenantAdminLogin(tenantId);
        
        IntegrationResp respdata = new  IntegrationResp();
        IntegrationStatus status = growingService.getGrowingIOJS(tenantId, respdata);
        
        return getReturnValue(status,respdata);
		
	}
	
	
	@RequestMapping(value="/v1/integration/tenants/{tenantId}/dashboard", method = RequestMethod.GET)
	public ResponseEntity<IntegrationResp> getDashboard(HttpServletRequest request,
            @PathVariable("tenantId") int tenantId) {
		
		assertLogin();
        assertTenantAdminLogin(tenantId);
        
        IntegrationResp respdata = new  IntegrationResp();
        IntegrationStatus status = growingService.getGrowingDashBoard(tenantId, respdata);
        
        return getReturnValue(status,respdata);
		
	}
	
	@RequestMapping(value="/v1/integration/tenants/{tenantId}/regedit", method = RequestMethod.GET)
	public ResponseEntity<IntegrationResp> doRegedit(HttpServletRequest request,
            @PathVariable("tenantId") int tenantId) {
		
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
	
	@RequestMapping(value="/v1/integration/tenants/{tenantId}/servicesessionid/{servicesessionid}/tracks", method = RequestMethod.GET)
	public ResponseEntity<IntegrationResp> getGrowingIOUserTrack(HttpServletRequest request,
            @PathVariable("tenantId") int tenantId,@PathVariable("servicesessionid") String servicesessionid) {
		
		assertLogin();
        assertTenantAdminLogin(tenantId);
        
        IntegrationResp respdata = new  IntegrationResp();
        IntegrationStatus status = growingService.getGrowingIOTracksUser(tenantId, servicesessionid, respdata);
        
        return getReturnValue(status,respdata);
		
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

	 
	    protected ResponseEntity<IntegrationResp> createSucResponse(Object entity, HttpStatus httpStatus) {
	    	IntegrationResp response = new IntegrationResp();
	        response.setStatus(IntegrationResp.STATUS_OK);
	        if(entity.getClass().equals(Page.class)){
	        	response.setEntity(createResponseEntity(entity));
	        }else{
	        	response.setEntity(entity);
	        }
	        
	        return new ResponseEntity<IntegrationResp>(response, httpStatus);
	    }

	    protected ResponseEntity<ApiResponse> createResponseEntity(Page<?> page) {
	        ApiResponse response = new ApiResponse();
	        response.setStatus(ApiResponse.STATUS_OK);
	        response.setFirst(page.isFirst());
	        response.setLast(page.isLast());
	        response.setTotalPages(page.getTotalPages());
	        response.setTotalElements(page.getTotalElements());
	        response.setSize(page.getSize());
	        response.setNumber(page.getNumber());
	        response.setNumberOfElements(page.getNumberOfElements());
	        response.setEntities(page.getContent());
	        if(response.getNumberOfElements() == 0){
	                return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	        }else{
	                return new ResponseEntity<ApiResponse>(response, HttpStatus.NOT_FOUND);
	        }

	    }


	
}
