package com.easemob.kefu.rtcmedia.service;

import com.easemob.kefu.rtcmedia.model.RtcMediaEntity;
import com.easemob.kefu.rtcmedia.protocol.GetStatus;
import com.easemob.kefu.rtcmedia.protocol.UpdateStatus;

public interface ConferenceService {
    RtcMediaEntity agentCreateConference(RtcMediaEntity rtcMediaEntity);

    UpdateStatus.Response updateStatus(String msgId, UpdateStatus.Request request);

    GetStatus.Response obtainStatus(String msgId);
}
