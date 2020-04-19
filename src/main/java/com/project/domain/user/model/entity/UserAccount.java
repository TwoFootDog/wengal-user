package com.project.domain.user.model.entity;

import com.project.domain.base.AbstractEntity;
import com.project.domain.base.AggregateRoot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "usr_acct_mst")   // 유저 계정 마스터 테이블
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount extends AbstractEntity implements AggregateRoot {
    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "nickname", nullable = false, unique = true, length = 50)
    private String nickname;

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
