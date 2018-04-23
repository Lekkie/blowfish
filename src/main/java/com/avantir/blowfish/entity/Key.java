package com.avantir.blowfish.entity;

import com.avantir.blowfish.services.SecurityService;
import com.avantir.blowfish.utils.EntityType;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.hateoas.Link;

import javax.crypto.Cipher;
import javax.persistence.*;
import java.io.Serializable;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * Created by lekanomotayo on 16/01/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_keys")
@SuppressWarnings("serial")
public class Key extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    public Key(){
    }

    public Key(Builder builder){
        this.keyId = builder.keyId;
        this.data = builder.data;
        this.valeUnderParent = builder.valeUnderParent;
        this.status = builder.status;
        this.checkDigit = builder.checkDigit;
        this.keyCryptographicTypeId = builder.keyCryptographicTypeId;
        this.keyUsageTypeId = builder.keyUsageTypeId;
        this.description = builder.description;
        this.createdBy = builder.createdBy;
        createdOn = builder.createdOn;
    }

    // capture public, private,
    // Key parent,
    // Key type (RSA, Symmetric),
    // Key Length (single, double, triple),
    // Key usage (DEK, ZMK, ZPK)

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long keyId;
    @Column(name = "data", columnDefinition = "TEXT", nullable = false)
    private String data;
    @Column(name = "value_under_parent", columnDefinition = "TEXT", nullable = true)
    private String valeUnderParent; // if it has parent, store value under parent
    @Column(name = "check_digit", nullable = true)
    private String checkDigit;
    @Column(name = "key_cryptographic_type_id", nullable = false, unique = false)
    private Long keyCryptographicTypeId; //e.g RSA, 3DES, AES
    @Column(name = "key_usage_type_id", nullable = false, unique = false)
    private Long keyUsageTypeId; //e.g LMK, ZMK, ZPK, DEK
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "status", nullable = false)
    private int status;




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;

        if (keyId != null ? !keyId.equals(key.keyId) : key.keyId != null) return false;
        if (data != null ? !data.equals(key.data) : key.data != null) return false;
        if (valeUnderParent != null ? !valeUnderParent.equals(key.valeUnderParent) : key.valeUnderParent != null) return false;
        if (checkDigit != null ? !checkDigit.equals(key.checkDigit) : key.checkDigit != null) return false;
        if (keyUsageTypeId != null ? !keyUsageTypeId.equals(key.keyUsageTypeId) : key.keyUsageTypeId != null) return false;
        if (keyCryptographicTypeId != null ? !keyCryptographicTypeId.equals(key.keyCryptographicTypeId) : key.keyCryptographicTypeId != null) return false;
        if (description != null ? !description.equals(key.description) : key.description != null) return false;
        if (status != key.status) return false;
        if (createdBy != null ? !createdBy.equals(key.createdBy) : key.createdBy != null) return false;

        List<Link> linkList = Optional.ofNullable(getLinks()).orElse(new ArrayList<>());
        for(Link link: linkList){
            Link acquirerLink = key.getLink(link.getRel());
            if (acquirerLink != null ? !link.getHref().equals(acquirerLink.getHref()) : acquirerLink.getHref() != null) return false;
        }
        List<Link> acquirerLinkList = Optional.ofNullable(key.getLinks()).orElse(new ArrayList<>());
        for(Link acquirerLink: acquirerLinkList){
            Link thisLink = getLink(acquirerLink.getRel());
            if (thisLink != null ? !acquirerLink.getHref().equals(thisLink.getHref()) : thisLink.getHref() != null) return false;
        }
        return createdOn != null ? createdOn.equals(key.createdOn) : key.createdOn == null;
    }

    @Override
    public int hashCode() {
        int result = EntityType.KEY.getId();
        result = 31 * result + (keyId != null ? keyId.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (valeUnderParent != null ? valeUnderParent.hashCode() : 0);
        result = 31 * result + (checkDigit != null ? checkDigit.hashCode() : 0);
        result = 31 * result + (keyUsageTypeId != null ? keyUsageTypeId.hashCode() : 0);
        result = 31 * result + (keyCryptographicTypeId != null ? keyCryptographicTypeId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + status;
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (createdOn != null ? createdOn.hashCode() : 0);
        List<Link> linkList = Optional.ofNullable(getLinks()).orElse(new ArrayList<>());
        for(Link link: linkList){
            result = 31 * result + (link.getRel() != null ? link.getRel().hashCode() : 0);
            result = 31 * result + (link.getHref() != null ? link.getHref().hashCode() : 0);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Key{" +
                "id='" + keyId + '\'' +
                ", data='" + data + '\'' +
                ", valeUnderParent='" + valeUnderParent + '\'' +
                ", checkDigit='" + checkDigit + '\'' +
                ", keyUsageTypeId='" + keyUsageTypeId + '\'' +
                ", keyCryptographicTypeId='" + keyCryptographicTypeId + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn='" + createdOn + '\'' +
                '}';
    }


    public static class Builder{

        private Long keyId;
        private String data;
        private String valeUnderParent;
        private String checkDigit;
        private String description;
        private int status;
        private String createdBy;
        private Date createdOn;
        private Long keyUsageTypeId;
        private Long keyCryptographicTypeId;


        public Builder keyId(Long val){
            keyId = val;
            return this;
        }
        public Builder valeUnderParent(String val){
            valeUnderParent = val;
            return this;
        }
        public Builder data(String val){
            data = val;
            return this;
        }
        public Builder status(int val){
            status = val;
            return this;
        }
        public Builder keyCryptographicTypeId(Long val){
            keyCryptographicTypeId = val;
            return this;
        }
        public Builder keyUsageTypeId(Long val){
            keyUsageTypeId = val;
            return this;
        }
        public Builder checkDigit(String val){
            checkDigit = val;
            return this;
        }
        public Builder description(String val){
            description = val;
            return this;
        }
        public Builder createdBy(String val){
            createdBy = val;
            return this;
        }
        public Builder createdOn(Date val){
            createdOn = val;
            return this;
        }


        public Key build(){
            return new Key(this);
        }
    }


    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCheckDigit() {
        return checkDigit;
    }

    public void setCheckDigit(String checkDigit) {
        this.checkDigit = checkDigit;
    }

    public Long getKeyCryptographicTypeId() {
        return keyCryptographicTypeId;
    }

    public void setKeyCryptographicTypeId(Long keyCryptographicTypeId) {
        this.keyCryptographicTypeId = keyCryptographicTypeId;
    }

    public Long getKeyUsageTypeId() {
        return keyUsageTypeId;
    }

    public void setKeyUsageTypeId(Long keyUsageTypeId) {
        this.keyUsageTypeId = keyUsageTypeId;
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

    public String getValeUnderParent() {
        return valeUnderParent;
    }

    public void setValeUnderParent(String valeUnderParent) {
        this.valeUnderParent = valeUnderParent;
    }
}
