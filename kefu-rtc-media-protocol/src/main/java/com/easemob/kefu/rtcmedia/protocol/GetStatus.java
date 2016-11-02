package com.easemob.kefu.rtcmedia.protocol;

import com.easemob.kefu.rtcmedia.protocol.types.State;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;

/**
 * 坐席调用 Kefu server 获取会议状态
 *
 * Created by wangchunye on 10/28/16.
 */
public final class GetStatus {
    private GetStatus() {}

    @Data
    @Builder
    @JsonDeserialize(builder = Response.ResponseBuilder.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        // ------------- 华丽的分割线 ----------
        State state;

        // ------------- 华丽的分割线 ----------
        @JsonPOJOBuilder(withPrefix = "")
        public static final class ResponseBuilder {
        }
    }
}
