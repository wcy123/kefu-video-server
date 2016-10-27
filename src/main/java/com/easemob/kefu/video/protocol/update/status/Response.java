package com.easemob.kefu.video.protocol.update.status;

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
    private final String status;

    // ------------- 华丽的分割线 ----------
    @JsonPOJOBuilder(withPrefix = "")
    public static final class ResponseBuilder {
    }
}
