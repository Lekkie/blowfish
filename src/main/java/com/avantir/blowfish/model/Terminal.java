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
    private Long id;
    @Column(name = "code", nullable = false, unique = true)
    String code;
    @Column(name = "device_serial_no", nullable = false, unique = true)
    String deviceSerialNo;
    @Column(name = "terminal_parameter_group", nullable = false, unique = true)
    Long terminalParameterGroup;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "status", nullable = false)
    private int status = 1;

    // Add public key
    // terminal parameter download id?


    public String getDeviceSerialNo() {
        return deviceSerialNo;
    }

    public void setDeviceSerialNo(String deviceSerialNo) {
        this.deviceSerialNo = deviceSerialNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getTerminalParameterGroup() {
        return terminalParameterGroup;
    }

    public void setTerminalParameterGroup(Long terminalParameterGroup) {
        this.terminalParameterGroup = terminalParameterGroup;
    }
}
