package com.easemob.kefu.rtcmedia.protocol;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

/**
 * 坐席前端请求建立视频会议
 *
 * Created by wangchunye on 10/27/16.
 */
public final class AgentCreateConference {
    private AgentCreateConference() {

    }

    @Value
    @Builder
    @JsonDeserialize(builder = Request.RequestBuilder.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {
        /**
         * 坐席 ID
         */
        String agentId;
        /**
         * 访客 ID
         */
        String visitorId;
        /**
         * 呼叫类型 AUDIO or VIDEO 现在都是 Video
         */
        CreateConference.MediaType mediaType;

        /**
         * msg id 是那一条消息触发的这次呼叫vi
         */
        String msgId;
        /**
         * JID 中的组织名
         */
        String orgName;
        /**
         * JID 中的应用名
         */
        String appName;
        /**
         * JID 中的访客用户名
         */
        String visitorUserName;

        /**
         * JID 中的坐席名称, 如果坐席本地没有缓存 cookie ,那么利用 getJID 接口获取
         */
        String agentUserName;

        @JsonPOJOBuilder(withPrefix = "")
        public static final class RequestBuilder {
        }
    }

    @Value
    @Builder
    @JsonDeserialize(builder = Response.ResponseBuilder.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        /**
         * 返回的会议唯一标识, 客户端也许用不上这个信息. 还是用 message id 唯一标识一个会话
         */
        String sid;

        @JsonPOJOBuilder(withPrefix = "")
        public static final class ResponseBuilder {
        }
    }
}
