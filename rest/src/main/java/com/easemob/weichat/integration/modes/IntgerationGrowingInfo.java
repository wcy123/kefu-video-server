package com.easemob.weichat.integration.modes;

import lombok.Data;

@Data
public class IntgerationGrowingInfo {
	private long timestamp;
	private int tenantId;
	private String growingioId;
	private String userId;
}
