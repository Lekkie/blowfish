package com.avantir.blowfish.model;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lekanomotayo on 07/01/2018.
 */
@Entity
@DynamicInsert
@Table(name = "tbl_bins")
@SuppressWarnings("serial")
public class Bin extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long binId;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "status", nullable = false)
    private int status = 1;


    public Long getBinId() {
        return binId;
    }

    public void setBinId(Long binId) {
        this.binId = binId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
