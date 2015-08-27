package com.lfantastico.web.pay;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.util.encoders.Base64;

public class PayPalCrypt {
    private PrivateKey ownKey;
    private X509Certificate ownCert;
    private X509Certificate paypalCert;

    public PayPalCrypt(PrivateKey myKey, X509Certificate myCert,
            X509Certificate ppCert) {
        this.ownKey = myKey;
        this.ownCert = myCert;
        this.paypalCert = ppCert;
    }

    public String encrypt(byte[] data)
            throws IOException, GeneralSecurityException {
        try {
            CMSSignedDataGenerator sg = new CMSSignedDataGenerator();
            sg.addSigner(ownKey, ownCert, CMSSignedDataGenerator.DIGEST_SHA1);
            ArrayList<X509Certificate> certs = new ArrayList<X509Certificate>();
            certs.add(ownCert);
            CertStore certStore = CertStore.getInstance("Collection",
                    new CollectionCertStoreParameters(certs));
            sg.addCertificatesAndCRLs(certStore);
            CMSProcessableByteArray cmsba = new CMSProcessableByteArray(data);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            cmsba.write(baos);
            CMSSignedData signedData = sg.generate(cmsba, true, "BC");
            byte[] signed = signedData.getEncoded();
            CMSEnvelopedDataGenerator eg = new CMSEnvelopedDataGenerator();
            eg.addKeyTransRecipient(paypalCert);
            CMSEnvelopedData envData = eg.generate(
                    new CMSProcessableByteArray(signed),
                    CMSEnvelopedDataGenerator.DES_EDE3_CBC, "BC");
            byte[] bytes = envData.getEncoded();
            String encoded = new String(Base64.encode(bytes), "ASCII");
            return "-----BEGIN PKCS7-----" + encoded + "-----END PKCS7-----";
        } catch (CMSException e) {
            throw new GeneralSecurityException(e.getMessage());
        }
    }
}
