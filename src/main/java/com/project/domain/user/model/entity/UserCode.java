package com.project.domain.user.model.entity;

import com.project.domain.base.AbstractEntity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "usr_code_mst",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"code_fg", "code_value"})})
@Entity
public class UserCode extends AbstractEntity {
    @Column(name = "code_fg", length = 20)
    private String codeFg;

    @Column(name = "code_value", length = 20)
    private String codeValue;

/*    @EmbeddedId
    private UserCodePK userCodePK;*/

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
