package com.easemob.kefu.rtcmedia.sample.data;

import java.net.URI;
import java.security.DigestException;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.core.UriBuilder;

import com.easemob.kefu.rtcmedia.protocol.AgentCreateConference;
import com.easemob.kefu.rtcmedia.protocol.AgentJid;
import com.easemob.kefu.rtcmedia.protocol.CreateConference;
import com.easemob.kefu.rtcmedia.protocol.UpdateStatus;
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

    public static CreateConference.Request createConferenceRequest() throws DigestException {
        return CreateConference.Request.builder()
                .mediaType(CreateConference.MediaType.VIDEO)
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

    public static CreateConference.Response createConferenceResponse() {
        return CreateConference.Response.builder()
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

    public static UpdateStatus.Request updateStatusRequest() {
        return UpdateStatus.Request.builder()
                .state(UpdateStatus.State.INIT)
                .callbackArg(callbackArg()).build();
    }

    public static UpdateStatus.Response updateStatusResponse() {
        return UpdateStatus.Response.builder()
                .callbackUrl(callbackUrl())
                .callbackArg(callbackArg())
                .callbackTimeoutMs(CALLBACK_TIMEOUT_MS)
                .build();
    }

    public static String updateStatusResponseJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(updateStatusRequest());
    }

    public static AgentJid.Response getJidResponse() {
        return AgentJid.Response.builder()
                .orgName(ORG_NAME)
                .appName(APP_NAME)
                .userName(USER_NAME)
                .build();
    }

    public static String getStates() {
        return String.join(",", Arrays.asList(UpdateStatus.State.values()).stream()
                .map(UpdateStatus.State::toString)
                .collect(Collectors.toList()));
    }

    public static AgentCreateConference.Request agentCreateConferenceRequest() {
        return AgentCreateConference.Request.builder()
                .visitorId(visitorId())
                .mediaType(CreateConference.MediaType.VIDEO)
                .msgId(msgId())
                .build();
    }

    private static UUID msgId() {
        return UUID.fromString("cafebabe-bb24-4762-987e-bb0bd71d1234");
    }

    private static UUID visitorId() {
        return UUID.fromString("cafebabe-bb24-4762-987e-bb0bd71da8fc");
    }

    public static AgentCreateConference.Response agentCreateConferenceResponse() {
        return AgentCreateConference.Response.builder()
                .sid(SID)
                .build();
    }

    public static String agentCreateConferenceRequestJson() {
        try {
            return new ObjectMapper().writeValueAsString(agentCreateConferenceRequest());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
