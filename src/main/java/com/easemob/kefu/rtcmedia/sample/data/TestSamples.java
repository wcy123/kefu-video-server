package com.easemob.kefu.rtcmedia.sample.data;

import java.net.URI;
import java.security.DigestException;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.ws.rs.core.UriBuilder;

import com.easemob.kefu.rtcmedia.protocol.create.conference.MediaType;
import com.easemob.kefu.rtcmedia.protocol.create.conference.Request;
import com.easemob.kefu.rtcmedia.protocol.create.conference.Response;
import com.easemob.kefu.rtcmedia.protocol.update.status.State;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.common.collect.ImmutableList;

public class TestSamples {

    public static final String USER1 = "easemob-demo#chatdemouiwlz1@easemob.com/mobile";
    public static final String USER2 = "easemob-demo#chatdemouiwlz2@easemob.com/mobile";
    public static final int TIMESTAMP1 = 1477452999;
    public static final String SID = "1234567890";
    public static final String ORG_NAME = "easemob123";
    public static final String APP_NAME = "app1";
    public static final String USER_NAME = "a1";
    public static final int CALLBACK_TIMEOUT_MS = 60000;

    private TestSamples() {}

    public static Request createConferenceRequest() throws DigestException {
        return Request.builder()
                .mediaType(MediaType.VIDEO)
                .callbackUrl(callbackUrl())
                .callbackArg(callbackArg())
                .callbackTimeoutMs(CALLBACK_TIMEOUT_MS)
                .users(users())
                .timestamp((long) TIMESTAMP1)
                .callExt("anystring")
                .createSign()
                .build();
    }

    public static URI callbackUrl() {
        return UriBuilder.fromPath("/callback/kefu/call/").scheme("http")
                .host("kefu.sandbox.easemob.com")
                .build();
    }

    public static String callbackArg() {
        return "{\n" +
                "  \"anyObject\":[1,2,3,4]\n" +
                "}";
    }

    public static ImmutableList users() {
        return ImmutableList.builder().add(USER1).add(USER2).build();
    }

    public static Response createConferenceResponse() {
        return Response.builder()
                .sid(SID)
                .build();
    }

    public static String createConferenceRequestJson() {
        try {
            return new ObjectMapper().writeValueAsString(createConferenceRequest());
        } catch (DigestException | JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String createConferenceResponseJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(createConferenceResponse());
    }

    public static com.easemob.kefu.rtcmedia.protocol.update.status.Request updateStatusRequest() {
        return com.easemob.kefu.rtcmedia.protocol.update.status.Request.builder()
                .state(State.INIT)
                .callbackArg(callbackArg()).build();
    }

    public static com.easemob.kefu.rtcmedia.protocol.update.status.Response updateStatusResponse() {
        return com.easemob.kefu.rtcmedia.protocol.update.status.Response.builder()
                .callbackUrl(callbackUrl())
                .callbackArg(callbackArg())
                .callbackTimeoutMs(CALLBACK_TIMEOUT_MS)
                .build();
    }

    public static String updateStatusResponseJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(updateStatusRequest());
    }

    public static com.easemob.kefu.rtcmedia.protocol.get.jid.Response getJidResponse() {
        return com.easemob.kefu.rtcmedia.protocol.get.jid.Response.builder()
                .orgName(ORG_NAME)
                .appName(APP_NAME)
                .userName(USER_NAME)
                .build();
    }

    public static String getStates() {
        return String.join(",", Arrays.asList(State.values()).stream()
                .map(State::toString)
                .collect(Collectors.toList()));
    }
}
