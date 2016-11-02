package com.easemob.kefu.rtcmedia.util;

import java.util.Date;

import com.easemob.kefu.rtcmedia.model.RtcMediaEntity;
import com.easemob.kefu.rtcmedia.protocol.AgentCreateConference;
import com.easemob.kefu.rtcmedia.protocol.CreateConference;
import com.easemob.kefu.rtcmedia.protocol.types.State;

public final class Utils {
    // 这里面的函数都是static 的, 所以没有必要创建对象
    private Utils() {}

    public static RtcMediaEntity buildEntity(AgentCreateConference.Request request) {
        long createdTimestamp = System.currentTimeMillis();

        return RtcMediaEntity.builder()
                .msgId(request.getMsgId())
                .agentId(request.getAgentId())
                .visitorId(request.getVisitorId())
                .orgName(request.getOrgName())
                .appName(request.getAppName())
                .mediaType(CreateConference.MediaType.VIDEO)
                .visitorUserName(request.getVisitorUserName())
                .agentUserName(request.getAgentUserName())
                .state(State.INIT)
                .created(new Date(createdTimestamp))
                .lastModified(new Date(createdTimestamp))
                .build();
    }

    public static void updateEntityLastUpdateTime(RtcMediaEntity entity) {
        if (entity.getLastModified() == null) {
            entity.setLastModified(new Date(System.currentTimeMillis()));
        } else {
            entity.getLastModified().setTime(System.currentTimeMillis());
        }
    }
}
