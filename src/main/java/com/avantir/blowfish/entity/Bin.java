package com.avantir.blowfish.entity;

import com.avantir.blowfish.utils.EntityType;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.hateoas.Link;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * Created by lekanomotayo on 07/01/2018.
 */
@Entity
@DynamicInsert
@Table(name = "tbl_bins")
@SuppressWarnings("serial")
public class Bin extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final transient Comparator<Bin> BY_BIN_CODE_LENGTH = new ByBinCodeLength();

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long binId;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "status", nullable = false)
    private int status = 1;

    //@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JoinColumn(name = "acquirer_id")
    //private Acquirer acquirer;



    public Long getBinId() {
        return binId;
    }

    public void setBinId(Long binId) {
        this.binId = binId;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bin bin = (Bin) o;

        if (binId != null ? !binId.equals(bin.binId) : bin.binId != null) return false;
        if (code != null ? !code.equals(bin.code) : bin.code != null) return false;
        if (description != null ? !description.equals(bin.description) : bin.description != null) return false;
        if (status != bin.status) return false;
        if (createdBy != null ? !createdBy.equals(bin.createdBy) : bin.createdBy != null) return false;

        List<Link> linkList = Optional.ofNullable(getLinks()).orElse(new ArrayList<>());
        for(Link link: linkList){
            Link binLink = bin.getLink(link.getRel());
            if (binLink != null ? !link.getHref().equals(binLink.getHref()) : binLink.getHref() != null) return false;
        }
        List<Link> acquirerLinkList = Optional.ofNullable(bin.getLinks()).orElse(new ArrayList<>());
        for(Link acquirerLink: acquirerLinkList){
            Link thisLink = getLink(acquirerLink.getRel());
            if (thisLink != null ? !acquirerLink.getHref().equals(thisLink.getHref()) : thisLink.getHref() != null) return false;
        }

        return createdOn != null ? createdOn.equals(bin.createdOn) : bin.createdOn == null;
    }

    @Override
    public int hashCode() {
        int result = EntityType.BIN.getId();
        result = 31 * result + (binId != null ? binId.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
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



    private static class ByBinCodeLength implements Comparator<Bin> {
        public int compare(Bin v, Bin w){
            int x = v.code.length();
            int y = w.code.length();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        }
    }
}
