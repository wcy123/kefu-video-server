package com.easemob.weichat.integration.data;

import java.io.Serializable;
import lombok.Data;

/**
 * @author shengyp
 * @since 09/18/16
 */

@Data
public class ReceivedId implements Serializable {
    private static final long serialVersionUID = -6830717801779966035L;
    private String id;
}
