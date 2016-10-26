package com.easemob.kefu.video.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;

/**
 * media service -> kefu server 的回调
 *
 * Created by wangchunye on 10/26/16.
 */
public interface UpdateStatus {
  public enum State {
    INIT, CREATED, RINGING, TERMINATED, ABORTED
  }
  @Data
  @Builder
  @JsonDeserialize(builder = UpdateStatus.Request.RequestBuilder.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  class Request {
    // ------------- 华丽的分割线 ----------
    private final State state;

    /**
     * 回调参数, media service 不用关心这个, 怎么送过去, 怎么还回来
     */
    private final String callbackArg;

    // ------------- 华丽的分割线 ----------
    @JsonPOJOBuilder(withPrefix = "")
    public static final class RequestBuilder {
    }
  }
  @Data
  @Builder
  @JsonDeserialize(builder = UpdateStatus.Response.ResponseBuilder.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  class Response {
    // ------------- 华丽的分割线 ----------
    private final String status;

    // ------------- 华丽的分割线 ----------
    @JsonPOJOBuilder(withPrefix = "")
    public static final class ResponseBuilder {
    }
  }
}
