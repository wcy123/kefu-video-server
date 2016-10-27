package com.easemob.kefu.rtcmedia.protocol.get.jid;

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
@Value
@Builder
@JsonDeserialize(builder = Response.ResponseBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    String orgName;
    String appName;
    String userName;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class ResponseBuilder {
    }
}
