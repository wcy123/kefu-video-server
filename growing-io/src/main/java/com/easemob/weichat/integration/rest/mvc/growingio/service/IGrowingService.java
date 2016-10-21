package com.easemob.weichat.integration.rest.mvc.growingio.service;

import java.util.List;

import com.easemob.weichat.integration.constants.IntegrationStatus;
import com.easemob.weichat.integration.modes.GrowingIoInfo;
import com.easemob.weichat.integration.modes.IntegrationResp;
import com.easemob.weichat.integration.modes.IntgerationGrowingInfo;


public interface IGrowingService {
	
	IntegrationStatus getGrowingIOJS(int tenantId , IntegrationResp resp);
	
	IntegrationStatus getGrowingDashBoard(int tenantId , IntegrationResp resp);
	
	IntegrationStatus doGrowingIORegedit(int tenantId , IntegrationResp resp);
	
	IntegrationStatus isGrowingIOUserInfo(int tenantId ,IntegrationResp resp);
	
	IntegrationStatus getGrowingIOTracksUser(int tenantId, String servicesessionId ,IntegrationResp resp);
	
	IntegrationStatus loadGrowingIOInfo(String integrationMessage );
	
    void processData(List<GrowingIoInfo> array,IntgerationGrowingInfo info);
    
}
