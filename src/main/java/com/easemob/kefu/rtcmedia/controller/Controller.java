package com.easemob.kefu.rtcmedia.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.easemob.kefu.rtcmedia.protocol.CreateConference;
import com.easemob.kefu.rtcmedia.sample.data.TestSamples;

/**
 * 程序入口
 * Created by wangchunye on 10/26/16.
 */

@RestController
public class Controller {
  @RequestMapping(path = "/v1/webrtc/kefu/call", method = RequestMethod.POST)
    CreateConference.Response createConference(@RequestBody CreateConference.Request request) {
    return TestSamples.createConferenceResponse();
  }


}
