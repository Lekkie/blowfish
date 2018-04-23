package com.avantir.blowfish.entity;

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
@Table(name = "tbl_active_hsms")
@SuppressWarnings("serial")
public class ActiveHsm extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    public ActiveHsm(){
    }

    public ActiveHsm(Builder builder){
        this.activeHsmId = builder.activeHsmId;
        this.hsmEndpointId = builder.hsmEndpointId;
        this.code = builder.code;
        this.createdBy = builder.createdBy;
        createdOn = builder.createdOn;
    }


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "active_hsm_id")
    private Long activeHsmId;
    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "hsm_endpoint_id", nullable = false)
    private Long hsmEndpointId;

    public Long getActiveHsmId() {
        return activeHsmId;
    }

    public void setActiveHsmId(Long activeHsmId) {
        this.activeHsmId = activeHsmId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getHsmEndpointId() {
        return hsmEndpointId;
    }

    public void setHsmEndpointId(Long hsmEndpointId) {
        this.hsmEndpointId = hsmEndpointId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActiveHsm key = (ActiveHsm) o;

        if (activeHsmId != null ? !activeHsmId.equals(key.activeHsmId) : key.activeHsmId != null) return false;
        if (code != null ? !code.equals(key.code) : key.code != null) return false;
        if (hsmEndpointId != null ? !hsmEndpointId.equals(key.hsmEndpointId) : key.hsmEndpointId != null) return false;
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
        result = 31 * result + (activeHsmId != null ? activeHsmId.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (hsmEndpointId != null ? hsmEndpointId.hashCode() : 0);
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
                "id='" + activeHsmId + '\'' +
                ", code='" + code + '\'' +
                ", hsmEndpointId='" + hsmEndpointId + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn='" + createdOn + '\'' +
                '}';
    }


    public static class Builder{

        private Long activeHsmId;
        private String code;
        private Long hsmEndpointId;
        private String createdBy;
        private Date createdOn;


        public Builder activeHsmId(Long val){
            activeHsmId = val;
            return this;
        }
        public Builder code(String val){
            code = val;
            return this;
        }
        public Builder hsmEndpointId(Long val){
            hsmEndpointId = val;
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


        public ActiveHsm build(){
            return new ActiveHsm(this);
        }
    }

}
