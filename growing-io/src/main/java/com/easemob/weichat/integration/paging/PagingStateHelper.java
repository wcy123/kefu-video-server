package com.easemob.weichat.integration.paging;

import com.datastax.driver.core.PagingState;

import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Optional;

/**
 * @author stliu @ apache.org
 */
public class PagingStateHelper {
    private PagingStateHelper(){};
  
    public static Optional<PagingState> toPagingState(String base64Str){
        if(StringUtils.isEmpty(base64Str)){
            return Optional.empty();
        }
        byte[] bytes = Base64.getUrlDecoder().decode(base64Str);
        PagingState pagingState = PagingState.fromBytes(bytes);
        return Optional.of(pagingState);
    }

    public static Optional<String> toBase64PagingState(PagingState pagingState){
        if(pagingState == null){
            return Optional.empty();
        }
        String base64Str = Base64.getUrlEncoder().encodeToString(pagingState.toBytes());
        return Optional.of(base64Str);
    }
}
