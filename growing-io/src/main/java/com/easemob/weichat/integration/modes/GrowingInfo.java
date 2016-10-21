package com.easemob.weichat.integration.modes;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class GrowingInfo {
  
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date timestamp;
  
  private Map<String, String> attrs;
  
  private String type;
  
}
