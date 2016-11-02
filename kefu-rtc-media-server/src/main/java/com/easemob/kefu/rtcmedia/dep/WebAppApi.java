package com.easemob.kefu.rtcmedia.dep;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@FeignClient(name = "${kf.rtc.media.webappname}", fallback = WebAppApiFallback.class)
@FunctionalInterface
public interface WebAppApi {
    @RequestMapping(value = "/v1/webimplugin/visitors", method = RequestMethod.POST,
            headers = {"Accept = application/json", "Content-Type = application/json"})
    ResponseRegisterImUser registerImUserInternal(RequestRegisterImUser requestRegisterImUser);

    default ResponseRegisterImUser registerImUser(String orgName, String appName,
            String imServiceNumber) {
        return registerImUserInternal(new RequestRegisterImUser(orgName, appName, imServiceNumber));
    }

    @Data
    class RequestRegisterImUser {
        private final String orgName;
        private final String appName;
        private final String imServiceNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    class ResponseRegisterImUser {
        private String userId;
        private String userPassword;
    }
}
