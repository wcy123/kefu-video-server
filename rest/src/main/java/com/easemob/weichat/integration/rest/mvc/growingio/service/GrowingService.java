package com.easemob.weichat.integration.rest.mvc.growingio.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.easemob.weichat.integration.constants.IntegrationStatus;
import com.easemob.weichat.integration.modes.DashboardReq;
import com.easemob.weichat.integration.modes.DashboardResp;
import com.easemob.weichat.integration.modes.DelegateRegisterDataReq;
import com.easemob.weichat.integration.modes.DelegateRegisterDataResp;
import com.easemob.weichat.integration.modes.EventReq;
import com.easemob.weichat.integration.modes.GrowingIoInfo;
import com.easemob.weichat.integration.modes.InstallSdkReq;
import com.easemob.weichat.integration.modes.InstallSdkResp;
import com.easemob.weichat.integration.modes.IntegrationResp;
import com.easemob.weichat.integration.modes.IntgerationGrowingInfo;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.GrowingIoCompanyRepository;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.OptionsRepository;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.ServicesessionTrackRepository;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.UserTracksRepository;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity.GrowingIoCompanyAction;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity.OptionAction;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity.ServicesessionTrack;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity.UserTracks;
import com.easemob.weichat.integration.rest.mvc.growingio.remote.GrowingIoServiceMgr;
import com.easemob.weichat.models.entity.AgentUser;
import com.easemob.weichat.models.util.JSONUtil;
import com.easemob.weichat.persistence.jpa.AgentUserRepository;
import com.google.common.collect.Maps;

import feign.FeignException;
import joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GrowingService implements IGrowingService{

	public static final String INTEGATION_TOPIC = "kf:integation:access:token";
	
	@Autowired
	private AgentUserRepository agentUserRepository;
	
	@Autowired
	private GrowingIoCompanyRepository growingIoCompanyRepository ; 
	
	@Autowired
	private GrowingIoServiceMgr growingIOServiceDecorator ;
	
	@Autowired
	private ServicesessionTrackRepository servicesessionTrackRepository ;
	
	@Autowired
	private OptionsRepository optionsRepository;
	
	@Autowired
    private EntityManagerFactory emf;

	@Autowired
	private  UserTracksRepository userTracksRepository;
	
	@Value("${kefu.growingio.client_id}")
	private  String client_id ; 
	
	@Value("${kefu.growingio.client_secret}")
	private  String client_secret;
	
	private boolean isGrowingIOFunc(int tenantId){
		boolean sign  =  false ; 
		OptionAction option = optionsRepository.findByTenantId(tenantId, "growingioEnable");
		if(option!=null){
			sign = Boolean.parseBoolean(option.getOptionValue());
		}
		
		return sign ;
	}
	
	@Override
	public IntegrationStatus getGrowingIOJS(int tenantId, IntegrationResp resp) {
		IntegrationStatus status = IntegrationStatus.NOKNOW ;
		if(tenantId != 0){
			boolean isGrowingIoSingn = isGrowingIOFunc(tenantId);
			if(!isGrowingIoSingn){
				return IntegrationStatus.GROWING_TENANTID_REGEDIT_OPTION_ERROR ;
			}
			GrowingIoCompanyAction action = growingIoCompanyRepository.findByTenantId(tenantId);
			if(action==null ){
				log.debug(String.format("%d,not is growingio_user , need regedit", Long.valueOf(tenantId)));
				status = delegateRegister(tenantId);	
			}else{
				try{
			    	InstallSdkReq req = new InstallSdkReq();
			    	req.setClient_id(client_id);
			    	req.setUser_id(action.getUserId().toString());
			    	req.setTimestamp(new Date().getTime());
			    	
			    	ResponseEntity<InstallSdkResp> installsdkresp = growingIOServiceDecorator.installSdk(req, action.getProjectId(), tenantId);
			    	if(installsdkresp!=null && installsdkresp.getStatusCode().equals(HttpStatus.OK)){
			    		resp.setEntity(installsdkresp.getBody());
			    		status = IntegrationStatus.SUCCESS;
			    	}else{
			    		status = IntegrationStatus.GROWING_TENANTID_REMOTE_ERROR;
			    		status.setTempStr(String.format(status.getDescription(), tenantId));
			    	}
		    	}catch (FeignException e){
					log.debug(e.getMessage(),e);
					status = IntegrationStatus.GROWING_TENANTID_REGEDIT_CONN_ERROR ;
					status.setTempStr(String.format(status.getDescription(), tenantId,growingIOServiceDecorator.analyzeFeignException(e)));
				}catch(Exception e){
					log.debug(e.getMessage(),e);
					status = IntegrationStatus.GROWING_TENANTID_REGEDIT_CONN_ERROR ;
					status.setTempStr(String.format(status.getDescription(), tenantId,e.getMessage()));
				}
				
			}
			
		}else{
			status = IntegrationStatus.GROWING_TENANTID_NULL;
		}
		return status;
	}

	private void insertRegeditInfo(DelegateRegisterDataReq req,DelegateRegisterDataResp resp,int tenantId){
		GrowingIoCompanyAction newaction  = new GrowingIoCompanyAction();
		newaction.setAccountId(resp.getAccount_id());
		newaction.setMail(req.getEmail());
		newaction.setProjectId( resp.getProject_id());
		newaction.setTenantId(tenantId);
		newaction.setUserId(resp.getUser_id());
		newaction.setCreateDateTime(new Date());
		newaction.setRefreshToken(resp.getRefresh_token());
		EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(newaction);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.error("Failed to create regedit for growingIO {}", newaction, e);
            throw e ;
        }	
	}
	
	private IntegrationStatus delegateRegister(int tenantId){
		IntegrationStatus status = IntegrationStatus.NOKNOW ; 
		
		DelegateRegisterDataReq req = new DelegateRegisterDataReq();
		
	    List<AgentUser>	agentlist = agentUserRepository.findByTenantId(tenantId);
	    if(!agentlist.isEmpty()){
	    	req.setCompany(agentlist.get(0).getNicename());
	    	
	    	req.setEmail(String.format("%s@easemob.com", agentlist.get(0).getUserId()));
	    	req.setTimestamp(new Date().getTime());
	  
	    	try{
	    		ResponseEntity<DelegateRegisterDataResp> remoteresp = growingIOServiceDecorator.delegateRegister(req,tenantId);
		    	if(remoteresp!=null&&remoteresp.getStatusCode().equals(HttpStatus.OK)){
		    		insertRegeditInfo(req,remoteresp.getBody(),tenantId);
		    		status = IntegrationStatus.SUCCESS ;
		    	}
			}catch (FeignException e){
				log.debug(e.getMessage(),e);
				status = IntegrationStatus.GROWING_TENANTID_REGEDIT_CONN_ERROR ;
				status.setTempStr(String.format(status.getDescription(), tenantId,growingIOServiceDecorator.analyzeFeignException(e)));
			}
	    	
	    }else{
	    	log.error(String.format("%d, the AgentUser is not use growingin service", tenantId));
	    	status = IntegrationStatus.GROWING_TENANTID_NOT_ANGET;
	    }
		return status;
	}

	@Override
	public IntegrationStatus getGrowingDashBoard(int tenantId, IntegrationResp resp) {
		IntegrationStatus status = IntegrationStatus.NOKNOW ;
		if(tenantId != 0){
			
			boolean isGrowingIoSingn = isGrowingIOFunc(tenantId);
			if(!isGrowingIoSingn){
				return IntegrationStatus.GROWING_TENANTID_REGEDIT_OPTION_ERROR ;
			}
			
			GrowingIoCompanyAction action = growingIoCompanyRepository.findByTenantId(tenantId);
			if(action==null ){
				log.debug(String.format("%d,没有注册Growing账户，需要注册", tenantId));
				status = delegateRegister(tenantId);	
			}else{
				status = IntegrationStatus.SUCCESS;
			}
			
			if(status == IntegrationStatus.SUCCESS){
				try{
					DashboardReq req = new DashboardReq();
					req.setTimestamp(new Date().getTime());
					req.setUser_id(action.getUserId());
					req.setClient_id(client_id);
					ResponseEntity<DashboardResp> dashboardResp = growingIOServiceDecorator.dashboard( req, tenantId);
					if(dashboardResp!=null&&dashboardResp.getStatusCode().equals(HttpStatus.OK)){
						resp.setEntity(dashboardResp.getBody());
			    		status = IntegrationStatus.SUCCESS ;
			    	}
				
				}catch (FeignException e){
					log.debug(e.getMessage(),e);
					status = IntegrationStatus.GROWING_TENANTID_REGEDIT_CONN_ERROR ;
					status.setTempStr(String.format(status.getDescription(), tenantId,growingIOServiceDecorator.analyzeFeignException(e)));
				}catch(Exception e){
					log.debug(e.getMessage(),e);
					status = IntegrationStatus.GROWING_TENANTID_REGEDIT_CONN_ERROR ;
					status.setTempStr(String.format(status.getDescription(), tenantId,e.getMessage()));
				}
			}else{
		    	log.debug(String.format("%d,经过注册以后，在growing_io_company表中没有数据", tenantId));
				status = IntegrationStatus.GROWING_TENANTID_NOT_ANGET;
		    }
			
		}else{
			status = IntegrationStatus.GROWING_TENANTID_NULL;
		}
		
		return status;
	}

	@Override
	public IntegrationStatus doGrowingIORegedit(int tenantId, IntegrationResp resp) {
		IntegrationStatus status = IntegrationStatus.NOKNOW ;
		boolean isGrowingIoSingn = isGrowingIOFunc(tenantId);
		if(!isGrowingIoSingn){
			return IntegrationStatus.GROWING_TENANTID_REGEDIT_OPTION_ERROR ;
		}
		
		GrowingIoCompanyAction action = growingIoCompanyRepository.findByTenantId(tenantId);
		if(action==null ){
			status = delegateRegister(tenantId);	
		}else{
			status = IntegrationStatus.SUCCESS;
		}
		
		return status;
	}


	
	@Override
	public IntegrationStatus isGrowingIOUserInfo(int tenantId, IntegrationResp resp) {
		IntegrationStatus status = IntegrationStatus.NOKNOW ;
		
		boolean isGrowingIoSingn = isGrowingIOFunc(tenantId);
		if(!isGrowingIoSingn){
			return IntegrationStatus.GROWING_TENANTID_REGEDIT_OPTION_ERROR ;
		}
		
		GrowingIoCompanyAction action = growingIoCompanyRepository.findByTenantId(tenantId);
		if(action==null ){
			status = IntegrationStatus.GROWING_TENANTID_REGEDIT_ERROR;
		}else{
			
			status = IntegrationStatus.SUCCESS;
		}
		return status;
	}

	
	@Override
	public IntegrationStatus loadGrowingIOInfo(String integrationMessage) {
		IntegrationStatus status = IntegrationStatus.NOKNOW ;
	
		try{
			IntgerationGrowingInfo info = JSONUtil.getObjectMapper().readValue(integrationMessage, IntgerationGrowingInfo.class);
			GrowingIoCompanyAction action = growingIoCompanyRepository.findByTenantId(info.getTenantId());
			if(action==null ){
		    	status = IntegrationStatus.GROWING_TENANTID_NOT_ANGET;
			}else{
				
				EventReq req = new EventReq();
		        req.setProject_id(action.getProjectId());
		        req.setUser_id(info.getGrowingioId());
		        req.setTenantId(action.getTenantId());
		        ResponseEntity<String> respinfo = growingIOServiceDecorator.event(req);
		        if(respinfo.getStatusCode() == HttpStatus.OK){
		        	String json = respinfo.getBody() ;
		        	if(!Strings.isNullOrEmpty(json)){
		        		GrowingIoInfo[][] array = JSONUtil.getObjectMapper().readValue(json, GrowingIoInfo[][].class);
		        		List<GrowingIoInfo>  nearList = null;
		        		if(array!=null&&  array.length > 0 ){
		        			for(int i = 0 ; i < array.length ; i ++){
		        				List<GrowingIoInfo> list =Arrays.asList(array[i]) ; 
		        				if(nearList == null){
		        					nearList = list;
		        				}else{
		        					if(nearList.get(0).getTimestamp().getTime() <=list.get(0).getTimestamp().getTime()){
		        						nearList =list;
		        					}
		        				}
		        				
		        				processData(list,info);
		        			}
		        			
		        			String arrayjson = JSONUtil.getObjectMapper().writeValueAsString(nearList);
		        			processServiceSession(arrayjson,info);
		        		}else{
		        			log.debug(String.format("%d,%s,is not trace", info.getTenantId(),info.getUserId()));
		        		}
		        	}
		        	status = IntegrationStatus.SUCCESS;
		        }else{
			    	status = IntegrationStatus.GROWING_TENANTID_EVENT_ERROR;
			    	status.setTempStr(String.format(status.getDescription(), info.getTenantId(),req.getUser_id()));
		        }

			}
		}catch(Exception e){
			log.debug(e.getMessage(),e);
			status = IntegrationStatus.NOKNOW;
		}

        return status ;
	}
	
  private void processServiceSession(String growingJson,IntgerationGrowingInfo info){
	  ServicesessionTrack track = new ServicesessionTrack();
	  track.setTenantId(info.getTenantId());
	  track.setVisitorId(UUID.fromString(info.getUserId()));
	  track.setGrowingioId(info.getGrowingioId());
	  track.setServicesessionId(UUID.fromString(info.getServiceSessionId()));
	  track.setContext(growingJson);
	  servicesessionTrackRepository.save(track);
  }
	
  @Override
  public void processData(List<GrowingIoInfo> array,IntgerationGrowingInfo info){
    	
    	for(GrowingIoInfo growinginfo : array){
    		try{
    			UserTracks visit = new UserTracks();
    			buildUserTracks(info,growinginfo,visit);
    			userTracksRepository.save(visit);
        	}catch (Exception e){
        		log.debug(e.getMessage(),e);
        	}
    		
    	}
    }
    
    private HashMap<String, String> covnterMapValue(Map<String, Object> map){
    	HashMap<String, String> returnmap = Maps.newHashMap() ; 
    	for(Map.Entry<String,Object> entry : map.entrySet()){
    		returnmap.put(entry.getKey(), entry.getValue().toString());
    	}
    	
    	return returnmap ;
    }
    
   
    
    private void buildUserTracks(IntgerationGrowingInfo info,GrowingIoInfo growinginfo,Object source){
        growingIOServiceDecorator.setFieldValue(source, "tenantId",info.getTenantId());
        growingIOServiceDecorator.setFieldValue(source, "visitorId", UUID.fromString(info.getUserId()));
        growingIOServiceDecorator.setFieldValue(source, "growingioId",info.getGrowingioId());
        growingIOServiceDecorator.setFieldValue(source, "timestamp",growinginfo.getTimestamp());
        growingIOServiceDecorator.setFieldValue(source, "attributes",covnterMapValue(growinginfo.getAttrs()));
        growingIOServiceDecorator.setFieldValue(source, "type",growinginfo.getType());
    }

	@Override
	public IntegrationStatus getGrowingIOTracksUser(int tenantId, String servicesessionId,
			IntegrationResp resp) {
		
		
		boolean isGrowingIoSingn = isGrowingIOFunc(tenantId);
		if(!isGrowingIoSingn){
			return IntegrationStatus.GROWING_TENANTID_REGEDIT_OPTION_ERROR ;
		}
		
		IntegrationStatus status = IntegrationStatus.NOKNOW ;
		GrowingIoCompanyAction action = growingIoCompanyRepository.findByTenantId(tenantId);
		if(action==null ){
			log.debug(String.format("%d,没有注册Growing账户，需要注册", Long.valueOf(tenantId)));
			status = IntegrationStatus.GROWING_TENANTID_REGEDIT_ERROR;
		}else{
			ServicesessionTrack track = servicesessionTrackRepository.findbyServicesessionId(UUID.fromString(servicesessionId));
			if(track!=null){
				resp.setEntity(track.getContext());
				status = IntegrationStatus.SUCCESS;
			}
			
		}
		return status;
		
	}

	
}
