package com.easemob.kefu.rtcmedia.protocol.create.conference;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

/**
 * media server 发送给 kefu 的创建视频的响应 Created by wangchunye on 10/27/16.
 */
@Value
@Builder
@JsonDeserialize(builder = Response.ResponseBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    // ------------- 华丽的分割线 ----------
    /**
     * call session id
     */
    private final String sid;
    
    // ------------- 华丽的分割线 ----------
    @JsonPOJOBuilder(withPrefix = "")
    public static final class ResponseBuilder {
    }
}
