/*package clabs.srv.vo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class GSuiteLink {

	public static String idpMetafile;
	public static String serviceDomain, serviceName, baseURL;
	
	public Properties props;

	public GSuiteLink(String config) {

		props = new Properties();
        try {
        	props.load(new InputStreamReader(new FileInputStream(config)));
 
        	serviceName = props.getProperty("saml.serviceName", "시소아이티");
        	serviceDomain = props.getProperty("saml.serviceDomain", "sysoit.com");
        	baseURL = props.getProperty("saml.baseURL", "https://gsl." + serviceDomain);
            idpMetafile = props.getProperty("saml.idpMeta", "GoogleIDPMetadata.xml");
            
        } catch (IOException e) {
            System.out.println("GSuiteLink Error:" + e.getMessage());
        }
	}
}
*/