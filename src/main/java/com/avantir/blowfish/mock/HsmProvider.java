package com.avantir.blowfish.mock;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by lekanomotayo on 18/04/2018.
 */

@Service
public class HsmProvider {


    private static Map<String, String> encryptedKeyClearKeyMap = new HashMap();

    static{
        encryptedKeyClearKeyMap.put("", "DBEECACCB4210977ACE73A1D873CA59F");
    }

    private static final String LMK = "10101010101010101010101010101010";
    private static final String LMK_CHK_DIGIT = "82E13665B4624DF5";

    //public Key generate
    //translateISO1PINZPK2ISOPINZPK(srcZMK, srcZPK, destZMK), returns destZPK (under given srcZMK)
    //translateISO1PINZPK2ZPK
    //translateISOPINZPK2ISO1PINZPK
    //translateISOPINZPK2ZPK
    //translateZPKZMK2LMK
    ///translateZPKLMK2ZMK
    //generateHash(data)
    //generate3DESKey
    //import3DESKey
    //translateZMKFromUnderLMK(srcZMK) returns destZMK (under HSM LMK)
    //generateZMK() returns ZMK (under HSM LMK)
    //generateZPKUnderZMK(zmk) returns ZPK (under given ZMK)


    public String encryptWith3DES(String key, String cipherHex)  {
        // create a binary key from the argument key (seed)
        try{
            byte[] tmp = hex2bin(key);
            byte[] keyBytes = new byte[24];
            System.arraycopy(tmp, 0, keyBytes, 0, 16);
            System.arraycopy(tmp, 0, keyBytes, 16, 8);
            SecretKey sk = new SecretKeySpec(keyBytes, "DESede");
            // create an instance of cipher
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, sk);

            // enctypt!
            byte[] cipherByte = hex2bin(cipherHex);
            byte[] encrypted = cipher.doFinal(cipherByte);
            return bin2hex(encrypted);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return null;

    }

    public String decryptWith3DES(String key, String cryptogram) {
        // create a binary key from the argument key (seed)
        try{
            byte[] tmp = hex2bin(key);
            byte[] keyBytes = new byte[24];
            System.arraycopy(tmp, 0, keyBytes, 0, 16);
            System.arraycopy(tmp, 0, keyBytes, 16, 8);
            SecretKey sk = new SecretKeySpec(keyBytes, "DESede");

            // do the decryption with that key
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, sk);
            byte[] cryptogramByte = hex2bin(cryptogram);
            byte[] decrypted = cipher.doFinal(cryptogramByte);
            return bin2hex(decrypted);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


    public String encryptWithRSA(String publicKeyHex, String cipherText){

        byte[] encodedBytes = null;
        try {
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(hex2bin(publicKeyHex));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE,  publicKey);
            encodedBytes = cipher.doFinal(hex2bin(cipherText));
            String cryptogram = bin2hex(encodedBytes);
            return cryptogram;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decryptWithRSA(String privateKeyHex, String cryptogram){
        try {
            //BigInteger privateExponent = new BigInteger(privateExponentHex, 16);
            //BigInteger modulus = new BigInteger(modulusHex, 16);
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(hex2bin(privateKeyHex));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(privKeySpec);
            Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c.init(Cipher.DECRYPT_MODE,  privateKey);
            byte[] decryptedBytes = c.doFinal(hex2bin(cryptogram));
            return bin2hex(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    /*

    public String encryptRSA(String publicKeyBase64, String cipherText){
        byte[] encodedBytes = null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(publicKeyBase64);

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

        public String decryptRSA(String secretKeyBase64, String cryptogram){
                return null;
                }


        public String decryptWithRSA(String privateKeyHex, String cryptogram){
                try {
                //BigInteger privateExponent = new BigInteger(privateExponentHex, 16);
                //BigInteger modulus = new BigInteger(modulusHex, 16);
                PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privateKeyHex.getBytes());
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PrivateKey privateKey = keyFactory.generatePrivate(privKeySpec);
                Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                c.init(Cipher.DECRYPT_MODE,  privateKey);
                byte[] decryptedBytes = c.doFinal(hex2bin(cryptogram));
                return bin2hex(decryptedBytes);
                } catch (Exception e) {
                e.printStackTrace();
                }
                return  null;
        }
        */




    public byte[] hex2bin(String hex)
    {
        if ((hex.length() & 0x01) == 0x01)
            throw new IllegalArgumentException();
        byte[] bytes = new byte[hex.length() / 2];
        for (int idx = 0; idx < bytes.length; ++idx) {
            int hi = Character.digit((int) hex.charAt(idx * 2), 16);
            int lo = Character.digit((int) hex.charAt(idx * 2 + 1), 16);
            if ((hi < 0) || (lo < 0))
                throw new IllegalArgumentException();
            bytes[idx] = (byte) ((hi << 4) | lo);
        }
        return bytes;
    }

    public String bin2hex(byte[] bytes)
    {
        char[] hex = new char[bytes.length * 2];
        for (int idx = 0; idx < bytes.length; ++idx) {
            int hi = (bytes[idx] & 0xF0) >>> 4;
            int lo = (bytes[idx] & 0x0F);
            hex[idx * 2] = (char) (hi < 10 ? '0' + hi : 'A' - 10 + hi);
            hex[idx * 2 + 1] = (char) (lo < 10 ? '0' + lo : 'A' - 10 + lo);
        }
        return new String(hex);
    }


}
