package com.avantir.blowfish.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by lekanomotayo on 07/01/2018.
 */
@Entity
@DynamicInsert
@Table(name = "tbl_key_cryptographic_types")
@SuppressWarnings("serial")
public class KeyCryptographicType extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "key_cryptographic_type_id")
    private Long keyCryptographicTypeId;
    @Column(name = "code", nullable = false, unique = true)
    private String code; // RSA, 3DES, AES
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "status", nullable = false)
    private int status = 1;

    public Long getKeyCryptographicTypeId() {
        return keyCryptographicTypeId;
    }

    public void setKeyCryptographicTypeId(Long keyCryptographicTypeId) {
        this.keyCryptographicTypeId = keyCryptographicTypeId;
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
