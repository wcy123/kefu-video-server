package com.easemob.weichat.integration.rest.mvc.growingio.service;

import java.util.List;

import com.easemob.weichat.integration.constants.IntegrationStatus;
import com.easemob.weichat.integration.modes.GrowingIoInfo;
import com.easemob.weichat.integration.modes.IntegrationResponse;
import com.easemob.weichat.integration.modes.IntgerationGrowingInfo;


public interface IGrowingService {
	
	IntegrationStatus getGrowingIOJS(int tenantId , IntegrationResponse resp);
	
	IntegrationStatus getGrowingDashBoard(int tenantId , IntegrationResponse resp);
	
	IntegrationStatus doGrowingIORegedit(int tenantId , IntegrationResponse resp);
	
	IntegrationStatus isGrowingIOUserInfo(int tenantId ,IntegrationResponse resp);
	
	IntegrationStatus getGrowingIOTracksUser(int tenantId, String servicesessionId ,IntegrationResponse resp);
	
	IntegrationStatus loadGrowingIOInfo(String integrationMessage );
	
    void processData(List<GrowingIoInfo> array,IntgerationGrowingInfo info);
}
