package com.easemob.kefu.rtcmedia.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.easemob.kefu.rtcmedia.protocol.types.State;
import com.easemob.kefu.rtcmedia.util.Utils;

public interface RtcMediaRepository extends JpaRepository<RtcMediaEntity, Long> {
    RtcMediaEntity findByMsgId(String msgId);

    default RtcMediaEntity updateStateByMsgId(String msgId, State state) {
        RtcMediaEntity entity = findByMsgId(msgId);
        if (entity != null) {
            entity.setState(state);
            Utils.updateEntityLastUpdateTime(entity);
            return save(entity);
        } else {
            throw new ResourceNotFoundException("conference is not found via msgId(" + msgId + ")");
        }
    }
}
