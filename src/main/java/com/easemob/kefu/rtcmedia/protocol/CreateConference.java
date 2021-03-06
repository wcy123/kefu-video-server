package com.easemob.kefu.rtcmedia.protocol;

import java.net.URI;
import java.security.DigestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easemob.kefu.rtcmedia.util.Encryptor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.datatype.guava.GuavaDeserializers;

import com.google.common.collect.ImmutableList;

import lombok.Builder;
import lombok.Value;

public final class CreateConference {
    public static final String KEY = "DDAY_101_airborne_division_e_company:We_stand_alone_together";

    private CreateConference() {}

    public enum MediaType {
        VIDEO, AUDIO
    }

    public static class Constant {

        private Constant() {}
    }

    /**
     * Created by wangchunye on 10/27/16.
     */
    @Value
    @Builder(buildMethodName = "hiddenBuild")
    @JsonDeserialize(builder = Request.RequestBuilder.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {
        /**
         * 扩展用, 客服server传递过来的此字段，MediaService 不做任何修改，将直接发送到 SDK；可用作 客服Server跟SDK间的透传
         */
        private final String callExt;
        // ------------- 华丽的分割线 ----------
        /**
         * 服务类型，如：音频、视频
         */
        MediaType mediaType;
        /**
         * 回调地址
         */
        URI callbackUrl;
        /**
         * 回调参数, media service 不用关心这个, 怎么送过去, 怎么还回来
         */
        String callbackArg;
        /**
         * 回调时间, 在 callbackTimeoutMs 毫秒只能, 必须再次回调, 否则 kefu server 认为连接异常
         * 同UpdateStatus.Response.callbackTimeoutMs
         */
        long callbackTimeoutMs;
        /**
         * 邀请下面这些用户加入会议, 内容是 JID<br/>
         * 约定：<br/>
         * 第一个：坐席<br/>
         * 第二个：访客<br/>
         * <code> users:[
         * 'easemob-demo#chatdemouiwlz1@easemob.com/mobile',   // 第一个是坐席 'easemob-demo#chatdemouiwlz2@easemob.com/mobile',
         * // 第二个是访客 ] </code>
         */
        @JsonDeserialize(builder = GuavaDeserializers.class)
        ImmutableList<String> users;
        /**
         * 时间戳, 可以用于请求去重
         */
        long timestamp;

        public String getSign() {
            final String digest =
                    String.join("$", callbackUrl.toString(), callbackArg, users.toString(),
                            String.valueOf(timestamp), callExt);
            return Encryptor.hmacSha1(digest, KEY);
        }
        // ------------- 华丽的分割线 ----------

        @JsonPOJOBuilder(withPrefix = "")
        public static final class RequestBuilder {
            private final Logger log = LoggerFactory.getLogger(RequestBuilder.class);

            private String sign;

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public RequestBuilder createSign() {
                sign = Encryptor.hmacSha1(digest(), KEY);
                return this;
            }

            /**
             * 请求参数的摘要，可用其 生成 签名
             *
             * @return 请求参数的摘要
             */
            private String digest() {
                return String.join("$", callbackUrl.toString(), callbackArg, users.toString(),
                        String.valueOf(timestamp), callExt);
            }

            private void verify() throws DigestException {
                final String s = Encryptor.hmacSha1(digest(), KEY);
                if (sign == null) {
                    throw new DigestException("please provide a sign");
                }
                if (!sign.equals(s)) {
                    log.warn("sign error: {} is expected but {} is given", s, sign);
                    throw new DigestException("please sign " + s + " is not correct");
                }
            }

            public Request build() throws DigestException {
                verify();
                return hiddenBuild();
            }
        }
    }

    /**
     * media server 发送给 kefu 的创建视频的响应 Created by wangchunye on 10/27/16.
     */
    @Value
    @Builder
    @JsonDeserialize(builder = Response.ResponseBuilder.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
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
}
