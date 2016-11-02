package com.easemob.kefu.rtcmedia.service.impl;

import java.net.URI;
import java.security.DigestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import com.easemob.kefu.rtcmedia.dep.MediaServiceApi;
import com.easemob.kefu.rtcmedia.model.RtcMediaEntity;
import com.easemob.kefu.rtcmedia.model.RtcMediaRepository;
import com.easemob.kefu.rtcmedia.protocol.CreateConference;
import com.easemob.kefu.rtcmedia.protocol.GetStatus;
import com.easemob.kefu.rtcmedia.protocol.UpdateStatus;
import com.easemob.kefu.rtcmedia.protocol.types.State;
import com.easemob.kefu.rtcmedia.service.ConferenceService;

import com.google.common.collect.ImmutableList;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConferenceServiceImpl implements ConferenceService {
    @Autowired
    RtcMediaRepository repository;
    @Autowired
    private MediaServiceApi mediaServiceApi;
    @Autowired
    private Parameters parameters;

    @Override
    public RtcMediaEntity agentCreateConference(RtcMediaEntity rtcMediaEntity) {
        CreateConference.Request request;
        final long createdTimestamp = System.currentTimeMillis();
        String msgId = rtcMediaEntity.getMsgId();
        try {
            request = CreateConference.Request.builder()
                    .mediaType(CreateConference.MediaType.VIDEO)
                    .callbackUrl(buildCallbackUrl(msgId))
                    .callbackArg(buildCallbackArg())
                    .callbackTimeoutMs(parameters.getCallbackTimeoutMs())
                    .users(buildUsers(rtcMediaEntity))
                    .timestamp(createdTimestamp)
                    .callExt("")
                    .createSign()
                    .build();
        } catch (DigestException e) {
            log.error("cannot create digest {}, entity = {} ", e, rtcMediaEntity);
            return null;
        }
        final CreateConference.Response response = mediaServiceApi.createConference(request);

        rtcMediaEntity.setSid(response.getSid());
        return repository.save(rtcMediaEntity);
    }

    private ImmutableList<String> buildUsers(RtcMediaEntity rtcMediaEntity) {
        return ImmutableList.of(buildAgentJid(rtcMediaEntity), buildVisitorJid(rtcMediaEntity));
    }

    private String buildAgentJid(RtcMediaEntity rtcMediaEntity) {
        return buildJid(rtcMediaEntity, rtcMediaEntity.getAgentUserName());
    }

    private String buildVisitorJid(RtcMediaEntity rtcMediaEntity) {
        return buildJid(rtcMediaEntity, rtcMediaEntity.getVisitorUserName());
    }

    private String buildJid(RtcMediaEntity rtcMediaEntity, String userName) {
        return rtcMediaEntity.getOrgName() + "#" + rtcMediaEntity.getAppName() + "_" + userName
                + "@easemob.com";
    }

    private String buildCallbackArg() {
        // language=JSON
        return "{\n" +
                "  \"type\":\"not used yet\"\n" +
                "}";
    }

    private URI buildCallbackUrl(String msgId) {
        return parameters.getRootUri().resolve("/v1/rtcmedia/conference/" + msgId);
    }

    @Override
    public UpdateStatus.Response updateStatus(String msgId, UpdateStatus.Request request) {
        final State state = request.getState();
        repository.updateStateByMsgId(msgId, state);
        return UpdateStatus.Response.builder()
                .callbackUrl(buildCallbackUrl(msgId))
                .callbackArg(buildCallbackArg())
                .callbackTimeoutMs(parameters.getCallbackTimeoutMs())
                .build();
    }

    @Override
    public GetStatus.Response obtainStatus(String msgId) {
        final RtcMediaEntity mediaEntity = repository.findByMsgId(msgId);
        if (mediaEntity == null) {
            throw new ResourceNotFoundException(
                    "cannot find media entity via msg id(" + msgId + ")");
        }
        return GetStatus.Response.builder()
                .state(mediaEntity.getState())
                .build();

    }

    @ConfigurationProperties("kf.rtc.media")
    @Component
    @Data
    public static class Parameters {
        private URI rootUri;
        private Long callbackTimeoutMs;
    }

}
