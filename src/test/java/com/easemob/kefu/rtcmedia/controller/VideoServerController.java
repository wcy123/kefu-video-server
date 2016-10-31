package com.easemob.kefu.rtcmedia.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.easemob.kefu.rtcmedia.data.TestSamples;
import com.easemob.kefu.rtcmedia.protocol.AgentCreateConference;
import com.easemob.kefu.rtcmedia.protocol.AgentJid;
import com.easemob.kefu.rtcmedia.protocol.GetStatus;
import com.easemob.kefu.rtcmedia.protocol.UpdateStatus;

/**
 * 坐席前段呼叫 KEFU 的 API 接口
 *
 * Created by wangchunye on 10/26/16.
 */
@RestController
@RequestMapping("/v1/rtcmedia")
public class VideoServerController {
    /**
     * 坐席前段得到 JID 用于视频通信
     *
     * @return
     */
    @RequestMapping(path = "/{agentName}/jid", method = RequestMethod.GET)
    AgentJid.Response getJid() {
        return TestSamples.getJidResponse();
    }

    @RequestMapping(path = "/conferences", method = RequestMethod.POST)
    AgentCreateConference.Response createConference(
            @RequestBody AgentCreateConference.Request request) {
        return TestSamples.agentCreateConferenceResponse();
    }

    /**
     * MediaService 回调 Kefu server
     * 
     * @param sid 会话 ID
     * @param request 请求 ID
     * @return 响应
     */
    @RequestMapping(path = "/conference/{sid}", method = RequestMethod.POST)
    UpdateStatus.Response updateStatus(@PathVariable String sid,
            @RequestBody UpdateStatus.Request request) {
        return TestSamples.updateStatusResponse();
    }

    /**
     * 坐席调用 Kefu server 获取
     *
     * @param msgId 引起会话的消息 ID . 一个会议既可以用 sid 表示(客服和media service 之间的协议), 也可以用 msgId 表示(客服和坐席前段之间协议)
     * @return 响应
     */
    @RequestMapping(path = "/conference/{msgId}", method = RequestMethod.GET)
    GetStatus.Response getStatus(@PathVariable UUID msgId) {
        return TestSamples.getStatusResponse();
    }
}
