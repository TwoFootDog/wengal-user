package com.project.domain.user.model.entity;

import com.project.domain.base.AbstractEntity;
import com.project.domain.base.AggregateRoot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;

@Entity
@Data
@Table(name = "usr_info_mst",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "user_info_code"})})   // 유저 정보(공유정보) 테이블
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo extends AbstractEntity implements AggregateRoot {

    @Column(name = "user_id")
    private Long userId;        // 회원id

    @Column(name = " user_info_code", nullable = true, length = 3)
    private String userInfoCode;    // 회원정보코드(AGE, SEX, EDU, BIR, CTY)

    @Column(name = " user_info", nullable = true, length = 100)
    private String userInfo;        // 회원 정보(10, 20, man, women, university, 11/30, seoul...)

    @Column(name = "reg_id", length = 20)
    private String regId;

    @CreationTimestamp
    @Column(name = "reg_dt")
    private Date regDate;

    @Column(name = "upd_id", length = 20)
    private String updId;

    @CreationTimestamp
    @Column(name = "upd_dt")
    private Date updDate;
}
