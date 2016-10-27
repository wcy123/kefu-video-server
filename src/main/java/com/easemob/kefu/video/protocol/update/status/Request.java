package com.easemob.kefu.video.protocol.update.status;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

/**
 * MediaService 发给  KEFUServer 的回调请求
 * Created by wangchunye on 10/27/16.
 */
@Value
@Builder
@JsonDeserialize(builder = Request.RequestBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request {
    // ------------- 华丽的分割线 ----------
    private final State state;

    /**
     * 回调参数, media service 不用关心这个, 怎么送过去, 怎么还回来
     */
    private final String callbackArg;

    // ------------- 华丽的分割线 ----------
    @JsonPOJOBuilder(withPrefix = "")
    public static final class RequestBuilder {
    }
}
