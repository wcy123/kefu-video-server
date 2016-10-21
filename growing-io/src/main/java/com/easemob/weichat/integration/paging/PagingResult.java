package com.easemob.weichat.integration.paging;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author stliu @ apache.org
 */
@Data
@NoArgsConstructor
public class PagingResult<T> {
    private  List<T> entities;
    @JsonProperty("next")
    private  String nextCursor;
    @JsonProperty("previous")
    private  String previousCursor;

    public PagingResult(List<T> elements, String nextCursor, String previousCursor) {
        this.entities = elements;
        this.nextCursor = nextCursor;
        this.previousCursor = previousCursor;
    }
}
