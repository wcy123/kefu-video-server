package com.easemob.kefu.video.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.easemob.kefu.video.protocol.get.jid.Response;
import com.easemob.kefu.video.sample.data.TestSamples;

/**
 * 坐席前段呼叫 KEFU 的 API 接口
 *
 * Created by wangchunye on 10/26/16.
 */
@RestController
@RequestMapping("/v1/video")
public class VideoServerController {
    @RequestMapping(path = "/conference/{sid}", method = RequestMethod.POST)
    com.easemob.kefu.video.protocol.update.status.Response updateStatus(@PathVariable String sid,
            @RequestBody com.easemob.kefu.video.protocol.update.status.Request request) {
        return TestSamples.updateStatusResponse();
    }

    @RequestMapping(path = "/{agentName}/jid", method = RequestMethod.GET)
    Response getJid() {
        return TestSamples.getJidResponse();
    }
}
