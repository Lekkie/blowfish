package com.avantir.blowfish.model;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lekanomotayo on 16/01/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_key_versions")
@SuppressWarnings("serial")
public class KeyVersion extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "version", nullable = false, unique = true)
    private String version; //e.g 1.0, 1.1
    @Column(name = "usage", nullable = false, unique = false)
    private String usage; //e.g Auth, Encrypt, Sign
    @Column(name = "algo", nullable = false, unique = false)
    private String algo; //e.g SHA512,SHA256
    @Column(name = "salt", nullable = true)
    private String salt; //e.g SHA512,SHA256


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getAlgo() {
        return algo;
    }

    public void setAlgo(String algo) {
        this.algo = algo;
    }
}
