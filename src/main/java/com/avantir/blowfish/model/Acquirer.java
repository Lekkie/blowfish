package com.avantir.blowfish.model;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

/**
 * Created by lekanomotayo on 07/01/2018.
 */
@Entity
@DynamicInsert
@Table(name = "tbl_acquirers")
@Cacheable(true)
@SuppressWarnings("serial")
public class Acquirer extends BaseModel{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    String name;
    @Column(name = "code", nullable = false, unique = true)
    String code;
    @Column(name = "cbn_code", nullable = false, unique = true)
    String cbnCode;
    @Column(name = "address", nullable = false)
    String address;
    @Column(name = "phoneNo", nullable = false)
    String phoneNo;
    @Column(name = "domain_id", nullable = false)
    Long domain_id;
    @Column(name = "enable_all_tran_type", nullable = false)
    boolean enableAllTranType;
    @Column(name = "enable_all_bin", nullable = false)
    boolean enableAllBin;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "status", nullable = false)
    private int status = 1;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Long getDomain_id() {
        return domain_id;
    }

    public void setDomain_id(Long domain_id) {
        this.domain_id = domain_id;
    }

    public boolean isEnableAllTranType() {
        return enableAllTranType;
    }

    public void setEnableAllTranType(boolean enableAllTranType) {
        this.enableAllTranType = enableAllTranType;
    }

    public boolean isEnableAllBin() {
        return enableAllBin;
    }

    public void setEnableAllBin(boolean enableAllBin) {
        this.enableAllBin = enableAllBin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCbnCode() {
        return cbnCode;
    }

    public void setCbnCode(String cbnCode) {
        this.cbnCode = cbnCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
