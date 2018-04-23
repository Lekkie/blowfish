package com.avantir.blowfish.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lekanomotayo on 07/01/2018.
 */
@Entity
@DynamicInsert
@Table(name = "tbl_key_usage_types")
@SuppressWarnings("serial")
public class KeyUsageType extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "key_usage_type_id")
    private Long keyUsageTypeId;
    @Column(name = "code", nullable = false, unique = true)
    private String code; // LMK, ZMK, ZPK, DEK
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "parent_allowed", nullable = false)
    private boolean parentAllowed;
    @Column(name = "status", nullable = false)
    private int status = 1;

    public Long getKeyUsageTypeId() {
        return keyUsageTypeId;
    }

    public void setKeyUsageTypeId(Long keyUsageTypeId) {
        this.keyUsageTypeId = keyUsageTypeId;
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

    public boolean isParentAllowed() {
        return parentAllowed;
    }

    public void setParentAllowed(boolean parentAllowed) {
        this.parentAllowed = parentAllowed;
    }
}
