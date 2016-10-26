package com.easemob.kefu.video.controller;

import com.easemob.kefu.video.SampleData.TestSamples;
import com.easemob.kefu.video.protocol.CreateConference;
import com.easemob.kefu.video.protocol.UpdateStatus;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * 程序入口
 * Created by wangchunye on 10/26/16.
 */

@RestController
@RequestMapping("/v1/video")
public class Controller {
  @RequestMapping(path = "/conferences", method = RequestMethod.POST)
  CreateConference.Response createConference(@RequestBody CreateConference.Request request){
    return TestSamples.createConferenceResponse();
  }

  @RequestMapping(path = "/conference/{sid}", method = RequestMethod.POST)
  UpdateStatus.Response createConference(@PathVariable String sid, @RequestBody UpdateStatus.Request request){
    return TestSamples.updateStatusResponse();
  }
}
