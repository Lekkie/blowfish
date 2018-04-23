package com.avantir.blowfish.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lekanomotayo on 07/01/2018.
 */
@Entity
@DynamicInsert
@Table(name = "tbl_hsm_endpoints")
@Cacheable(true)
@SuppressWarnings("serial")
public class HsmEndpoint extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    public HsmEndpoint(){
    }

    public HsmEndpoint(Builder builder){
        this.hsmEndpointId = builder.hsmEndpointId;
        this.ip = builder.ip;
        this.port = builder.port;
        this.timeout = builder.timeout;
        this.sslEnabled = builder.sslEnabled;
        this.description = builder.description;
        this.status = builder.status;
        this.createdBy = builder.createdBy;
        createdOn = builder.createdOn;
    }


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long hsmEndpointId;
    @Column(name = "ip", nullable = false)
    private String ip;
    @Column(name = "port", nullable = false)
    private int port;
    @Column(name = "timeout", nullable = false)
    private int timeout;
    @Column(name = "ssl_enabled", nullable = false)
    private boolean sslEnabled;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "status", nullable = false)
    private int status = 1;


    public Long getHsmEndpointId() {
        return hsmEndpointId;
    }

    public void setHsmEndpointId(Long hsmEndpointId) {
        this.hsmEndpointId = hsmEndpointId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
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


    public static class Builder{

        private Long hsmEndpointId;
        private String ip;
        private int port;
        private int timeout;
        private boolean sslEnabled;
        private String description;
        private int status;
        private String createdBy;
        private Date createdOn;


        public Builder hsmEndpointId(Long val){
            hsmEndpointId = val;
            return this;
        }
        public Builder ip(String val){
            ip = val;
            return this;
        }
        public Builder port(int val){
            port = val;
            return this;
        }
        public Builder timeout(int val){
            timeout = val;
            return this;
        }
        public Builder sslEnabled(boolean val){
            sslEnabled = val;
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


        public HsmEndpoint build(){
            return new HsmEndpoint(this);
        }
    }


}
