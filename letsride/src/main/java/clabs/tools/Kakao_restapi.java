package clabs.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import clabs.srv.service.LetsService;

public class Kakao_restapi {
	
	private final static Logger logger = Logger.getLogger(LetsService.class.getName());
	
/*	public JsonNode getAccessToken(String autorize_code) {
        final String RequestUrl = "https://kauth.kakao.com/oauth/token";
        final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
        postParams.add(new BasicNameValuePair("client_id", "f2718ce94cd09ac1b864827d1578caee"));
        postParams.add(new BasicNameValuePair("redirect_uri", "http://localhost:7070/kakao/acs.do"));
        postParams.add(new BasicNameValuePair("code", autorize_code));
        final CloseableHttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);
        JsonNode returnNode = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(postParams));
            final CloseableHttpResponse response = client.execute(post);
            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
 
        }
        return returnNode;
	}*/
	@SuppressWarnings("unchecked")
	public static ResObject checkAccessToken(String access_token) {
        final String RequestUrl = "https://kapi.kakao.com/v1/user/access_token_info";
        
        final CloseableHttpClient client = HttpClientBuilder.create().build();
        final HttpGet get = new HttpGet(RequestUrl);
        Map<String, Object> tmpMap = new HashMap<String, Object>();
        try {
        	get.addHeader("Authorization","Bearer "+access_token);
            CloseableHttpResponse response = client.execute(get);
            int status=response.getStatusLine().getStatusCode();// status code
            logger.info("status : "+status);
            
            String json = EntityUtils.toString(response.getEntity(), "UTF-8");
            tmpMap = new Gson().fromJson(json, HashMap.class);
            logger.info("json : "+json);
            logger.info("tmpMap : "+tmpMap);
            if(status==200) {
            	return new ResObject(1,"정상",null);
            }else if(status==400) {
            	return new ResObject(-1,"잘못된 키 형식",null);
            }else if(status==401) {
            	return new ResObject(-2,"만료된 토큰",null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return null;
	}
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getUserAttr(String access_token) {
        final String RequestUrl = "https://kapi.kakao.com/v2/user/me";
        
        final CloseableHttpClient client = HttpClientBuilder.create().build();
        final HttpGet get = new HttpGet(RequestUrl);
        Map<String, Object> tmpMap = new HashMap<String, Object>();
        try {
        	get.addHeader("Authorization","Bearer "+access_token);
            CloseableHttpResponse response = client.execute(get);
            int status=response.getStatusLine().getStatusCode();// status code
            logger.info("status : "+status);
            
            String json = EntityUtils.toString(response.getEntity(), "UTF-8");
            tmpMap = new Gson().fromJson(json, HashMap.class);
            logger.info("json : "+json);
            logger.info("tmpMap : "+tmpMap);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
 
        }
        return tmpMap;
	}
}
