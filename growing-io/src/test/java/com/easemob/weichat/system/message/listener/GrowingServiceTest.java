package com.easemob.weichat.system.message.listener;

import java.util.Date;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.easemob.weichat.integration.constants.IntegrationStatus;
import com.easemob.weichat.integration.modes.GrowingIoInfo;
import com.easemob.weichat.integration.modes.IntgerationGrowingInfo;
import com.easemob.weichat.integration.paging.PagingRequest;
import com.easemob.weichat.integration.paging.PagingResult;
import com.easemob.weichat.integration.persistence.UserTracksCassandraTemplate;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.GrowingIoCompanyRepository;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.ServicesessionTrackRepository;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity.GrowingIoCompanyAction;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity.ServicesessionTrack;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity.UserTracks;
import com.easemob.weichat.integration.rest.mvc.growingio.service.GrowingService;
import com.easemob.weichat.models.util.JSONUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = {GrowingIOTestApplication.class})
@ActiveProfiles("test")
public class GrowingServiceTest {

  @Autowired 
  private GrowingService growingService ;
  @Autowired
  private GrowingIoCompanyRepository growingIoCompanyRepository;
  @Autowired
  private UserTracksCassandraTemplate userTracksCassandraTemplate;
  @Autowired
  private ServicesessionTrackRepository serviceSessionTrackRepository;
  
  private IntgerationGrowingInfo data;
  private String visitorUserId=UUID.randomUUID().toString();
  private String grUserId="test_grUserId";
  private int tenantId=123456;
  private String serviceSessionId=UUID.randomUUID().toString();
  private String integrationMessage;
  private GrowingIoCompanyAction testCompany;
  
  
  @Before
  public void init() throws Exception{
    data = new IntgerationGrowingInfo();
    data.setTimestamp(System.currentTimeMillis());
    data.setUserId(visitorUserId);
    data.setGrowingioId(grUserId);
    data.setTenantId(tenantId);
    data.setServiceSessionId(serviceSessionId);
    integrationMessage = JSONUtil.getObjectMapper().writeValueAsString(data);
    
    GrowingIoCompanyAction company=new GrowingIoCompanyAction();
    company.setAccountId("testAccountId");
    company.setCreateDateTime(new Date());
    company.setId(12345678L);
    company.setMail("test@test.test");
    company.setProjectId("test_project_id");
    company.setRefreshToken("test_token");
    company.setTenantId(tenantId);
    company.setUserId("test_tenant_grUserID");
    testCompany=growingIoCompanyRepository.saveAndFlush(company);


  }
  
  @After
  public void clear(){
    growingIoCompanyRepository.deleteAll();
    userTracksCassandraTemplate.delete(tenantId, visitorUserId);
    serviceSessionTrackRepository.deleteAll();
  }
  
  
  @Test
  public void testLoadGrowingIOInfo() throws Exception{
    IntegrationStatus status = growingService.loadGrowingIOInfo(integrationMessage);

    PagingRequest pagingRequest=new PagingRequest();
    PagingResult<UserTracks> results=userTracksCassandraTemplate.findVisitorTracks(tenantId, visitorUserId, pagingRequest);
    
    GrowingIoInfo[][] array = JSONUtil.getObjectMapper().readValue(GrowingIOTestApplication.growingIoResponseStr, GrowingIoInfo[][].class);
    int actualLenth=0;
    for(GrowingIoInfo[] arr:array ){
      actualLenth+=arr.length;
    }
    Assert.assertEquals(actualLenth, results.getEntities().size());
    
    int pageSize=10;
    pagingRequest.setLimit(pageSize);
    PagingResult<UserTracks> results1=userTracksCassandraTemplate.findVisitorTracks(tenantId, visitorUserId, pagingRequest);
    Assert.assertEquals(pageSize, results1.getEntities().size());

    pagingRequest.setCursor(results1.getNextCursor());
    PagingResult<UserTracks> results2=userTracksCassandraTemplate.findVisitorTracks(tenantId, visitorUserId, pagingRequest);
    Assert.assertEquals(pageSize, results2.getEntities().size());

    pagingRequest.setCursor(results2.getNextCursor());
    PagingResult<UserTracks> results3=userTracksCassandraTemplate.findVisitorTracks(tenantId, visitorUserId, pagingRequest);
    Assert.assertEquals(actualLenth-2*pageSize, results3.getEntities().size());
    
    ServicesessionTrack sessionTrack=serviceSessionTrackRepository.findbyServicesessionId(serviceSessionId);
    Assert.assertEquals(serviceSessionId, sessionTrack.getServicesessionId());
    Assert.assertEquals(grUserId, sessionTrack.getGrowingioId());
    Assert.assertEquals(visitorUserId, sessionTrack.getVisitorId());
    Assert.assertEquals(tenantId, sessionTrack.getTenantId());

    IntegrationStatus status2 = growingService.loadGrowingIOInfo(integrationMessage);

  }
  
}
