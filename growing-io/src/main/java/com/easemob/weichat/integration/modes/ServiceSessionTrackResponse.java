package com.easemob.weichat.integration.modes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity.ServicesessionTrack;
import com.easemob.weichat.models.util.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ServiceSessionTrackResponse {

  private int tenantId;
  
  private String servicesessionId;
  
  private String growingioId;
  
  private String visitorId;
  
  private List<GrowingInfo> visitorTracks;
  
  public static ServiceSessionTrackResponse fromServiceSessionTrack(ServicesessionTrack sessionTrack){
      ServiceSessionTrackResponse response = new ServiceSessionTrackResponse();
      response.setTenantId(sessionTrack.getTenantId());
      response.setServicesessionId(sessionTrack.getServicesessionId());
      response.setGrowingioId(sessionTrack.getGrowingioId());
      response.setVisitorId(sessionTrack.getVisitorId());
      if(StringUtils.isNotBlank(sessionTrack.getContext())){
          try {
              GrowingInfo[] array = JSONUtil.getObjectMapper().readValue(sessionTrack.getContext(), GrowingInfo[].class);
              response.setVisitorTracks(Arrays.asList(array));
          } catch (IOException e) {
              log.error(" error transfer servicesessiontrack.context to servicesessiontrackresponse.visitortracks,exception {} ",e);
          }
      }
      return response;
  }
  
}
