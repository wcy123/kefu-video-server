package com.easemob.kefu.rtcmedia.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.easemob.kefu.rtcmedia.protocol.CreateConference;
import com.easemob.kefu.rtcmedia.protocol.types.State;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RtcMediaEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String msgId;
    private String agentId;
    private String sid;
    private String visitorId;
    private CreateConference.MediaType mediaType;
    private String orgName;
    private String appName;
    private String visitorUserName;
    private String agentUserName;
    private State state;

    @OneToMany
    private List<RtcMediaEventLog> logs;

    @CreatedDate
    @Column(name = "created",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            nullable = false,
            insertable = false,
            updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date lastModified;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class RtcMediaEntityBuilder {
    }
}
