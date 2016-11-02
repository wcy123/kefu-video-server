package com.easemob.kefu.rtcmedia.controller;

import static com.easemob.kefu.rtcmedia.util.Utils.buildEntity;

import java.io.IOException;

import javax.persistence.NonUniqueResultException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.easemob.kefu.rtcmedia.model.RtcMediaEntity;
import com.easemob.kefu.rtcmedia.protocol.AgentCreateConference;
import com.easemob.kefu.rtcmedia.protocol.AgentJid;
import com.easemob.kefu.rtcmedia.protocol.GetStatus;
import com.easemob.kefu.rtcmedia.protocol.UpdateStatus;
import com.easemob.kefu.rtcmedia.service.ConferenceService;
import com.easemob.kefu.rtcmedia.service.ImUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 坐席前段呼叫 KEFU 的 API 接口
 *
 * Created by wangchunye on 10/26/16.
 */
@Slf4j
@RestController
@RequestMapping("/v1/rtcmedia")
public class RtcServerController {
    @Autowired
    BeanFactory beanFactory;
    @Autowired
    private ConferenceService conferenceService;
    @Autowired
    private ImUserService imUserService;

    /**
     * 坐席前段得到 JID 用于视频通信
     */
    @RequestMapping(path = "/{agentName}/jid", method = RequestMethod.GET)
    public AgentJid.Response getJid(@PathVariable String agentName,
            @RequestParam String orgName,
            @RequestParam String appName,
            @RequestParam String imServiceNumber) {
        log.debug("obtainStatus: {} getJid agent = {}#{}_{}", agentName, orgName,
                appName, imServiceNumber);
        final AgentJid.Response response =
                imUserService.getJidResponse(agentName, orgName, appName, imServiceNumber);
        log.debug("obtainStatus: response = {}", response);
        return response;
    }

    /**
     * 坐席前段发起视频呼叫请求
     */
    @RequestMapping(path = "/conferences", method = RequestMethod.POST)
    public AgentCreateConference.Response createConference(
            @RequestBody AgentCreateConference.Request request) {
        log.debug("createConference: request = {}", request);
        final RtcMediaEntity entity = conferenceService.agentCreateConference(buildEntity(request));
        final AgentCreateConference.Response response = AgentCreateConference.Response.builder()
                .sid(entity.getSid())
                .build();
        log.debug("createConference: response = {}", response);
        return response;
    }

    /**
     * 坐席调用 Kefu server 获取
     *
     * @param msgId 引起会话的消息 ID.
     * @return 响应
     */
    @RequestMapping(path = "/conference/{msgId}", method = RequestMethod.GET)
    public GetStatus.Response obtainStatus(@PathVariable String msgId) {
        log.debug("updateStatus: obtainStatus msgId = {}", msgId);
        final GetStatus.Response response = conferenceService.obtainStatus(msgId);
        log.debug("updateStatus: response = {}", response);
        return response;
    }

    /**
     * MediaService 回调 Kefu server
     *
     * @param msgId 会话 ID
     * @param request 请求 ID
     * @return 响应
     */
    @RequestMapping(path = "/conference/{msgId}", method = RequestMethod.POST)
    public UpdateStatus.Response updateStatus(@PathVariable String msgId,
            @RequestBody UpdateStatus.Request request) {
        log.debug("updateStatus: request msgId = {}, request = {}", msgId, request);
        final UpdateStatus.Response response = conferenceService.updateStatus(msgId, request);
        log.debug("updateStatus: response = {}", response);
        return response;
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "msgId not unique")
    @ExceptionHandler({IncorrectResultSizeDataAccessException.class,
            NonUniqueResultException.class})
    public void handleError(HttpServletRequest request, Exception ex) throws IOException {
        log.error("msgId is not unique, request = {}, exception = {}", request.getRequestURI(), ex);
    }
}
