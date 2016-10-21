package com.easemob.weichat.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * 新东方iframe中访客信息
 * @author zhaoqiang
 *
 */
@Data
@Entity
@Table(name = "xdfvisitoruser")
@JsonInclude(JsonInclude.Include.NON_NULL)

public class XdfVisitor implements Serializable {

	private static final long serialVersionUID = 679545693245207947L;
	private Integer tenantId;
    @Id
    private String userId;
    
    private String trueName;  //姓名
    private Integer sex = 0;    //性别,0=未知，1=男，2=女
    private String qq;            //QQ号码
    private String email;        //邮箱
    private String companyName;  //公司名称
    private String description;    //描述
    private String telephone; //固定电话
    private String mobilephone1; //手机1
    private String mobilephone2; //手机2 
    private String studentNo; //学员号
    private String studentType;//学员类型
    private String studentGrade; //学员年级
    private String studentSchool; //学校
    private String consultCategory; //咨询类别： 下拉选择  课程类咨询	事务服务类咨询	投诉/建议	市场活动及预约	商机挖掘
    private String consultDept; //咨询部门 下拉选择  国外北美	国外英联邦	英语学习部	国内部	中学部	少儿部	多语种部	国际游学部	企业培训	个性化	VIP部
    private String recommendIllustrate; //推荐说明	下拉选择	1*2推荐	退班挽留	主推班	套班	续班
    private String className1; //班级名称1
    private String classNo1; //班号1
    private String className2; //班级名称2
    private String classNo2; //班号2
    private String purposeClass; //意向班级
   
    @Column(updatable=false)
    private String creator; //创建人 系统自建 保存访客信息的人
    private String followAgent; //跟踪人 系统自建 更新访客信息的人
    
    private String consultOrderType; //咨询工单类型	下拉选择	事物服务	疑问	投诉单	预约单
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(updatable=false)
    private Date createDateTime;
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date lastUpdateDateTime;
    
    @Transient
    private String sexStr;
}
