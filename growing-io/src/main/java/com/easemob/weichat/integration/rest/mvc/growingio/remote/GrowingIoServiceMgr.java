package com.easemob.weichat.integration.rest.mvc.growingio.remote;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.easemob.weichat.integration.modes.DashboardReq;
import com.easemob.weichat.integration.modes.DashboardResp;
import com.easemob.weichat.integration.modes.DelegateRegisterDataReq;
import com.easemob.weichat.integration.modes.DelegateRegisterDataResp;
import com.easemob.weichat.integration.modes.EventReqCookie;
import com.easemob.weichat.integration.modes.InstallSdkReq;
import com.easemob.weichat.integration.modes.InstallSdkResp;
import com.easemob.weichat.integration.modes.UpdateRegisterDataReq;
import com.easemob.weichat.integration.modes.UpdateRegisterDataResp;
import com.easemob.weichat.integration.modes.VisitorTrackReq;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.GrowingIoCompanyRepository;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity.GrowingIoCompanyAction;
import com.easemob.weichat.util.HttpUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GrowingIoServiceMgr  {

	public static final String INTEGATION_TOPIC = "kf:integation:access:token:%d";

	public static final String INTEGATION_OATH_FOMAT="Oauth client_id=%s,client_secret=%s";
	
	private static final RequestConfig NOREDIRECTCONFIG = RequestConfig.custom()
        .setSocketTimeout(3000)
        .setConnectTimeout(3000)
        .setConnectionRequestTimeout(3000)
        .setRedirectsEnabled(false)
        .setCircularRedirectsAllowed(false)
        .setRelativeRedirectsAllowed(false)
        .build();
	
	@Value("${kefu.growingio.client_id}")
	private  String client_id ; 
	
	@Value("${kefu.growingio.client_secret}")
	private  String client_secret;
	
	@Value("${kefu.growingio.url.iframe}")
	private String remoteUrl ;
	
	@Value("${kefu.growingio.url.track}")
	private String visitorTrackHost;
	
	private  int repeated_num  = 3;
	
	@Autowired
	private IGrowingRemoteRegeditService growingRemoteService ;

	@Autowired
	private IGrowingRemoteIframeRegeditService growingRemoteIframeRegeditService ;

	@Autowired
	private IGrowingRemoteEventService growingRemoteEventService;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private GrowingIoCompanyRepository growingIoCompanyRepository ;
		
	private void clearGrowingAccessToken(int tenantId){
		String token = String.format(INTEGATION_TOPIC,tenantId);
		stringRedisTemplate.delete(token);
	}

	private void setGrowingAccessToken(int tenantId ,String accessToken,long expire){
		String key = String.format(INTEGATION_TOPIC, tenantId);
		stringRedisTemplate.boundValueOps(key).setIfAbsent(accessToken);
		if(expire !=-1){
			stringRedisTemplate.boundValueOps(key).expire(expire, TimeUnit.SECONDS);
		}
		
	}

	public ResponseEntity<DelegateRegisterDataResp> delegateRegister(DelegateRegisterDataReq req, int tenantId ){
		clearGrowingAccessToken( tenantId); 
		ResponseEntity<DelegateRegisterDataResp> resp = growingRemoteService.delegateRegister(req, String.format(INTEGATION_OATH_FOMAT, client_id,client_secret));
		if(resp.getStatusCode() == HttpStatus.OK){
			setGrowingAccessToken(tenantId, resp.getBody().getAccessToken(),resp.getBody().getExpiresIn());
		}
		return resp;
	}
	
	public String  analyzeFeignException(FeignException e){
		int index = e.getMessage().indexOf("content:") + "content:".length();
		return e.getMessage().substring(index);
	}
	

	public ResponseEntity<InstallSdkResp> installSdk(InstallSdkReq req, String projectId,int tenantId) throws Exception{
		String baseurl = String.format("/widgets/projects/%s/install_sdk", projectId);
		Map<String, String> map = Maps.newTreeMap() ;
		
		String url = getDashBoardInfo(req,baseurl,tenantId,map);
		log.debug(String.format("remote:[%s]", url));
		
		return remoteGrowingInstallSdk(req,baseurl,tenantId,url,map,projectId);
	}
	
	private ResponseEntity<InstallSdkResp> remoteGrowingInstallSdk(InstallSdkReq req,String baseurl,int tenantId,String url ,Map<String, String> map ,String projectId) {
		ResponseEntity<InstallSdkResp> resp = null ;
		int loop = 0;
		boolean sucesssign = false ;
		String remoteurl = url ;
		while(loop <= repeated_num&&!sucesssign){
			try{
				resp = growingRemoteIframeRegeditService.installSdk(map, String.format(INTEGATION_OATH_FOMAT, client_id,client_secret), projectId);
				sucesssign = true;
			} catch (FeignException e) {
				if(e.status() == 401){
					if(loop == repeated_num -1){
						log.debug(e.getMessage(),e);
						throw e ;
					}else{
						getAccessToken(tenantId, true);
						remoteurl = getDashBoardInfo(req,baseurl,tenantId,map);
					}
				} else if (e.status() == 301) {
					InstallSdkResp data = new InstallSdkResp();
					data.setUrl(remoteurl);
					resp = new ResponseEntity<InstallSdkResp>(data,HttpStatus.OK );
					sucesssign = true;
				}else{
					log.debug(e.getMessage(),e);
					throw e;
				}
			}
			
			loop ++;
		}
		
		return resp ;
	}
	
	public ResponseEntity<DashboardResp> dashboard(DashboardReq req, int tenantId) {
		String baseurl = "/widgets/dashboard";
		Map<String, String> map = Maps.newTreeMap() ;
		String url = getDashBoardInfo(req,baseurl,tenantId,map);
		return remoteGrowingDashboard(url,baseurl,map,req, tenantId);
	}
	
	private String getDashBoardInfo(Object req, String baseurl,int tenantId,Map<String, String> map) {
		String url = "";
		try{
			map.clear();
			setFieldValue(req, "sign", null);
			String accessToken = getAccessToken(tenantId,false);
			String sign = getSign(HttpMethod.GET,baseurl,req,accessToken);
			setFieldValue(req, "sign", sign.toLowerCase());
			getValue(req,map);
		    url =geturl(baseurl,map);
		    log.debug(String.format("remote:[%s]", url));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		
		return url ; 
	}
	
	private ResponseEntity<DashboardResp> remoteGrowingDashboard(String url ,String baseurl,Map<String, String> map,DashboardReq req, int tenantId) {
		ResponseEntity<DashboardResp> resp = null ;
		int loop = 0;
		boolean sucesssign = false ;
		String remoteurl = url ;
		while(loop <= repeated_num && !sucesssign){
			try{
				resp = growingRemoteIframeRegeditService.dashboard(map, String.format(INTEGATION_OATH_FOMAT, client_id,client_secret));
				sucesssign = true;
			} catch (FeignException e) {
				if(e.status() == 401){
					if(loop == repeated_num -1){
						log.debug(e.getMessage(),e);
						throw e ;
					}else{
						getAccessToken(tenantId, true);
						remoteurl = getDashBoardInfo(req,baseurl,tenantId,map);
						
					}
				} else if (e.status() == 301) {
					DashboardResp data = new DashboardResp();
					data.setUrl(remoteurl);
					resp = new ResponseEntity<DashboardResp>(data,HttpStatus.OK );
					sucesssign = true;
				}else{
					log.debug(e.getMessage(),e);
					throw e;
				}
			}
			
			loop ++;
		}
		
		return resp ;
	}
	
	private String visitorTracksDashboard(VisitorTrackReq req,GrowingIoCompanyAction action,Map<String, String> map){
	  DashboardReq dashboardReq = new DashboardReq();
      dashboardReq.setTimestamp(new Date().getTime());
      dashboardReq.setUser_id(action.getUserId());//  if action==null???
      dashboardReq.setClient_id(client_id);
      
      String baseurl = String.format("/widgets/projects/%s/insights/segmentations/latest/users/%s", req.getProjectId(),req.getGrowingUserId());
      String url = getDashBoardInfo(dashboardReq,baseurl,action.getTenantId(),map);
      log.debug(String.format("remote:[%s]", url));
      return url;
	}
	
	private Header[] visitorTracks(VisitorTrackReq req,GrowingIoCompanyAction action) {
        Map<String, String> map = Maps.newTreeMap() ;
        int loop = 0;
        boolean sucesssign = false ;
        CloseableHttpClient client = HttpUtil.hc;
        String requestUrl = "/widgets/projects/%s/insights/segmentations/latest/users/%s";
        
        Header[] headers = null;
        
        while (loop <= repeated_num && !sucesssign) {
            visitorTracksDashboard(req,action,map);
            
            HttpGet get = new HttpGet(getRequestUrl(map,String.format(requestUrl, req.getProjectId(),req.getGrowingUserId())));
            get.setConfig(NOREDIRECTCONFIG);
        
            get.addHeader("Authorization", String.format(INTEGATION_OATH_FOMAT, client_id, client_secret));
            HttpResponse response;
            try {
                response = client.execute(get);
                headers = response.getHeaders("set-cookie");
                if (response.getStatusLine().getStatusCode() == 401) {
                    getAccessToken(req.getTenantId(), true);
                    loop++;
                } else {
                    sucesssign = true;
                }
            } catch (IOException e) {
                log.error(" error when call growingIo to get Cookies for getting visitortracks info , url : {},exception : {}",get.getURI(),e);
            } 
          }
          return headers;
    }
	
	private String getRequestUrl(Map<String, String> map, String requestUrl) {
  	    String params = "?";
  	        for (Map.Entry<String,String> entry : map.entrySet()) {
        	    params += entry.getKey() + "=" + entry.getValue() + "&";
        	}  	  
  	    params = params.substring(0, params.length()-1);
	    return visitorTrackHost + requestUrl + params;
    }

    public ResponseEntity<String> visitorEvents(VisitorTrackReq req,GrowingIoCompanyAction action){
        int loop = 0;
        boolean sucesssign = false ;
        ResponseEntity<String> resp = null;
        while(loop <= repeated_num && !sucesssign){
            try {
                EventReqCookie cookie = requestForEventReqCookie(req,action);
    
                resp = growingRemoteEventService.event(String.format("user_id=%s;user_session=%s", cookie.getUserId(),cookie.getUserSession()),
                    req.getProjectId(), base64Encode(req.getGrowingUserId()));
                sucesssign = true;
            } catch (UnsupportedEncodingException e) {
                log.error(" error to parse String to bytes by String's getBytes mehtod , string : {} , exception : {}",req.getGrowingUserId(),e);
            } catch (FeignException e) {
                if(e.status() == 401&&loop<repeated_num -1){
                    log.debug(e.getMessage(),e);
                    throw e ;
                }
            }
            loop++;
        }
        return resp ;
    }

	private EventReqCookie requestForEventReqCookie(VisitorTrackReq visitorTrackReq,
	      GrowingIoCompanyAction action) {
	      EventReqCookie cookie = new EventReqCookie();
	      Header[] headers = visitorTracks(visitorTrackReq, action);
	      for(Header header : headers){
	          HeaderElement[] elements = header.getElements();
	          for(HeaderElement element : elements){
	              if(StringUtils.equals(element.getName(),"user_id")){
	                  cookie.setUserId(element.getValue());
	              }
	              if(StringUtils.equals(element.getName(),"user_session")){
	                  cookie.setUserSession(element.getValue());
	              }
	          }
	      }    
	      return cookie;
	  }
	
  private String getSign(HttpMethod method,String base_uri , Object param,String accessToken) throws Exception {

		Map<String, String> map = Maps.newTreeMap() ;
		getValue(param,map);
		String methodcode = URLEncoder.encode(method.name().toUpperCase(),"UTF-8")  ;
		String baseurlcode=URLEncoder.encode(base_uri,"UTF-8")  ;
		String paramval ="";
		for (Map.Entry<String, String> entry : map.entrySet()) { 
			if(Strings.isNullOrEmpty(paramval)){
				paramval= entry.getKey() + "=" + entry.getValue();
			}else{
				paramval = paramval + "&" + entry.getKey() + "=" + entry.getValue();
			}
		}
		
		String paramvalcode = URLEncoder.encode(paramval,"UTF-8")  ;
		String signstr= String.format("%s&%s&%s", methodcode,baseurlcode,paramvalcode);
		
		return HMACSHA1.HmacSHA1Encrypt(signstr, accessToken)   ;
	}
	
	private String getAccessToken(int tenantId, boolean isInit) {
		
		String accessToken = "";
		if(isInit){
			accessToken = processAccessToken(tenantId);
		}else{
			String accesstokenkey = String.format(INTEGATION_TOPIC, tenantId);
			accessToken = stringRedisTemplate.boundValueOps(accesstokenkey).get();
			if(Strings.isNullOrEmpty(accessToken)){
				accessToken = processAccessToken(tenantId);
			}
		}		
		
		return accessToken;
	}
	
	private String getrefAccessTokenForTeanan(int tenantId){
		GrowingIoCompanyAction action = growingIoCompanyRepository.findByTenantId(tenantId);
		if(action!=null){
			return action.getRefreshToken();
		}else{
			return null;
		}
	}
	
	
	
    private String processAccessToken(int tenantId){
    	String accessToken = "";
    	clearGrowingAccessToken(tenantId);
    	String refAccessToken = getrefAccessTokenForTeanan(tenantId);
    	if(!Strings.isNullOrEmpty(refAccessToken)){
    		UpdateRegisterDataReq req = new UpdateRegisterDataReq();
    		req.setClientId(client_id);
    		req.setClientSecret(client_secret);
    		req.setGrantType("refresh_token");
    		req.setRefreshToken(refAccessToken);
    		req.setTimestamp(new Date().getTime());
    		ResponseEntity<UpdateRegisterDataResp> resp =growingRemoteService.updateRegister(req);
    		if(resp.getStatusCode().equals(HttpStatus.OK)){
    			accessToken = resp.getBody().getAccessToken();
    			growingIoCompanyRepository.updateGrowingRefreshTokenByTenanId(resp.getBody().getRefreshToken(), tenantId);
    			setGrowingAccessToken(tenantId, resp.getBody().getAccessToken(), resp.getBody().getExpiresIn());
    		}else{
    			log.debug("processAccessToken :[%d],[%s]",tenantId,resp.getStatusCode().toString());
    		}
    	}
    	
    	return accessToken ;
    }
    
    
    public Method getDeclaredMethod(Object object, String fieldName){
        Method field = null;
        Class<?> clazz = object.getClass();
		for(; clazz != Object.class; clazz = clazz.getSuperclass()) {
		    try {
		        for (Method method : clazz.getDeclaredMethods()) {
		                                if(method.getName().equals(fieldName)){
		                                        field = method;
		                                }
		                        }
		
		
		    } catch (Exception e) {
		    	log.debug(e.getMessage(),e);
		    }
		}

		return  field ;
    }

    public void setFieldValue(Object orig, String field, Object value){

        String setfunc = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
        Method setMethod = getDeclaredMethod(orig,setfunc);
        if(setMethod!=null){
                try {
                    setMethod.invoke(orig, value);
               } catch (Exception e) {
                	log.debug(e.getMessage(),e);
                } 
        }
    }

    public void getValue(Object source, Map<String, String> map) {

		if (source != null) {
			Field[] fields = source.getClass().getDeclaredFields();
			Method m = null;
			Object value = null;
			for (Field field : fields) {
				String name = field.getName();
				String getname = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
				try {
					m = field.getDeclaringClass().getMethod(getname);
					value = m.invoke(source);
					if (value != null) {
						if(value.getClass().equals(String.class)){
							map.put(name, (String) value);
						}else{
							map.put(name, String.valueOf(value));
						}
						
					}
				}
				catch (Exception e1) {
				    log.debug(e1.getMessage(),e1);
				}
			}
		}
	}

    public String geturl(String baseurl, Map<String, String> map) {
    	String param = null;
    	for(Map.Entry<String, String> entry : map.entrySet()){
    		if(Strings.isNullOrEmpty(param)){
    			param=String.format("%s=%s", entry.getKey(),entry.getValue());
    		}else{
    			param = param + "&" + String.format("%s=%s", entry.getKey(),entry.getValue());
    		}
    	}
    	
    	
    	return String.format("%s%s?%s", remoteUrl,baseurl,param) ;
    }
    
     
    private static class HMACSHA1 {  
    	  
        private static final String MAC_NAME = "HmacSHA1";    
        private static final String ENCODING = "UTF-8";  
        
        /**  
         * 使用 HMAC-SHA1 签名方法对对encryptText进行签名  
         * @param encryptText 被签名的字符串  
         * @param encryptKey  密钥  
         * @return  
         * @throws UnsupportedEncodingException 
         * @throws NoSuchAlgorithmException 
         * @throws InvalidKeyException 
         * @throws Exception  
         */    
        public static String HmacSHA1Encrypt(String encryptText, String encryptKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException      
        {           
            byte[] data=encryptKey.getBytes(ENCODING);  
            SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);   
            Mac mac = Mac.getInstance(MAC_NAME);  
            mac.init(secretKey);    
            byte[] text = encryptText.getBytes(ENCODING);    
            byte[] digest = mac.doFinal(text);  
            return new HexBinaryAdapter().marshal(digest); 
        }      
    }  
    
    private static String base64Encode(String growingUserId) throws UnsupportedEncodingException {
        byte[] bytes=growingUserId.getBytes("UTF-8");
        return Base64.getEncoder().encodeToString(bytes);
    }
    
}