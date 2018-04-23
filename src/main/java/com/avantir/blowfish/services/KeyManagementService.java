package com.avantir.blowfish.services;

import com.avantir.blowfish.clients.tcp.ThalesHSMClient;
import com.avantir.blowfish.entity.Key;
import com.avantir.blowfish.entity.KeyCryptographicType;
import com.avantir.blowfish.entity.KeyMap;
import com.avantir.blowfish.entity.KeyUsageType;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.exceptions.BlowfishIllegalArgumentException;
import com.avantir.blowfish.exceptions.BlowfishProcessingErrorException;
import com.avantir.blowfish.exceptions.BlowfishRuntimeException;
import com.avantir.blowfish.model.KeyComponent;
import com.avantir.blowfish.providers.KeyProvider;
import com.avantir.blowfish.utils.ErrorType;
import com.avantir.hsm.client.messages.*;

import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.x509.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by lekanomotayo on 21/04/2018.
 */

@Service
public class KeyManagementService {

    @Autowired
    ThalesHSMClient thalesHSMClient;
    @Autowired
    KeyCryptographicTypeService keyCryptographicTypeService;
    @Autowired
    KeyUsageTypeService keyUsageTypeService;
    @Autowired
    KeyService keyService;
    @Autowired
    KeyMapService keyMapService;
    @Autowired
    KeyProvider keyProvider;


    /***
     *
     * Generate an RSA Key. The returned private key is encrypted under HSM LMK
     * @param keyLength e.g. 512, 1024, 2048
     * @param exponent 010001
     * @throws Exception
     */
    public Optional<Key> generatePrivateKeyWithHSM(String keyLength, String exponent){
        try{
            GenerateRSA generateRSA = new GenerateRSA(keyLength, exponent);
            byte[] requestBytes = generateRSA.pack();
            byte[] responseBytes = thalesHSMClient.sendReceive(requestBytes);
            generateRSA.unpack(responseBytes);
            String publicKeyHex = generateRSA.getPubEncoded();
            //String publicKeyLen = String.format("%4s", new Object[]{String.valueOf(publicKeyHex.length())}).replace(' ', '0');
            String privateKeyHex = generateRSA.getPrivEncoded();
            //String privateKeyLen = String.format("%4s", new Object[]{String.valueOf(privateKeyHex.length())}).replace(' ', '0');
            KeyCryptographicType keyCryptographicType = keyCryptographicTypeService.findByCode("RSA").orElseThrow(() -> new BlowfishEntityNotFoundException("KeyCryptographicType RSA"));
            KeyUsageType keyUsageType = keyUsageTypeService.findByCode("ZMK").orElseThrow(() -> new BlowfishEntityNotFoundException("KeyUsageType ZMK"));
            Key key = new Key.Builder()
                    //.data(publicKeyLen + publicKeyHex + privateKeyLen + privateKeyHex)
                    .data(publicKeyHex)
                    .valeUnderParent(privateKeyHex)
                    .keyCryptographicTypeId(keyCryptographicType.getKeyCryptographicTypeId())
                    .keyUsageTypeId(keyUsageType.getKeyUsageTypeId())
                    .build();
            return Optional.ofNullable(key);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return Optional.empty();
    }


    /***
     * Generate Encrypted Data Encryption Key
     * @throws Exception
     */
    public Optional<Key> generateDEKWithHSM(String zmk) {

        try{
            GenerateDEK generateDEK = new GenerateDEK(zmk);
            byte[] requestBytes = generateDEK.pack();
            byte[] responseBytes = thalesHSMClient.sendReceive(requestBytes);
            generateDEK.unpack(responseBytes);
            KeyCryptographicType keyCryptographicType = keyCryptographicTypeService.findByCode("3DES").orElseThrow(() -> new BlowfishEntityNotFoundException("KeyCryptographicType 3DES"));
            KeyUsageType keyUsageType = keyUsageTypeService.findByCode("DEK").orElseThrow(() -> new BlowfishEntityNotFoundException("KeyUsageType DEK"));
            Key key = new Key.Builder()
                    .data(generateDEK.getKeyUnderLmk())
                    .valeUnderParent(generateDEK.getKeyUnderZmk())
                    .checkDigit(generateDEK.getCheckDigit())
                    .keyCryptographicTypeId(keyCryptographicType.getKeyCryptographicTypeId())
                    .keyUsageTypeId(keyUsageType.getKeyUsageTypeId())
                    .build();
            return Optional.ofNullable(key);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return Optional.empty();
    }


    /***
     *
     * @param zmkKeyUnderPublicKey
     * @param privateKeyUnderLMK - Encrypted Private key generated from generatePrivateKey()
     * @throws Exception
     */
    public Optional<Key> importZMKKeyUnderRsaToUnderLMKWithHSM(String zmkKeyUnderPublicKey, String privateKeyUnderLMK){
        try{
            ImportKey importKey = new ImportKey(zmkKeyUnderPublicKey, privateKeyUnderLMK, "0000");
            byte[] requestBytes = importKey.pack();
            byte[] responseBytes = thalesHSMClient.sendReceive(requestBytes);
            importKey.unpack(responseBytes);
            KeyCryptographicType keyCryptographicType = keyCryptographicTypeService.findByCode("3DES").orElseThrow(() -> new BlowfishEntityNotFoundException("KeyCryptographicType 3DES"));
            KeyUsageType keyUsageType = keyUsageTypeService.findByCode("ZMK").orElseThrow(() -> new BlowfishEntityNotFoundException("KeyUsageType ZMK"));
            String keyHex = importKey.getKeyUnderLmk();
            Key key = new Key.Builder()
                    .data(keyHex)
                    .checkDigit(importKey.getCheckDigit())
                    .keyCryptographicTypeId(keyCryptographicType.getKeyCryptographicTypeId())
                    .keyUsageTypeId(keyUsageType.getKeyUsageTypeId())
                    .build();
            return Optional.ofNullable(key);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return Optional.empty();

    }



    /***
     *
     * @param zmkKeyUnderLMK - ZMK Key under LMK
     * @param encodedPublicKeyHex - ASN1 Encoded Public key Hex
     * @throws Exception
     */
    public Optional<Key> exportZMKUnderLMKToUnderRsaKeyWithHSM(String zmkKeyUnderLMK, String encodedPublicKeyHex) {

        try{
            ExportToRsaKey exportToRsaKey = new ExportToRsaKey(zmkKeyUnderLMK, encodedPublicKeyHex, "0000");
            byte[] requestBytes = exportToRsaKey.pack();
            byte[] responseBytes = thalesHSMClient.sendReceive(requestBytes);
            exportToRsaKey.unpack(responseBytes);
            KeyCryptographicType keyCryptographicType = keyCryptographicTypeService.findByCode("RSA").orElseThrow(() -> new BlowfishEntityNotFoundException("KeyCryptographicType 3DES"));
            KeyUsageType keyUsageType = keyUsageTypeService.findByCode("ZMK").orElseThrow(() -> new BlowfishEntityNotFoundException("KeyUsageType ZMK"));
            String keyHex = exportToRsaKey.getKeyUnderPublicKey();
            Key key = new Key.Builder()
                    .data(keyHex)
                    .keyCryptographicTypeId(keyCryptographicType.getKeyCryptographicTypeId())
                    .keyUsageTypeId(keyUsageType.getKeyUsageTypeId())
                    .build();
            return Optional.ofNullable(key);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return Optional.empty();
    }


    /***
     * Encrypted Data using encrypted key under HSM LMK
     * @param keyUnderLMK
     * @param cipher
     * @return
     * @throws Exception
     */
    public Optional<String> encrypt3DESWithHSM(String keyUnderLMK, String cipher){
        try{
            Encrypt encrypt = new Encrypt(keyUnderLMK, cipher);
            byte[] requestBytes = encrypt.pack();
            byte[] responseBytes = thalesHSMClient.sendReceive(requestBytes);
            encrypt.unpack(responseBytes);
            return Optional.ofNullable(encrypt.getCryptogram());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return Optional.empty();
    }


    /**
     * Decrypts Data using encrypted key under HSM LMK
     * @param keyUnderLMK
     * @param cryptogram
     * @return
     * @throws Exception
     */
    public Optional<String> decrypt3DESWithHSM(String keyUnderLMK, String cryptogram) {
        try{
            Decrypt decrypt = new Decrypt(keyUnderLMK, cryptogram);
            byte[] requestBytes = decrypt.pack();
            byte[] responseBytes = thalesHSMClient.sendReceive(requestBytes);
            decrypt.unpack(responseBytes);
            return Optional.ofNullable(decrypt.getCipher());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return Optional.empty();
    }




    public Optional<String> exportZMKUnderLMKToUnderRsaKeyWithHSMInBase64(String zmkKeyUnderLMK, String encodedPublicKeyBase64) {
        PublicKey publicKey = null;
        try{
            byte[] decodedBytes = Base64.getDecoder().decode(encodedPublicKeyBase64);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return Optional.empty();
        }
        String publicKeyHex = getPublicKeyHex(publicKey);
        Key key = exportZMKUnderLMKToUnderRsaKeyWithHSM(zmkKeyUnderLMK, publicKeyHex).orElseThrow(() -> new BlowfishRuntimeException(ErrorType.ENCRYPT_ERROR.getCode(), "Unable to encrypt ctmk"));
        byte[] encryptedBytes = hex2bin(key.getData());
        return Optional.ofNullable(Base64.getEncoder().encodeToString(encryptedBytes));
    }


    public Optional<Key> importZMKUnderLMK(List<KeyComponent> keyComponentList){
        String clearKey = xorKeyComponents(keyComponentList).orElseThrow(() -> new BlowfishProcessingErrorException("Error creating key from components"));
        return importZMKUnderLMK(clearKey);
    }

    public Optional<Key> importZMKUnderLMK(String clearKey){
        KeyMap keyMap = keyMapService.findByCode("TMS_HSM_PUBLIC_KEY").orElseThrow(() -> new BlowfishEntityNotFoundException("TMS_HSM_PUBLIC_KEY Map"));
        Key key = keyService.findByKeyId(keyMap.getKeyId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Key " + keyMap.getKeyId()));
        //String keyData = key.getData();
        //int publicKeyLen = Integer.parseInt(keyData.substring(0, 4));
        //String publicKeyHex = keyData.substring(4, 4 + publicKeyLen);
        String publicKeyHex = key.getData();
        //int privateKeyLen = Integer.parseInt(keyData.substring(4 + publicKeyLen,  4 + publicKeyLen + 4));
        //String privateKeyHex = keyData.substring(publicKeyLen + 8, publicKeyLen + privateKeyLen + 8);
        String privateKeyHex = key.getValeUnderParent();
        String encryptedKey = encryptWithRSA(publicKeyHex, clearKey).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to encrypt key with RSA Public Key"));
        return importZMKKeyUnderRsaToUnderLMKWithHSM(encryptedKey, privateKeyHex);
    }


    /*
    public Optional<String> importKey(List<KeyComponent> keyComponentList, PublicKey publicKey){
        if(keyComponentList == null || keyComponentList.size() < 2)
            throw new BlowfishIllegalArgumentException("There must be at least 2 components");

        String component1 = keyComponentList.get(0).getComponent();
        String component2 = keyComponentList.get(1).getComponent();
        keyComponentList.remove(0);
        keyComponentList.remove(1);
        String clearKey = xorKeyComponents(component1, component2).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to create key from key components"));
        for(KeyComponent keyComponent: keyComponentList){
            clearKey = xorKeyComponents(clearKey, keyComponent.getComponent()).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to create key from key components"));
        }
        String importedKeyUnderHSMCertificate = importKeyUnderLMKWithHSM(clearKey, publicKey).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to export key under HSM certificate"));
        return Optional.ofNullable(importedKeyUnderHSMCertificate);
    }

    public Optional<String> importKey(String hexComponent1, String hexComponent2){
        String clearKey = xorKeyComponents(hexComponent1, hexComponent2).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to create key from key components"));
        String importedKeyUnderHSMCertificate = importKeyUnderLMKWithHSM(clearKey,null ).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to export key under HSM certificate"));
        return Optional.ofNullable(importedKeyUnderHSMCertificate);
    }


    private Optional<Key> importKeyUnderLMKWithHSM(String key, String encodedPublicKeyHex, String encryptedPrivateKeyUnderLMK) throws IOException{
         String encryptedKeyUnderPublicKey = encryptWithRSA(encodedPublicKeyHex, key).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to encrypt data"));
         return importZMKKeyUnderRsaToUnderLMKWithHSM(encryptedKeyUnderPublicKey, encryptedPrivateKeyUnderLMK);
    }
    */


    public Optional<KeyPair> generatePrivateKey(int keyLength){
        return  Optional.ofNullable(keyProvider.generateRSAKey(keyLength));
    }







    // ******************DES Functions **********

    public Optional<String> encryptWithDES(String publicKeyHex, String cipherHex){
        SecretKey secretKey = getSecretKeyFromHex(publicKeyHex).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to create Public Key"));
        byte[] cryptogramBytes = encryptWithDES(secretKey, hex2bin(cipherHex)).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to encrypt data"));
        return Optional.ofNullable(bin2hex(cryptogramBytes));
    }

    public Optional<String> encryptWithDES(SecretKey secretKey, String cipherHex){
        byte[] cryptogramBytes = encryptWithDES(secretKey, hex2bin(cipherHex)).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to encrypt data"));
        return Optional.ofNullable(bin2hex(cryptogramBytes));
    }

    public Optional<byte[]> encryptWithDES(String publicKeyHex, byte[] cipherBytes){
        SecretKey secretKey = getSecretKeyFromHex(publicKeyHex).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to create Public Key"));
        return encryptWithDES(secretKey, cipherBytes);
    }

    public Optional<byte[]> encryptWithDES(SecretKey secretKey, byte[] cipherBytes) {
        try{
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(cipherBytes);
            return Optional.ofNullable(encrypted);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return Optional.empty();
    }


    public Optional<String> decryptWithDES(String secretKeyHex, String crytogramHex){
        SecretKey secretKey = getSecretKeyFromHex(secretKeyHex).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to create Private Key"));
        byte[] cipherBytes = decryptWithDES(secretKey, hex2bin(crytogramHex)).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to decrypt data"));
        return Optional.ofNullable(bin2hex(cipherBytes));
    }

    public Optional<String> decryptWithDES(SecretKey secretKey, String crytogramHex){
        byte[] cipherBytes = decryptWithDES(secretKey, hex2bin(crytogramHex)).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to decrypt data"));
        return Optional.ofNullable(bin2hex(cipherBytes));
    }

    public Optional<byte[]> decryptWithDES(String secretKeyHex, byte[] cryptogramBytes){
        SecretKey secretKey = getSecretKeyFromHex(secretKeyHex).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to create Private Key"));
        return decryptWithDES(secretKey, cryptogramBytes);
    }

    public Optional<byte[]> decryptWithDES(SecretKey secretKey, byte[] cryptogramBytes) {

        try{
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cipherBytes = cipher.doFinal(cryptogramBytes);
            return Optional.ofNullable(cipherBytes);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return Optional.empty();
    }




    // ******************RSA Functions **********
    public Optional<String> encryptWithRSA(String publicKeyHex, String cipherHex){
        PublicKey publicKey = getPublicKeyFromHex(publicKeyHex).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to create Public Key"));
        byte[] cryptogramBytes = encryptWithRSA(publicKey, hex2bin(cipherHex)).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to encrypt data"));
        return Optional.ofNullable(bin2hex(cryptogramBytes));
    }

    public Optional<String> encryptWithRSA(PublicKey publicKey, String cipherHex){
        byte[] cryptogramBytes = encryptWithRSA(publicKey, hex2bin(cipherHex)).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to encrypt data"));
        return Optional.ofNullable(bin2hex(cryptogramBytes));
    }

    public Optional<byte[]> encryptWithRSA(String publicKeyHex, byte[] cipherBytes){
        PublicKey publicKey = getPublicKeyFromHex(publicKeyHex).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to create Public Key"));
        return encryptWithRSA(publicKey, cipherBytes);
    }

    public Optional<byte[]> encryptWithRSA(PublicKey publicKey, byte[] cipherBytes){
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE,  publicKey);
            byte[] cryptogramBytes = cipher.doFinal(cipherBytes);
            return Optional.ofNullable(cryptogramBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }



    public Optional<String> decryptWithRSA(String privateKeyHex, String crytogramHex){
        PrivateKey privateKey = getPrivateKeyFromHex(privateKeyHex).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to create Private Key"));
        byte[] cipherBytes = decryptWithRSA(privateKey, hex2bin(crytogramHex)).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to decrypt data"));
        return Optional.ofNullable(bin2hex(cipherBytes));
    }

    public Optional<String> decryptWithRSA(PrivateKey privateKey, String crytogramHex){
        byte[] cipherBytes = decryptWithRSA(privateKey, hex2bin(crytogramHex)).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to decrypt data"));
        return Optional.ofNullable(bin2hex(cipherBytes));
    }

    public Optional<byte[]> decryptWithRSA(String privateKeyHex, byte[] crytogramBytes){
        PrivateKey privateKey = getPrivateKeyFromHex(privateKeyHex).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to create Private Key"));
        return decryptWithRSA(privateKey, crytogramBytes);
    }

    public Optional<byte[]> decryptWithRSA(PrivateKey privateKey, byte[] crytogramBytes){
        try {
            Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c.init(Cipher.DECRYPT_MODE,  privateKey);
            byte[] cipherBytes = c.doFinal(crytogramBytes);
            return Optional.ofNullable(cipherBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    /*

    public static String encryptWithRSAReturnBase64String(String cipherText){

        byte[] encodedBytes = null;
        try {
            PublicKey publicKey = getPublicKey();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE,  publicKey);
            encodedBytes = cipher.doFinal(cipherText.getBytes());
            String base64EncryptedText = new String(Base64.encode(encodedBytes, Base64.NO_WRAP));
            return base64EncryptedText;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
     */



    public Optional<String> xorKeyComponents(List<KeyComponent> keyComponentList){
        if(keyComponentList == null || keyComponentList.size() < 2)
            throw new BlowfishIllegalArgumentException("There must be at least 2 components");

        String component1 = keyComponentList.get(0).getComponent();
        String component2 = keyComponentList.get(1).getComponent();
        keyComponentList.remove(0);
        keyComponentList.remove(0);
        String clearKey = xorKeyComponents(component1, component2).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to create key from key components"));
        for(KeyComponent keyComponent: keyComponentList){
            clearKey = xorKeyComponents(clearKey, keyComponent.getComponent()).orElseThrow(() -> new BlowfishProcessingErrorException("Unable to create key from key components"));
        }
        return Optional.ofNullable(clearKey);
    }


    private Optional<String> xorKeyComponents(String hexComponent1, String hexComponent2){
        if (hexComponent1==null || hexComponent2==null || hexComponent1.isEmpty()|| hexComponent2.isEmpty())
            throw new BlowfishIllegalArgumentException("Component 1 or 2 cannot be empty");
        if (hexComponent1.length() != hexComponent2.length())
            throw new BlowfishIllegalArgumentException("Component 1 and 2 must be of same length");

        char[] chars = new char[hexComponent1.length()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = toHex(fromHex(hexComponent1.charAt(i)) ^ fromHex(hexComponent2.charAt(i)));
        }
        return Optional.ofNullable(new String(chars));
    }


    public Optional<SecretKey> getSecretKeyFromHex(String secretKeyHex){
        try{
            byte[] tmp = hex2bin(secretKeyHex);
            byte[] keyBytes = new byte[24];
            System.arraycopy(tmp, 0, keyBytes, 0, 16);
            System.arraycopy(tmp, 0, keyBytes, 16, 8);
            SecretKey secretKey = new SecretKeySpec(keyBytes, "DESede");;
            return Optional.ofNullable(secretKey);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<PublicKey> getPublicFromCert(String file){
        try{
            FileInputStream fin = new FileInputStream(file);
            CertificateFactory f = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
            PublicKey pk = certificate.getPublicKey();
            return Optional.ofNullable(pk);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public String getPrivateKeyHex(PrivateKey privateKey){
        try{
            RSAPrivateCrtKey rsaPrivateKey = (RSAPrivateCrtKey) privateKey;
            byte[] encodedPrivatekey = new RSAPrivateKeyStructure(rsaPrivateKey.getModulus(), rsaPrivateKey.getPublicExponent(), rsaPrivateKey.getPrivateExponent(), rsaPrivateKey.getPrimeP(), rsaPrivateKey.getPrimeQ(), rsaPrivateKey.getPrimeExponentP(), rsaPrivateKey.getPrimeExponentQ(), rsaPrivateKey.getCrtCoefficient()).getEncoded("DER");
            //byte[] encodedPrivatekey = privateKey.getEncoded();
            String hex = bin2hex(encodedPrivatekey);
            return hex;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public String getPublicKeyHex(PublicKey publicKey){
        try{
            RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
            byte[] encodedPublickey = new RSAPublicKeyStructure(rsaPublicKey.getModulus(), rsaPublicKey.getPublicExponent()).getEncoded("DER");
            //byte[] encodedPublickey = publicKey.getEncoded();
            String hex = bin2hex(encodedPublickey);
            return hex;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


    public Optional<PrivateKey> getPrivateKeyFromHex(String privateKeyHex){
        try{
            ASN1Sequence sequence = ASN1Sequence.getInstance(hex2bin(privateKeyHex));
            ASN1Integer modulus = (ASN1Integer) ASN1Integer.getInstance(sequence.getObjectAt(1));
            ASN1Integer publicExpo = (ASN1Integer) ASN1Integer.getInstance(sequence.getObjectAt(2));
            ASN1Integer privateExpo = (ASN1Integer) ASN1Integer.getInstance(sequence.getObjectAt(3));
            ASN1Integer prime1 = (ASN1Integer) ASN1Integer.getInstance(sequence.getObjectAt(4));
            ASN1Integer prime2 = (ASN1Integer) ASN1Integer.getInstance(sequence.getObjectAt(5));
            ASN1Integer primeExpo1 = (ASN1Integer) ASN1Integer.getInstance(sequence.getObjectAt(6));
            ASN1Integer primeExpo2 = (ASN1Integer) ASN1Integer.getInstance(sequence.getObjectAt(7));
            ASN1Integer coeff = (ASN1Integer) ASN1Integer.getInstance(sequence.getObjectAt(8));
            RSAPrivateKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus.getPositiveValue(), publicExpo.getPositiveValue(), privateExpo.getPositiveValue(), prime1.getPositiveValue(), prime2.getPositiveValue(), primeExpo1.getPositiveValue(), primeExpo2.getPositiveValue(), coeff.getPositiveValue());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateCrtKey privateKey = (RSAPrivateCrtKey) keyFactory.generatePrivate(keySpec);
            return Optional.ofNullable(privateKey);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<PublicKey> getPublicKeyFromHex(String publicKeyHex){
        try{
            ASN1Sequence sequence = ASN1Sequence.getInstance(hex2bin(publicKeyHex));
            ASN1Integer modulus = (ASN1Integer) ASN1Integer.getInstance(sequence.getObjectAt(0));
            ASN1Integer exponent = (ASN1Integer) ASN1Integer.getInstance(sequence.getObjectAt(1));
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus.getPositiveValue(), exponent.getPositiveValue());
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = factory.generatePublic(keySpec);
            return  Optional.ofNullable(publicKey);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return Optional.empty();
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

    private int fromHex(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        throw new IllegalArgumentException();
    }

    private char toHex(int nybble) {
        if (nybble < 0 || nybble > 15) {
            throw new IllegalArgumentException();
        }
        return "0123456789ABCDEF".charAt(nybble);
    }

}
