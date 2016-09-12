package com.easemob.weichat.integration.data;

import java.io.Serializable;
import lombok.Data;

/**
 * @author shengyp
 * @since 09/08/16
 */

@Data
public class NewVersionInfo implements Serializable {
    private static final long serialVersionUID = -5304512282800019216L;

    private String id;
    private String content;
}
