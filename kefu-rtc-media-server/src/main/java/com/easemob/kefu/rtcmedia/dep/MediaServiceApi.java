package com.easemob.kefu.rtcmedia.dep;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.easemob.kefu.rtcmedia.protocol.CreateConference;

@FeignClient(name = "${kf.rtc.media.service.name}", url = "${kf.rtc.media.service.url}")
@FunctionalInterface
public interface MediaServiceApi {
    @RequestMapping(path = "/v1/webrtc/kefu/call", method = RequestMethod.POST)
    CreateConference.Response createConference(@RequestBody CreateConference.Request request);
}
