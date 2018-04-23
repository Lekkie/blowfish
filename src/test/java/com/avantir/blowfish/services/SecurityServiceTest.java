package com.avantir.blowfish.services;

import com.avantir.blowfish.exceptions.BlowfishProcessingErrorException;
import com.avantir.blowfish.utils.TestUtils;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created by lekanomotayo on 19/04/2018.
 */
public class SecurityServiceTest {

    SecurityService securityService;

    @Before
    public void setup() throws Exception{
        securityService = new SecurityService();
    }

}
