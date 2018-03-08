package com.avantir.blowfish.model;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

/**
 * Created by lekanomotayo on 07/01/2018.
 */
@Entity
@DynamicInsert
@Table(name = "tbl_contacts")
@SuppressWarnings("serial")
public class Contact extends BaseModel{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long contactId;
    @Column(name = "fname", nullable = false, unique = true)
    String fname;
    @Column(name = "lname", nullable = false, unique = true)
    String lname;
    @Column(name = "address", nullable = false)
    String address;
    @Column(name = "phoneNo", nullable = true)
    String phoneNo;
    @Column(name = "status", nullable = false)
    private int status = 1;


    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
