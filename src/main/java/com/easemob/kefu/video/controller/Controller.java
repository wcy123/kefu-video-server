package com.easemob.kefu.video.controller;

import com.easemob.kefu.video.sample.data.TestSamples;
import com.easemob.kefu.video.protocol.create.conference.Request;
import com.easemob.kefu.video.protocol.create.conference.Response;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 程序入口
 * Created by wangchunye on 10/26/16.
 */

@RestController
public class Controller {
  @RequestMapping(path = "/v1/webrtc/kefu/call", method = RequestMethod.POST)
  Response createConference(@RequestBody Request request){
    return TestSamples.createConferenceResponse();
  }

  @RequestMapping(path = "/callback/kefu/call/{sid}", method = RequestMethod.POST)
  com.easemob.kefu.video.protocol.update.status.Response createConference(@PathVariable String sid, @RequestBody com.easemob.kefu.video.protocol.update.status.Request request){
    return TestSamples.updateStatusResponse();
  }
}
