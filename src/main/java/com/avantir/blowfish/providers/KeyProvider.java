package com.avantir.blowfish.providers;

import org.springframework.stereotype.Service;
import sun.security.x509.*;

import javax.xml.bind.DatatypeConverter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Optional;

/**
 * Created by lekanomotayo on 21/04/2018.
 */

@Service
public class KeyProvider {

    public KeyPair generateRSAKey(int keylength){
        try{
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keylength);
            return keyPairGenerator.genKeyPair();
        }
        catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }


    public void writeCertToFile(String file, X509Certificate cert){
        FileWriter fw = null;
        try{
            fw = new FileWriter(file);
            try {
                fw.write("-----BEGIN CERTIFICATE-----\n");
                fw.write(DatatypeConverter.printBase64Binary(cert.getEncoded()).replaceAll("(.{64})", "$1\n"));
                fw.write("\n-----END CERTIFICATE-----\n");
            } catch (CertificateEncodingException e) {
                e.printStackTrace();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally {
            try {
                if (fw != null)
                    fw.close();
            }
            catch (IOException ex){}
        }
    }



    /**
     * Create a self-signed X.509 Certificate
     * @param dn the X.509 Distinguished Name, eg "CN=Arca Payment, L=Lagos, C=NG"
     * @param pair the KeyPair
     * @param days how many days from now the Certificate is valid for
     * @param algorithm the signing algorithm, eg "SHA1withRSA"
     */
    public Optional<X509Certificate> generateCertificate(String dn, KeyPair pair, int days, String algorithm) {

        try{
            PrivateKey privkey = pair.getPrivate();
            X509CertInfo info = new X509CertInfo();
            Date from = new Date();
            Date to = new Date(from.getTime() + days * 86400000l);
            CertificateValidity interval = new CertificateValidity(from, to);
            BigInteger sn = new BigInteger(64, new SecureRandom());
            X500Name owner = new X500Name(dn);

            info.set(X509CertInfo.VALIDITY, interval);
            info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
            info.set(X509CertInfo.SUBJECT, owner);
            info.set(X509CertInfo.ISSUER, owner);
            info.set(X509CertInfo.KEY, new CertificateX509Key(pair.getPublic()));
            info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
            AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
            info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));

            // Sign the cert to identify the algorithm that's used.
            X509CertImpl cert = new X509CertImpl(info);
            cert.sign(privkey, algorithm);

            // Update the algorith, and resign.
            algo = (AlgorithmId) cert.get(X509CertImpl.SIG_ALG);
            info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algo);
            cert = new X509CertImpl(info);
            cert.sign(privkey, algorithm);
            return Optional.ofNullable(cert);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return Optional.empty();
    }



}
