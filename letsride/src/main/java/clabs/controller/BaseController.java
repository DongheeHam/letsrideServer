package clabs.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class BaseController {

	private final static Logger logger = Logger.getLogger(BaseController.class.getName());
	
	protected @Autowired ServletContext context; 

	protected final static String GSuiteAccessMode = "GSuite-AccessMode";
	//protected final static String GSPAccountInfo = "_GSVPM-UserInfo_";
//	protected final static String GSPCBParam = "_GSVPM-CBParm_"; 
	
	protected final static String siteAdminIps="1.255.4.115,1.255.4.116,223.38.17.86";
	
	protected boolean checkSysAdmin(HttpServletRequest request) {
		logger.info("remoteAddr"+request.getRemoteAddr());
		return true;
		/*if(checkAdminIP(request.getRemoteAddr())) return true;
		String token = (String) request.getSession().getAttribute(GSuiteAccessMode);
		return (token != null && "__YES__".equals(token));*/
	}
	
	private boolean checkAdminIP(String rip) {
		String[] ips = siteAdminIps.split(",");
		for(String ip : ips) if(ip.equals(rip)) return true;
		return false;
	}
}
