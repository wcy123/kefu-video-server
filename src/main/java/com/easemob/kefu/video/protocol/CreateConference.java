package com.easemob.kefu.video.protocol;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;

/**
 * kefu -> media server
 *
 * 创建一个新的会议
 *
 * Created by wangchunye on 10/25/16.
 */

public interface CreateConference {
  @Data
  @Builder
  @JsonDeserialize(builder = Request.RequestBuilder.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  class Request {
    // ------------- 华丽的分割线 ----------
    /**
     * 回调地址
     */
    private URI callbackUrl;
    /**
     * 回调参数, media server 不用关心这个, 怎么送过去, 怎么还回来
     */
    private String callbackArg;

    /**
     * 邀请下面这些用户加入会议, 内容是 JID <code>
     *   users:[
     *   'easemob-demo#chatdemouiwlz1@easemob.com/mobile',   // 第一个是坐席
     *   'easemob-demo#chatdemouiwlz2@easemob.com/mobile',   // 第二个是访客
     *   ] </code>
     */
    private String[] users;
    /**
     * 时间戳, 可以用于请求去重
     */
    private long timestamp;
    /**
     * 扩展用
     */
    private String callExt;

    // ------------- 华丽的分割线 ----------
    @JsonPOJOBuilder(withPrefix = "")
    public static final class RequestBuilder {
    }
  }
  @Data
  @Builder
  @JsonDeserialize(builder = Response.ResponseBuilder.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  class Response {
    // ------------- 华丽的分割线 ----------
    /**
     * video session id
     */
    private final String sid;

    // ------------- 华丽的分割线 ----------
    @JsonPOJOBuilder(withPrefix = "")
    public static final class ResponseBuilder {
    }
  }

}


