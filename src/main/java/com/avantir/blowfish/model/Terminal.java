package com.avantir.blowfish.model;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lekanomotayo on 07/01/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_terminals")
@SuppressWarnings("serial")
public class Terminal  extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long terminalId;
    @Column(name = "code", nullable = false, unique = true)
    String code;
    @Column(name = "serial_no", nullable = false, unique = true)
    String serialNo;
    @Column(name = "manufacturer", nullable = true)
    String manufacturer;
    @Column(name = "model_no", nullable = true)
    String modelNo;
    @Column(name = "build_no", nullable = true)
    String buildNo;
    @Column(name = "os", nullable = true)
    String os;
    @Column(name = "os_version", nullable = true)
    String osVersion;
    @Column(name = "firmware_no", nullable = true)
    String firmwareNo;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "status", nullable = false)
    private int status = 1;


    public Long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
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


    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getBuildNo() {
        return buildNo;
    }

    public void setBuildNo(String buildNo) {
        this.buildNo = buildNo;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getFirmwareNo() {
        return firmwareNo;
    }

    public void setFirmwareNo(String firmwareNo) {
        this.firmwareNo = firmwareNo;
    }
}
