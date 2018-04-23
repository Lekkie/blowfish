package com.avantir.blowfish.entity;

import com.avantir.blowfish.utils.EntityType;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.hateoas.Link;

import javax.persistence.*;
import java.util.*;

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

    public Acquirer(){
    }

    public Acquirer(Builder builder){
        this.acquirerId = builder.acquirerId;
        this.name = builder.name;
        this.code = builder.code;
        this.binCode = builder.binCode;
        this.cbnCode = builder.cbnCode;
        this.status = builder.status;
        this.address = builder.address;
        this.phoneNo = builder.phoneNo;
        this.description = builder.description;
        this.domainId = builder.domainId;
        this.enableAllTranType = builder.enableAllTranType;
        this.enableAllBin = builder.enableAllBin;
        this.description = builder.description;
        this.status = builder.status;
        this.createdBy = builder.createdBy;
        createdOn = builder.createdOn;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long acquirerId;
    @Column(name = "name", nullable = false, unique = true)
    String name;
    @Column(name = "code", nullable = false, unique = true)
    String code;
    @Column(name = "bin_code", nullable = false, unique = true)
    String binCode;
    @Column(name = "cbn_code", nullable = false, unique = true)
    String cbnCode;
    @Column(name = "address", nullable = false)
    String address;
    @Column(name = "phoneNo", nullable = false)
    String phoneNo;
    @Column(name = "domain_id", nullable = false)
    Long domainId;
    @Column(name = "enable_all_tran_type", nullable = false)
    boolean enableAllTranType;
    @Column(name = "enable_all_bin", nullable = false)
    boolean enableAllBin;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "status", nullable = false)
    private int status;

    /*
    //@OneToMany(mappedBy = "tbl_acquirers_bins")
    @OneToMany(mappedBy = "tbl_acquirers",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    //@JoinTable(name = "tbl_acquirers_bins", joinColumns = @JoinColumn(name = "acquirer_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "bin_id", referencedColumnName = "id"))
    private Set<Bin> binSet;
    */


    public Long getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(Long acquirerId) {
        this.acquirerId = acquirerId;
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

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
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

    public String getBinCode() {
        return binCode;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }



    /*
    public void addBin(Bin bin) {
        binSet.add(bin);
        bin.setAcquirer(this);
    }

    public void removeBin(Bin bin) {
        binSet.remove(bin);
        bin.setAcquirer(null);
    }
    */


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Acquirer acquirer = (Acquirer) o;

        if (acquirerId != null ? !acquirerId.equals(acquirer.acquirerId) : acquirer.acquirerId != null) return false;
        if (name != null ? !name.equals(acquirer.name) : acquirer.name != null) return false;
        if (code != null ? !code.equals(acquirer.code) : acquirer.code != null) return false;
        if (binCode != null ? !binCode.equals(acquirer.binCode) : acquirer.binCode != null) return false;
        if (cbnCode != null ? !cbnCode.equals(acquirer.cbnCode) : acquirer.cbnCode != null) return false;
        if (address != null ? !address.equals(acquirer.address) : acquirer.address != null) return false;
        if (phoneNo != null ? !phoneNo.equals(acquirer.phoneNo) : acquirer.phoneNo != null) return false;
        if (domainId != acquirer.domainId) return false;
        if (enableAllTranType != acquirer.enableAllTranType) return false;
        if (enableAllBin != acquirer.enableAllBin) return false;
        if (description != null ? !description.equals(acquirer.description) : acquirer.description != null) return false;
        if (status != acquirer.status) return false;
        if (createdBy != null ? !createdBy.equals(acquirer.createdBy) : acquirer.createdBy != null) return false;

        List<Link> linkList = Optional.ofNullable(getLinks()).orElse(new ArrayList<>());
        for(Link link: linkList){
            Link acquirerLink = acquirer.getLink(link.getRel());
            if (acquirerLink != null ? !link.getHref().equals(acquirerLink.getHref()) : acquirerLink.getHref() != null) return false;
        }
        List<Link> acquirerLinkList = Optional.ofNullable(acquirer.getLinks()).orElse(new ArrayList<>());
        for(Link acquirerLink: acquirerLinkList){
            Link thisLink = getLink(acquirerLink.getRel());
            if (thisLink != null ? !acquirerLink.getHref().equals(thisLink.getHref()) : thisLink.getHref() != null) return false;
        }

        return createdOn != null ? createdOn.equals(acquirer.createdOn) : acquirer.createdOn == null;
    }

    @Override
    public int hashCode() {
        int result = EntityType.ACQUIRER.getId();
        result = 31 * result + (acquirerId != null ? acquirerId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (binCode != null ? binCode.hashCode() : 0);
        result = 31 * result + (cbnCode != null ? cbnCode.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phoneNo != null ? phoneNo.hashCode() : 0);
        result = 31 * result + (domainId != null ? domainId.hashCode() : 0);
        result = 31 * result + (enableAllTranType ? 1 : 0);
        result = 31 * result + (enableAllBin ? 1 : 0);
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
                "id='" + acquirerId + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", binCode='" + binCode + '\'' +
                ", cbnCode='" + cbnCode + '\'' +
                ", address='" + address + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", domainId='" + domainId + '\'' +
                ", enableAllTranType='" + enableAllTranType + '\'' +
                ", enableAllBin='" + enableAllBin + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn='" + createdOn + '\'' +
                '}';
    }


    public static class Builder{

        private Long acquirerId;
        private String name;
        private String code;
        private String binCode;
        private String cbnCode;
        private String address;
        private String phoneNo;
        private Long domainId;
        boolean enableAllTranType;
        boolean enableAllBin;
        private String description;
        private int status;
        private String createdBy;
        private Date createdOn;


        public Builder acquirerId(Long val){
            acquirerId = val;
            return this;
        }
        public Builder name(String val){
            name = val;
            return this;
        }
        public Builder code(String val){
            code = val;
            return this;
        }
        public Builder binCode(String val){
            binCode = val;
            return this;
        }
        public Builder cbnCode(String val){
            cbnCode = val;
            return this;
        }
        public Builder address(String val){
            address = val;
            return this;
        }
        public Builder phoneNo(String val){
            phoneNo = val;
            return this;
        }
        public Builder domainId(Long val){
            domainId = val;
            return this;
        }
        public Builder enableAllTranType(boolean val){
            enableAllTranType = val;
            return this;
        }
        public Builder enableAllBin(boolean val){
            enableAllBin = val;
            return this;
        }
        public Builder description(String val){
            description = val;
            return this;
        }
        public Builder status(int val){
            status = val;
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


        public Acquirer build(){
            return new Acquirer(this);
        }
    }
}
