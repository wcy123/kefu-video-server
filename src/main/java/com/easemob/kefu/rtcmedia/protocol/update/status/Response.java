package com.easemob.kefu.rtcmedia.protocol.update.status;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;

/**
 * Kefu server 发给 media server 的回调响应 Created by wangchunye on 10/27/16.
 */
@Data
@Builder
@JsonDeserialize(builder = Response.ResponseBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    // ------------- 华丽的分割线 ----------
    /**
     * 下次回调地址
     */
    URI callbackUrl;
    /**
     * 回调参数, media service 不用关心这个, 怎么送过去, 怎么还回来
     */
    String callbackArg;

    /**
     * 回调时间, 在 timeoutMs 毫秒只能, 必须再次回调, 否则 kefu server 认为连接异常
     */
    long callbackTimeoutMs;
    State State;

    // ------------- 华丽的分割线 ----------
    @JsonPOJOBuilder(withPrefix = "")
    public static final class ResponseBuilder {
    }
}
