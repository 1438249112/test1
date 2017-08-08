package X509Certificate;


import java.io.*;
import java.util.*;
import java.security.*;
import java.security.cert.*;
import sun.security.x509.*;
import java.security.cert.Certificate;

import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.opensaml.xml.signature.impl.X509CertificateBuilder;

public class Snippet {
	public static void main(String[] args) throws Exception {
		X509v3CertificateBuilder
		 CertificateFactory cf=CertificateFactory.getInstance("X.509");
		    FileInputStream in=new FileInputStream("E:/lenovo-work/work/microsoft/soa-testlenovocom.crt");
		    X509Certificate c=(X509Certificate) cf.generateCertificate(in);

		    
		    
		    System.out.println(c.);
	}
	
}

