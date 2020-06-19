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
@Table(name = "usr_auth_mst",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "auth_name"})})   // 유저 권한 테이블
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthority extends AbstractEntity implements AggregateRoot {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @Column(name = "auth_name", length = 20)
    private String authName;

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
