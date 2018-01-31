package com.avantir.blowfish.messaging.model;

import com.avantir.blowfish.model.BaseModel;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lekanomotayo on 04/01/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_iso_bridges")
@SuppressWarnings("serial")
public class ISOBridge extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "class_name", nullable = false)
    private String className;
    @Column(name = "iso_packager_id", nullable = false)
    private String isoPackagerName;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "status", nullable = false)
    private int status = 1;
    @Column(name = "binary_bitmap", nullable = false)
    private boolean binaryBitmap = true;
    //BinaryMessages
    //CharacterEncoding

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public String getIsoPackagerName() {
        return isoPackagerName;
    }

    public void setIsoPackagerName(String isoPackagerName) {
        this.isoPackagerName = isoPackagerName;
    }

    public boolean isBinaryBitmap() {
        return binaryBitmap;
    }

    public void setBinaryBitmap(boolean binaryBitmap) {
        this.binaryBitmap = binaryBitmap;
    }
}
