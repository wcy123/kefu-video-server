package com.easemob.kefu.video.SampleData;

import com.google.common.collect.ImmutableList;

import java.net.URI;
import java.security.DigestException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import com.easemob.kefu.video.protocol.CreateConference;
import com.easemob.kefu.video.protocol.UpdateStatus;
import com.easemob.kefu.video.util.Encryptor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestSamples {
  public static CreateConference.Request createConferenceRequest() throws DigestException {
    CreateConference.Request request = CreateConference.Request.builder()
            .mediaType(CreateConference.MediaType.VIDEO)
            .callbackUrl(callbackUrl())
            .callbackArg(callbackArg())
            .users(users())
            .timestamp(timestampForCreateConferenceRequest())
            .callExt(callbackExt())
            .createSign()
            .build();
    return request;
  }

  public static URI callbackUrl() {
    return UriBuilder.fromPath("/callback/kefu/call/").scheme("http").host("kefu.sandbox.easemob.com")
            .build();
  }

  public static String callbackArg() {
    return "{\n" + "  \"anyObject\":[1,2,3]\n" + "}";
  }

  public static ImmutableList users() {
    return ImmutableList.builder().add(user1()).add(user2()).build();
  }

  public static String user1() {
    return "easemob-demo#chatdemouiwlz1@easemob.com/mobile";
  }

  public static String user2() {
    return "easemob-demo#chatdemouiwlz2@easemob.com/mobile";
  }

  public static long timestampForCreateConferenceRequest() {
    return 1477452999;
  }

  public static String callbackExt() {
    return "anystring";
  }

  public static CreateConference.Response createConferenceResponse() {
    return CreateConference.Response.builder().sid(sid())
            .callbackArg(callbackArg()).build();
  }

  public static String sid() {
    return "1234567890";
  }

  public static String createConferenceRequestJson() throws JsonProcessingException, DigestException {
    return new ObjectMapper().writeValueAsString(createConferenceRequest());
  }

  public static String createConferenceResponseJson() throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(createConferenceResponse());
  }

  public static UpdateStatus.Request updateStatusRequest() {
    return UpdateStatus.Request.builder().state(UpdateStatus.State.INIT).callbackArg(callbackArg()).build();
  }
  public static UpdateStatus.Response updateStatusResponse() {
    return UpdateStatus.Response.builder().status("OK").build();
  }

  public static String updateStatusResponseJson() throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(updateStatusRequest());
  }
}
