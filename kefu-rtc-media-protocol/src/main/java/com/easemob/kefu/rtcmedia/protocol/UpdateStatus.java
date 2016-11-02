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
         *
         * 此参数：要求MediaService需要在此时间内，对客服server发送状态更新。
         * 由于实现方式未定，暂时 先确定为：MediaService方 超时间 + 客户端ping间隔 + 2 * 超时检查时间
         *
         * 目前：客户端 在收到MediaService后，会定时对 MediaServie发送ping消息。MediaService会在收到后，更新时间戳。
         * 若：长时间未收到， MediaServie会进行超时检查。
         * MediaService收到ping后会主动向客服Server发送 状态更新回调
         *
         */
        long callbackTimeoutMs;

        // ------------- 华丽的分割线 ----------
        @JsonPOJOBuilder(withPrefix = "")
        public static final class ResponseBuilder {
        }
    }
}
