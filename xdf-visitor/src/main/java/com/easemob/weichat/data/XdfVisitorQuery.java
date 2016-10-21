package com.easemob.weichat.data;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

/**
 * 访客查询
 *
 */
@Data
public class XdfVisitorQuery  {

    /**
     * 租户ID
     */
    private Integer tenantId;
    /**
     * 访客名称
     */
    private String visitorName;
    /**
     * 访客创建时间（开始范围条件）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date beginDate;
    /**
     * 访客创建时间（结束范围条件）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date endDate;
    
    private String fileEncoding;
    
}
