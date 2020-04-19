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
@Table(name = "usr_auth_map")   // 유저 권한 테이블
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthority extends AbstractEntity implements AggregateRoot {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "usr_acct_id")
    private UserAccount userAccount;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "auth_name", referencedColumnName = "code_value")
    private UserCode userCode;

    @Column(name = "reg_id", length = 20)
    private String regId;

    @CreationTimestamp
    @Column(name = "reg_dt")
    private Date regDt;

    @Column(name = "upd_id",  length = 20)
    private String updId;

    @CreationTimestamp
    @Column(name = "upd_dt")
    private Date updDt;
}
