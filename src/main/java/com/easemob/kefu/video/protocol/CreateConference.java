package com.easemob.kefu.video.protocol;

import java.net.URI;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.datatype.guava.GuavaDeserializers;

import com.google.common.collect.ImmutableList;

import lombok.Builder;
import lombok.Data;

/**
 * kefu -> media service
 *
 * 创建一个新的会议
 *
 * Created by wangchunye on 10/25/16.
 */

public interface CreateConference {
  public static final String KEY = "DDAY_101_airborne_division_e_company:We_stand_alone_together";

  public static enum MediaType {
    VIDEO, AUDIO
  }

  @Data
  @Builder
  @JsonDeserialize(builder = Request.RequestBuilder.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  class Request {
    // ------------- 华丽的分割线 ----------

    /**
     * 服务类型，如：音频、视频
     */
    private final MediaType mediaType;

    /**
     * 回调地址
     */
    private final URI callbackUrl;
    /**
     * 回调参数, media service 不用关心这个, 怎么送过去, 怎么还回来
     */
    private final String callbackArg;

    /**
     * 邀请下面这些用户加入会议, 内容是 JID<br/>
     * 约定：<br/>
     *  第一个：坐席<br/>
     *  第二个：访客<br/>
     * <code>
     *   users:[
     *   'easemob-demo#chatdemouiwlz1@easemob.com/mobile',   // 第一个是坐席
     *   'easemob-demo#chatdemouiwlz2@easemob.com/mobile',   // 第二个是访客
     *   ]
     * </code>
     */
    @JsonDeserialize(builder = GuavaDeserializers.class)
    private final ImmutableList<String> users;
    /**
     * 时间戳, 可以用于请求去重
     */
    private final long timestamp;
    /**
     * 扩展用, 客服server传递过来的此字段，MediaService 不做任何修改，将直接发送到 SDK；可用作 客服Server跟SDK间的透传
     */
    private final String callExt;


    /**
     * HmacSHA1(argsString(), kefuKey) <br/>
     * kefuKey: MediaService 分配给 客服的key
     *
     * DDAY_101_airborne_division_e_company:We_stand_alone_together
     *
     */
    private String sign;

    // ------------- 华丽的分割线 ----------

    /**
     * 请求参数的摘要，可用其 生成 签名
     *
     * @return 请求参数的摘要
     */
    public final String digest(){
      return String.join("$", callbackUrl.toString(), callbackArg, Arrays.toString(users), String.valueOf(timestamp), callExt);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class RequestBuilder {
    }
  }
  @Data
  @Builder
  @JsonDeserialize(builder = Response.ResponseBuilder.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  class Response {
    // ------------- 华丽的分割线 ----------
    /**
     * call session id
     */
    private final String sid;

    /**
     * 回调参数, media service 不用关心这个, 怎么送过去, 怎么还回来
     */
    private final String callbackArg;

    // ------------- 华丽的分割线 ----------
    @JsonPOJOBuilder(withPrefix = "")
    public static final class ResponseBuilder {
    }
  }

}
