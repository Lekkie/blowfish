package com.avantir.blowfish.messaging.entity;

import com.avantir.blowfish.entity.BaseModel;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_tcp_endpoints")
@SuppressWarnings("serial")
public class TCPEndpoint extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long tcpEndpointId;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    //@EmbeddedId
    //private TCPEndpointPortIP tcpEndpointPortIP;
    @Column(name = "ip", nullable = true)
    private String ip;
    @Column(name = "port", nullable = false)
    private int port = 0;
    @Column(name = "server", nullable = false)
    private boolean server = true;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "status", nullable = false)
    private int status = 1; // 0 - inactive, 1- active


    public TCPEndpoint(){
    }


    public Long getTcpEndpointId() {
        return tcpEndpointId;
    }

    public void setTcpEndpointId(Long tcpEndpointId) {
        this.tcpEndpointId = tcpEndpointId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
    public TCPEndpointPortIP getTcpEndpointPortIP() {
        return tcpEndpointPortIP;
    }

    public void setTcpEndpointPortIP(TCPEndpointPortIP tcpEndpointPortIP) {
        this.tcpEndpointPortIP = tcpEndpointPortIP;
    }
    */

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

    public boolean isServer() {
        return server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }

}
