/*package clabs.srv.manager;


import org.apache.log4j.Logger;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;

import clabs.srv.vo.GSuiteLink;

@Configuration
@ImportResource({"classpath*:applicationContext.xml"})
public class GSApplication implements EnvironmentAware {

	private final static Logger logger = Logger.getLogger(GSApplication.class.getName());
	private static String configFile = "/gsvp.properties";
	public static String home;
	
	Environment env;
	
    @Bean
    public GSuiteLink gsuiteLink() {
    	home = env.getProperty("GSL_HOME");
    	return new GSuiteLink(home + configFile);
    }

	@Override
	public void setEnvironment(Environment environment) {
		env = environment;
	}
    
}
*/