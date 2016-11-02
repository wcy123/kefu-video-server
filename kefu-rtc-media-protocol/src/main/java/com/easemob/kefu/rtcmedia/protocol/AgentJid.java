package com.easemob.kefu.rtcmedia.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

/**
 * 坐席前段呼叫 KEFU 的 API 接口 用于得到坐席的 JID 用户登录 IM 系统, 视频通话
 *
 * Created by wangchunye on 10/27/16.
 */

public final class AgentJid {
    private AgentJid() {}

    @Value
    @Builder
    @JsonDeserialize(builder = Response.ResponseBuilder.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        String orgName;
        String appName;
        String userName;
        String password;
        @JsonPOJOBuilder(withPrefix = "")
        public static final class ResponseBuilder {
        }
    }
}
