package com.easemob.kefu.rtcmedia.protocol;

import java.net.URI;

import com.easemob.kefu.rtcmedia.protocol.types.State;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

/**
 * MediaService 发给 KEFUServer 的回调请求 Created by wangchunye on 10/27/16.
 */
public final class UpdateStatus {
    private UpdateStatus() {}

    @Value
    @Builder
    @JsonDeserialize(builder = Request.RequestBuilder.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {
        /**
         * 回调参数, media service 不用关心这个, 怎么送过去, 怎么还回来
         */
        private final String callbackArg;
        // ------------- 华丽的分割线 ----------
        State state;

        // ------------- 华丽的分割线 ----------
        @JsonPOJOBuilder(withPrefix = "")
        public static final class RequestBuilder {
        }
    }

    /**
     * Kefu server 发给 media server 的回调响应 Created by wangchunye on 10/27/16.
     */
    @Data
    @Builder
    @JsonDeserialize(builder = Response.ResponseBuilder.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
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
        State state;

        // ------------- 华丽的分割线 ----------
        @JsonPOJOBuilder(withPrefix = "")
        public static final class ResponseBuilder {
        }
    }
}
