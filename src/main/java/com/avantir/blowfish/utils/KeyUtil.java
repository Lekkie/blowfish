package com.avantir.blowfish.utils;


import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Created by lekanomotayo on 20/02/2018.
 */
public class KeyUtil {


    public static String encryptWithRSA(String publicKeyBase64, String cipherText){

        byte[] encodedBytes = null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(publicKeyBase64);
            /*
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedBytes);
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(byteArrayInputStream);
            PublicKey publicKey = cert.getPublicKey();
            */
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE,  publicKey);
            encodedBytes = cipher.doFinal(cipherText.getBytes());
            return Base64.getEncoder().encodeToString(encodedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
