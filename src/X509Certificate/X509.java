package X509Certificate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;

import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class X509 {

    public static void main(String[] args) {
        FileInputStream is;
        try {
            is = new FileInputStream("E:/lenovo-work/work/microsoft/soa-testlenovocom.crt");
            CertificateFactory x509CertFact = CertificateFactory.getInstance("X.509");
           
            X509Certificate cert = (X509Certificate)x509CertFact.generateCertificate(is);
            String thumbprint = getThumbPrint(cert);
            System.out.println( thumbprint);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public static String getThumbPrint(X509Certificate cert) 
        throws NoSuchAlgorithmException, CertificateEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] der = cert.getEncoded();
        md.update(der);
        byte[] digest = md.digest();
        return hexify(digest);

    }

    public static String hexify (byte bytes[]) {

        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', 
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        StringBuffer buf = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; ++i) {
            buf.append(hexDigits[(bytes[i] & 0xf0) >> 4]);
            buf.append(hexDigits[bytes[i] & 0x0f]);
        }

        return buf.toString();
    }

}