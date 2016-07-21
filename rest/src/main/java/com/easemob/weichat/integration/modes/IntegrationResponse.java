package com.easemob.weichat.integration.modes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value=Include.NON_NULL)
public class IntegrationResponse {
	 public static final String STATUS_OK = "OK";
     public static final String STATUS_FAIL = "FAIL";
     
     /**
      * 标明请求是否成功
      */
     private String status;
    
     /**
      * TODO 错误编码,需要讨论
      */
     private String errorCode;
     /**
      * 错误信息,用作前端提示
      */
     private String errorDescription;
     /**
      * 存放多个返回结果,比如查询坐席列表的结果
      */
     private List<?> entities;
     /**
      * 存放单个返回结果,比如查询某个坐席的结果
      */
     private Object entity;

     /**
      * 分页查询时使用,是否第一页
      */
     private Boolean first;
     /**
      * 分页查询时使用,是否最后一页
      */
     private Boolean last;
     /**
      * 一页的记录个数
      */
     private Integer size;
     /**
      * 当前页,从0开始
      */
     private Integer number;
     /**
      * 当前页的记录个数
      */
     private Integer numberOfElements;
     /**
      * 总共多少页
      */
     private Integer totalPages;
     /**
      * 总共多少记录
      */
     private Long totalElements;

     
}
