package com.avantir.blowfish.services;

import com.avantir.blowfish.clients.rest.HsmClient;
import com.avantir.blowfish.entity.Key;
import com.avantir.blowfish.entity.KeyMap;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.exceptions.BlowfishIllegalArgumentException;
import com.avantir.blowfish.exceptions.BlowfishProcessingErrorException;
import com.avantir.blowfish.model.KeyComponent;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import sun.security.rsa.RSAPublicKeyImpl;
import sun.security.x509.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;
import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.Certificate;
import java.security.MessageDigest;
import java.security.cert.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * Created by lekanomotayo on 27/03/2018.
 */

@Configuration
@Service
public class SecurityService {

    @Autowired
    KeyManagementService keyManagementService;



    public boolean validCheckDigit(String key, String checkDigit){
        String verifyCheckDigit = keyManagementService.encryptWithDES(key, "0000000000000000").orElseThrow(() -> new BlowfishProcessingErrorException("Could not encrypt with this key"));
        return verifyCheckDigit.contains(checkDigit);
    }


    public Optional<String> getHash(String data, String salt, String algo){
        try {
            MessageDigest md = MessageDigest.getInstance(getAlgoName(algo).orElseThrow(() -> new BlowfishIllegalArgumentException("Algo")));
            md.update(salt.getBytes("UTF-8"));
            byte[] bytes = md.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            return Optional.ofNullable(sb.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Optional.empty();
    }


    private Optional<String> getAlgoName(String algo){
        if(algo == null)
            return Optional.empty();

        if(algo.indexOf("SHA") > 0 && algo.indexOf("512") > 0)
            return Optional.ofNullable("SHA-512");

        return Optional.empty();
    }




    public Optional<String> maskPan(String strText) {
        return maskPan(strText, 6, 4, '*');
    }

    public Optional<String> maskPan(String strText, char maskChar) {
        return maskPan(strText, 6, 4, maskChar);
    }


    public Optional<String> maskPan(String strText, int start, int end, char maskChar) {
        if(strText == null || strText.equals(""))
            return Optional.empty();
        start = start < 0 ? 0 : start;
        end = end > strText.length() ? end = strText.length() : end;
        int maskLength = strText.length() - end - start;
        if(maskLength <= 0)
            return Optional.ofNullable(strText);
        StringBuilder sbMaskString = new StringBuilder();
        for(int i = 0; i < maskLength; i++)
            sbMaskString.append(maskChar);
        return Optional.ofNullable(strText.substring(0, start)
                + sbMaskString.toString()
                + strText.substring(start + maskLength));
    }



    public boolean validatePan(String pan) {
        if(pan == null)
            return false;

        try{
            int sum = 0;
            boolean alternate = false;
            for (int i = pan.length() - 1; i >= 0; i--) {
                int n = Integer.parseInt(pan.substring(i, i + 1));
                if (alternate) {
                    n *= 2;
                    if (n > 9) {
                        n = (n % 10) + 1;
                    }
                }
                sum += n;
                alternate = !alternate;
            }
            return (sum % 10 == 0);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }



    public boolean validateExpDate(String expDate){
        if(expDate == null)
            return false;
        try{
            String expYear = expDate.substring(0, 2);
            int expMonth = Integer.parseInt(expDate.substring(2, 4));
            Calendar cal = Calendar.getInstance();
            int thisMonth = cal.get(Calendar.MONTH) + 1;
            int thisYear = cal.get(Calendar.YEAR);
            String thisYearStrPrefix = String.valueOf(thisYear).substring(0, 2);
            String fullExpYear = thisYearStrPrefix + expYear;
            if(Integer.parseInt(fullExpYear) > thisYear)
                return true;

            if(Integer.parseInt(fullExpYear) == thisYear && thisMonth < expMonth)
                return true;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }


}
