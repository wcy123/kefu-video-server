package com.easemob.kefu.rtcmedia.model;

import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
public class RtcMediaEventLog {
    @Id
    @GeneratedValue
    private Long id;

    @CreatedDate
    @Column(name = "created",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            insertable = false,
            updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    private String action;
    private String name;
}
