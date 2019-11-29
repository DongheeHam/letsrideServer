package clabs.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PreDestroy;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import clabs.srv.service.AcmeService;
import clabs.tools.ResObject;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.shredzone.acme4j.Account;
import org.shredzone.acme4j.AccountBuilder;
import org.shredzone.acme4j.Authorization;
import org.shredzone.acme4j.Certificate;
import org.shredzone.acme4j.Login;
import org.shredzone.acme4j.Order;
import org.shredzone.acme4j.Session;
import org.shredzone.acme4j.Status;
import org.shredzone.acme4j.challenge.Challenge;
import org.shredzone.acme4j.challenge.Dns01Challenge;
import org.shredzone.acme4j.challenge.Http01Challenge;
import org.shredzone.acme4j.exception.AcmeException;
import org.shredzone.acme4j.toolbox.AcmeUtils;
import org.shredzone.acme4j.util.CSRBuilder;
import org.shredzone.acme4j.util.KeyPairUtils;

/*
 * GSuiteLink.siteKeystore : /root/keystore
 * ---------------------------------------------------------------------------------------------------------------------
      	<Connector port="443" protocol="org.apache.coyote.http11.Http11AprProtocol" maxThreads="150" SSLEnabled="true" >
	        <UpgradeProtocol className="org.apache.coyote.http2.Http2Protocol" />
	        <SSLHostConfig>
	            <Certificate certificateKeyFile="/root/keystore/domain.key"
	                         certificateFile="/root/keystore/domain.crt"
	                         certificateChainFile="/root/keystore/domain-chain.crt"
	                         type="RSA" />
	        </SSLHostConfig>
    	</Connector>
 * ---------------------------------------------------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/acme")
public class AcmeController extends BaseController {
    
	private final static Logger logger = Logger.getLogger(AcmeController.class.getName());
	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	
	private final static String acmeHosts="letsride.donghee.site";
	private final static String webRoot="/root/webletsride/";
	private final static String siteKeystore="/opt/tomcat/latest/conf";
	
	
	@Autowired AcmeService acmeService;
	
	// File name of the User Key Pair
    private static final File USER_KEY_FILE = new File(siteKeystore + "/user.key");
    private static final File DOMAIN_KEY_FILE = new File(siteKeystore + "/domain.key");
    private static final File DOMAIN_CSR_FILE = new File(siteKeystore + "/domain.csr");
    private static final File DOMAIN_CERT_FILE = new File(siteKeystore + "/domain.crt");
    private static final File DOMAIN_CHAIN_FILE = new File(siteKeystore + "/domain-chain.crt");
    
    private static final ChallengeType CHALLENGE_TYPE = ChallengeType.HTTP;
    //private static final ChallengeType CHALLENGE_TYPE = ChallengeType.DNS;

    private static final int KEY_SIZE = 2048;
    private enum ChallengeType { HTTP, DNS }
    
    ExecutorService msgMaker = null;
    
    @PreDestroy
    public void stop() {
    	if(msgMaker != null) msgMaker.shutdown();
    }
    
	@Scheduled(cron = "0 0 12 * * SUN")	// 일요일 정오에 수행 
	public void doOnWeekSchedule() {
		
		try {
			CertificateFactory fact = CertificateFactory.getInstance("X.509");
			FileInputStream is = new FileInputStream(DOMAIN_CERT_FILE);
			X509Certificate cert = (X509Certificate) fact.generateCertificate(is);
			   
			Date certNotAfter = cert.getNotAfter();
			Date now = new Date();
			long timeLeft = certNotAfter.getTime() - now.getTime(); // Time left in ms
			if (timeLeft < 7l * 24 * 3600 * 1000) {
				(new TimerTask() {

					@Override
					public void run() {
						try {
							fetchCertificate();
						} catch (IOException e) {
							logger.info(e.getMessage());
						}
					}
					
				}).run();
			}
			else {
				logger.info("cert validate: " + simpleDateFormat.format(certNotAfter) + " Left " + String.format("%d Days", timeLeft/(24 * 3600 * 1000)));
			}

		} catch (IOException | CertificateException e) {
			logger.error("Can't Find CER File:" + e.getMessage());
		}
	}
	
	@RequestMapping(value="/testAccess.json", produces="application/json;charset=UTF-8")
	public @ResponseBody ResObject testAccess(HttpServletRequest request, @RequestParam Map<String, String> params) throws IOException{
		
		if(!checkSysAdmin(request)) return new ResObject(0, "What are you doing here!!, It's System Admin Mode!!", null);		
		return new ResObject(0, "OK", null);
	}
	
	@RequestMapping(value="/testMail.json", produces="application/json;charset=UTF-8")
	public @ResponseBody ResObject testMail(HttpServletRequest request, @RequestParam Map<String, String> params) throws IOException{
		
		if(!checkSysAdmin(request)) return new ResObject(0, "What are you doing here!!, It's System Admin Mode!!", null);
		
		acmeService.sendMail(acmeService.accessUser, "AcmeManager", "[ACME] DNS TXT Record Request", 
        		String.format("<html><h2>%s</h2><p>_acme-challenge.%s. IN TXT %s</p><br><br><p>호스트: _acme-challenge.%s<br>텍스트: %s</p></html>", 
			        		"아래와 같이 DNS서비스에 TXT레코드를 등록해 주세요",
			        		"gsl.sample.domain", "samplechallengeTokens",
			        		acmeHosts.split("\\.")[0], "sample-digest"));
		
		return new ResObject(0, "OK", null);
	}
	
	private boolean isMailReport = true;
    /*
     * 2.7버전에서는
     * 초기 생성루틴과 리뉴의 루틴이 같다.
     * 키파일 기준이기때문에 초기키파일만 유지되면 지속적으로 리뉴프로세스가 진행됨.
     * 따라서, 초기에 한 번한 챌린지가 적용됨.
     */
	@RequestMapping(value="/fetchCertificate.json", produces="application/json;charset=UTF-8")
	public @ResponseBody ResObject fetchCertificate1(HttpServletRequest request, @RequestParam Map<String, String> params) throws IOException{
		
		if(!checkSysAdmin(request)) return new ResObject(0, "What are you doing here!!, It's System Admin Mode!!", null);
		
		if(params.get("nomail") != null) isMailReport = false;
		
		(new TimerTask() {

			@Override
			public void run() {
				try {
					fetchCertificate();
				} catch (IOException e) {
					logger.info(e.getMessage());
				}
			}
			
		}).run();
		
		return new ResObject(0, "OK", null);
	}
	
	private void fetchCertificate() throws IOException {
		
        Security.addProvider(new BouncyCastleProvider());
        String[] hosts = acmeHosts.split(",");
        Collection<String> domains = Arrays.asList(hosts);
//    	Collection<String> domains = Arrays.asList(GSuiteLink.gslHost);
        KeyPair userKeyPair = loadOrCreateUserKeyPair();
        Session session = new Session("acme://letsencrypt.org");

        // Get the Account.
        // If there is no account yet, create a new one.
        Account acct;
		try {
			acct = findOrRegisterAccount(session, userKeyPair);
	        
	        KeyPair domainKeyPair = loadOrCreateDomainKeyPair();
	        Order order = acct.newOrder().domains(domains).create();
	        for (Authorization auth : order.getAuthorizations()) {
	            authorize(auth);
	        }

	        CSRBuilder csrb = new CSRBuilder();
	        csrb.addDomains(domains);
	        csrb.sign(domainKeyPair);

	        try (Writer out = new FileWriter(DOMAIN_CSR_FILE)) {
	            csrb.write(out);
	        }
	        
	        order.execute(csrb.getEncoded());

	        // Wait for the order to complete
	        try {
	            int attempts = 10;
	            while (order.getStatus() != Status.VALID && attempts-- > 0) {
	                // Did the order fail?
	                if (order.getStatus() == Status.INVALID) {
	                    throw new AcmeException("Order failed... Giving up.-" + order.getError());
	                }

	                // Wait for a few seconds
	                Thread.sleep(5000L);

	                // Then update the status
	                order.update();
	            }
	        } catch (InterruptedException ex) {
	            logger.error("interrupted", ex);
	        }

	        // Get the certificate
	        Certificate certificate = order.getCertificate();
	        logger.info("Success! The certificate for domains {} has been generated! : [" +  domains + "]");

	        /*
	         * 2.7버전의 기본은 전체 인증서를 하나로 만들지만,
	         * 톰켓에서 설정을 용이하게하기위해 아래와 같이 수정 함.
	         * -----------------------------------------------------------------------------
	        // Write a combined file containing the certificate and chain.
	        try (FileWriter fw = new FileWriter(DOMAIN_CHAIN_FILE)) {
	            certificate.writeCertificate(fw);
	        }
	        */
	        try {
	        	List<X509Certificate> certs = certificate.getCertificateChain();
	        	FileWriter cf = new FileWriter(DOMAIN_CERT_FILE);
	        	FileWriter crf = new FileWriter(DOMAIN_CHAIN_FILE);
	            for (int i=0; i<certs.size(); i++) {
	            	X509Certificate cert = certs.get(i);
	            	if( i == 0 ) AcmeUtils.writeToPem(cert.getEncoded(), AcmeUtils.PemLabel.CERTIFICATE, cf);
	            	else  AcmeUtils.writeToPem(cert.getEncoded(), AcmeUtils.PemLabel.CERTIFICATE, crf);
	            }
	            crf.close(); cf.close();
	        } catch (CertificateEncodingException ex) {
	            logger.error(ex.getMessage());
	        }

		} catch (AcmeException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
     * Loads a user key pair from {@value #USER_KEY_FILE}. If the file does not exist,
     * a new key pair is generated and saved.
     * <p>
     * Keep this key pair in a safe place! In a production environment, you will not be
     * able to access your account again if you should lose the key pair.
     *
     * @return User's {@link KeyPair}.
     */
    private KeyPair loadOrCreateUserKeyPair() throws IOException {
    	
        if (USER_KEY_FILE.exists()) {
            // If there is a key file, read it
            try (FileReader fr = new FileReader(USER_KEY_FILE)) {
                return KeyPairUtils.readKeyPair(fr);
            }

        } else {
            // If there is none, create a new key pair and save it
            KeyPair userKeyPair = KeyPairUtils.createKeyPair(KEY_SIZE);
            try (FileWriter fw = new FileWriter(USER_KEY_FILE)) {
                KeyPairUtils.writeKeyPair(userKeyPair, fw);
            }
            return userKeyPair;
        }
    }

    /**
     * Loads a domain key pair from {@value #DOMAIN_KEY_FILE}. If the file does not exist,
     * a new key pair is generated and saved.
     *
     * @return Domain {@link KeyPair}.
     */
    private KeyPair loadOrCreateDomainKeyPair() throws IOException {
        if (DOMAIN_KEY_FILE.exists()) {
            try (FileReader fr = new FileReader(DOMAIN_KEY_FILE)) {
                return KeyPairUtils.readKeyPair(fr);
            }
        } else {
            KeyPair domainKeyPair = KeyPairUtils.createKeyPair(KEY_SIZE);
            try (FileWriter fw = new FileWriter(DOMAIN_KEY_FILE)) {
                KeyPairUtils.writeKeyPair(domainKeyPair, fw);
            }
            return domainKeyPair;
        }
    }

    /**
     * Finds your {@link Account} at the ACME server. It will be found by your user's
     * public key. If your key is not known to the server yet, a new account will be
     * created.
     * <p>
     * This is a simple way of finding your {@link Account}. A better way is to get the
     * URL and KeyIdentifier of your new account with {@link Account#getLocation()}
     * {@link Session#getKeyIdentifier()} and store it somewhere. If you need to get
     * access to your account later, reconnect to it via
     * {@link Account#bind(Session, URI)} by using the stored location.
     *
     * @param session
     *            {@link Session} to bind with
     * @return {@link Login} that is connected to your account
     */
    private Account findOrRegisterAccount(Session session, KeyPair accountKey) throws AcmeException {

        Account account = new AccountBuilder().agreeToTermsOfService().useKeyPair(accountKey).create(session);
        logger.info("Registered a new user, URL: {}" + account.getLocation());

        return account;
    }

 
    private void authorize(Authorization auth) throws AcmeException {
        logger.info("Authorization for domain [" + auth.getIdentifier().getDomain() + "]");

        // The authorization is already valid. No need to process a challenge.
        if (auth.getStatus() == Status.VALID) {
            return;
        }

        // Find the desired challenge and prepare it.
        Challenge challenge = null;
        switch (CHALLENGE_TYPE) {
            case HTTP:
                challenge = httpChallenge(auth);
                break;

            case DNS:
                challenge = dnsChallenge(auth);
                break;
        }

        if (challenge == null) {
            throw new AcmeException("No challenge found");
        }
        logger.info("33333333333333333333333333");
        // If the challenge is already verified, there's no need to execute it again.
        if (challenge.getStatus() == Status.VALID) {
            return;
        }
        logger.info("44444444444444444444");
        // Now trigger the challenge.
        challenge.trigger();

        // Poll for the challenge to complete.
        try {
        	logger.info("55555555555555555555");
            int attempts = 10;
            while (challenge.getStatus() != Status.VALID && attempts-- > 0) {
                // Did the authorization fail?
            	System.out.println(">>" + challenge.getStatus() + ":" + challenge.getError());
            	logger.info("66666666666666666666");
                if (challenge.getStatus() == Status.INVALID) {
                    throw new AcmeException("Challenge failed... Giving up. [" + auth.getIdentifier().getDomain() + "]");
                }
                logger.info("77777777777777777777777");
                // Wait for a few seconds
                Thread.sleep(5000L);

                // Then update the status
                challenge.update();
            }
            logger.info("44444444444444444444444444");
        } catch (InterruptedException ex) {
            logger.error("interrupted", ex);
            Thread.currentThread().interrupt();
        }

        // All reattempts are used up and there is still no valid authorization?
        if (challenge.getStatus() != Status.VALID) {
            throw new AcmeException("Failed to pass the challenge for domain [" + auth.getIdentifier().getDomain() + "], ... Giving up.");
        }
        logger.info("Challenge has been completed. Remember to remove the validation resource.");  
        if(CHALLENGE_TYPE == ChallengeType.HTTP) {
        	File dir = new File(String.format("%s/.well-known/acme-challenge", webRoot));
        	deleteDirectory(dir);
        }
    }    

    /**
     * Prepares a HTTP challenge.
     * <p>
     * The verification of this challenge expects a file with a certain content to be
     * reachable at a given path under the domain to be tested.
     * <p>
     * This example outputs instructions that need to be executed manually. In a
     * production environment, you would rather generate this file automatically, or maybe
     * use a servlet that returns {@link Http01Challenge#getAuthorization()}.
     *
     * @param auth
     *            {@link Authorization} to find the challenge in
     * @return {@link Challenge} to verify
     */
    public Challenge httpChallenge(Authorization auth) throws AcmeException {
        // Find a single http-01 challenge
        Http01Challenge challenge = auth.findChallenge(Http01Challenge.TYPE);
        if (challenge == null) {
            throw new AcmeException("Found no " + Http01Challenge.TYPE + " challenge, don't know what to do...");
        }

        /*
        // Output the challenge, wait for acknowledge...
        logger.info("Please create a file in your web server's base directory.");
        logger.info(String.format("It must be reachable at: http://%s/.well-known/acme-challenge/%s",
                    auth.getIdentifier().getDomain(), challenge.getToken()));
        logger.info("File name: " + challenge.getToken());
        logger.info("Content: " + challenge.getAuthorization());
        logger.info("The file must not contain any leading or trailing whitespaces or line breaks!");
        logger.info("If you're ready, dismiss the dialog...");
        */

        StringBuilder message = new StringBuilder();
        message.append("Create a file in your web server's base directory.\n\n");
        message.append("http://")
                    .append(auth.getIdentifier().getDomain())
                    .append("/.well-known/acme-challenge/")
                    .append(challenge.getToken())
                    .append("\n\n");
        message.append("Content:\n\n");
        message.append(challenge.getAuthorization());
        logger.info(message.toString());

        File dir = new File(String.format("%s/.well-known/acme-challenge", webRoot));
//        File dir = new File(String.format("%s/.well-known/acme-challenge", gsuiteLink.props.getProperty("acme."+auth.getIdentifier().getDomain()+".root")));
        dir.mkdirs();
        logger.info("11111111111111111111111");
        File f = new File(dir, challenge.getToken());
        try {
            //파일에 문자열을 쓴다.
            //하지만 이미 파일이 존재하면 모든 내용을 삭제하고 그위에 덮어쓴다
            //파일이 손산될 우려가 있다.
            FileWriter fw = new FileWriter(f);
            fw.write(challenge.getAuthorization());
            fw.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        
        logger.info("222222222222222222222222");
        return challenge;
    }
 

    /**
     * Prepares a DNS challenge.
     * <p>
     * The verification of this challenge expects a TXT record with a certain content.
     * <p>
     * This example outputs instructions that need to be executed manually. In a
     * production environment, you would rather configure your DNS automatically.
     *
     * @param auth
     *            {@link Authorization} to find the challenge in
     * @return {@link Challenge} to verify
     */
    public Challenge dnsChallenge(Authorization auth) throws AcmeException {
    	
        // Find a single dns-01 challenge
        Dns01Challenge challenge = auth.findChallenge(Dns01Challenge.TYPE);
        if (challenge == null) {
            throw new AcmeException("Found no " + Dns01Challenge.TYPE + " challenge, don't know what to do...");
        }

        // Output the challenge, wait for acknowledge...
        logger.info("Please create a TXT record:");
        logger.info("_acme-challenge.{}. IN TXT {}" + auth.getIdentifier().getDomain() + "," + challenge.getDigest());

        StringBuilder message = new StringBuilder();
        message.append("Please create a TXT record:\n\n");
        message.append("_acme-challenge.").append(auth.getIdentifier().getDomain()).append(". IN TXT ").append(challenge.getDigest());
        logger.info("acme challenge request :" + message.toString());
        
        if(isMailReport) {
            // 메일먼저 보내고...
        	acmeService.sendMail(acmeService.accessUser, "AcmeManager", "[지스위트] DNS TXT Record Request", 
            		String.format("<html><h2>%s</h2><p>_acme-challenge.%s. IN TXT %s</p><br><br><p>호스트: _acme-challenge.%s<br>텍스트: %s</p></html>", 
    			        		"아래와 같이 DNS서비스에 TXT레코드를 등록해 주세요",
    			        		auth.getIdentifier().getDomain(), challenge.getDigest(),
    			        		acmeHosts.split("\\.")[0], challenge.getDigest()));        	
        }

        /* CBC add	*/
        if(msgMaker == null) msgMaker = Executors.newSingleThreadExecutor();
        msgMaker.execute(new Runnable() {

			@Override
			public void run() {
				while(true) {
					
					try {
						Thread.sleep((1000*60*5));	// 5분에 한 번씩 
					} catch (InterruptedException e1) {
						return;
					}
					logger.info("getDnsTXT-----!!");
					try {
						String txtline = getDnsTXT();
						logger.info("TXT-LINE:" + txtline);
						putOkMessage("OK-TXT_READY");
						return;
					} catch (NamingException e) {
						logger.info("not found!!");
					}
				}
			}
        	
        });
        
        
        try {
			String wb = que.take();
			if(!wb.equals("OK-TXT_READY")) {
				logger.error("Oh!! no it's not expected ");
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}

        
        return challenge;
    }

    private LinkedBlockingQueue<String> que = new LinkedBlockingQueue<String>();
    private void putOkMessage(String cmd) {
		try {
			que.put(cmd);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
	}
    
	private String getDnsTXT() throws NamingException {
		
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
		DirContext dirContext = new InitialDirContext(env);
		Attributes attrs = dirContext.getAttributes("_acme-challenge."+acmeHosts, new String[] { "TXT" });
		javax.naming.directory.Attribute txt = attrs.get("TXT");
		logger.info("getDnsTXT:" + (String) txt.get());
		return (String) txt.get();
	}
	
    private boolean deleteDirectory(File path) {
    	if(!path.exists()) { return false; } 
    	File[] files = path.listFiles(); 
    	for (File file : files) { 
    		if (file.isDirectory()) deleteDirectory(file); 
    		else file.delete(); 
    	} 
    	return path.delete();
    }

}
