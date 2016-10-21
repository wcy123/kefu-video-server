package com.easemob.weichat.integration.paging;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author stliu @ apache.org
 */
@Data
@NoArgsConstructor
public class PagingRequest {
    @Max(100)
    @Min(1)
    private  Integer limit;
    private  String cursor;

    public PagingRequest(Integer limit, String cursor) {
        this.limit = limit;
        this.cursor = cursor;
    }
}
