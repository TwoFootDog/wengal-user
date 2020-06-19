package com.project.domain.user.model.entity;

import com.project.domain.base.AbstractEntity;
import com.project.domain.base.AggregateRoot;
import com.project.util.StringPrefixedSequenceIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "usr_acct_mst")   // 유저 계정 마스터 테이블
@NoArgsConstructor
public class UserAccount implements AggregateRoot {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GJ_SEQ")
    @GenericGenerator(
            name = "GJ_SEQ",
            strategy = "com.project.util.StringPrefixedSequenceIdGenerator",
            parameters = {
                    // @SequenceGenerator는 기본이 50이므로 50을 추천하지만(성능 문제), 시퀀스가 1씩 증가하는 경우는 1로 해야함
                    // @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "50"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "GJ"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%08d") })
    @Column(name = "user_id", nullable = false, unique = true, length = 10)
    protected String userId;

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

    public UserAccount(String email, String password, String nickname, String regId, Date regDate, String updId, Date updDate) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.regId = regId;
        this.regDate = regDate;
        this.updId = updId;
        this.updDate = updDate;
    }

}
