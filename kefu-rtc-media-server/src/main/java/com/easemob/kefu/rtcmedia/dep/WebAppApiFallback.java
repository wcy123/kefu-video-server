package com.easemob.kefu.rtcmedia.dep;

import org.springframework.stereotype.Component;

/**
 * fallback implementation
 *
 * Created by wangchunye on 10/19/16.
 */
@Component
public class WebAppApiFallback implements WebAppApi {

    @Override
    public ResponseRegisterImUser registerImUserInternal(
            RequestRegisterImUser requestRegisterImUser) {
        return null;
    }
}
