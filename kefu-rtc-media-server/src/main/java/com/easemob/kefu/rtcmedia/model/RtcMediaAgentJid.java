package com.easemob.kefu.rtcmedia.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RtcMediaAgentJid {
    @Id
    @GeneratedValue
    private Long id;
    private String agentName;
    private String orgName;
    private String appName;
    private String userName; // <orgName>#<appName>_<userName>
    private String password;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class RtcMediaAgentJidBuilder {
    }
}
