package com.avantir.blowfish.entity;

import com.avantir.blowfish.services.SecurityService;
import com.avantir.blowfish.utils.EntityType;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.hateoas.Link;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by lekanomotayo on 16/01/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_key_maps")
@SuppressWarnings("serial")
public class KeyMap extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    public KeyMap(){
    }

    public KeyMap(Builder builder){
        this.keyMapId = builder.keyMapId;
        this.code = builder.code;
        this.keyId = builder.keyId;
        this.createdBy = builder.createdBy;
        createdOn = builder.createdOn;
    }


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "key_map_id")
    private Long keyMapId;
    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "key_id", nullable = false)
    private Long keyId;

    public Long getKeyMapId() {
        return keyMapId;
    }

    public void setKeyMapId(Long keyMapId) {
        this.keyMapId = keyMapId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyMap key = (KeyMap) o;

        if (keyMapId != null ? !keyMapId.equals(key.keyMapId) : key.keyMapId != null) return false;
        if (code != null ? !code.equals(key.code) : key.code != null) return false;
        if (keyId != null ? !keyId.equals(key.keyId) : key.keyId != null) return false;
        if (createdBy != null ? !createdBy.equals(key.createdBy) : key.createdBy != null) return false;

        List<Link> linkList = Optional.ofNullable(getLinks()).orElse(new ArrayList<>());
        for(Link thisLink: linkList){
            Link keyLinkList = key.getLink(thisLink.getRel());
            if (keyLinkList != null ? !thisLink.getHref().equals(keyLinkList.getHref()) : keyLinkList.getHref() != null) return false;
        }
        List<Link> keyLinkList = Optional.ofNullable(key.getLinks()).orElse(new ArrayList<>());
        for(Link keyLink: keyLinkList){
            Link thisLink = getLink(keyLink.getRel());
            if (thisLink != null ? !keyLink.getHref().equals(thisLink.getHref()) : thisLink.getHref() != null) return false;
        }
        return createdOn != null ? createdOn.equals(key.createdOn) : key.createdOn == null;
    }

    @Override
    public int hashCode() {
        int result = EntityType.KEY.getId();
        result = 31 * result + (keyMapId != null ? keyMapId.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (keyId != null ? keyId.hashCode() : 0);
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
                "id='" + keyMapId + '\'' +
                ", code='" + code + '\'' +
                ", keyId='" + keyId + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn='" + createdOn + '\'' +
                '}';
    }


    public static class Builder{

        private Long keyMapId;
        private String code;
        private Long keyId;
        private String createdBy;
        private Date createdOn;


        public Builder keyMapId(Long val){
            keyMapId = val;
            return this;
        }
        public Builder code(String val){
            code = val;
            return this;
        }
        public Builder keyId(Long val){
            keyId = val;
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


        public KeyMap build(){
            return new KeyMap(this);
        }
    }

}
