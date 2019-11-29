/*package clabs.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;

import clabs.tools.Kakao_restapi;



 * this controller is 
 * for to install package.
 
@Controller
@RequestMapping("/kakao")
public class KakaoController extends BaseController {

	private final static Logger logger = Logger.getLogger(KakaoController.class.getName());	
	
	@Autowired SiSSO	sisso;
	
	
	 * index.do?page=xxx
	 * ----------------------------------
	 * default page is '/home'
	 
	
	
	@RequestMapping(value="/code.do", produces="application/json;charset=UTF-8")
	public ModelAndView code(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String,String> params) throws IOException{
		System.out.println("asdqwfqwfqwf");
		logger.info("code.do/ : params : "+params);
		Kakao_restapi kr = new Kakao_restapi();
        //결과값을 node에 담아줌
        JsonNode node = kr.getAccessToken(params.get("code"));
        //결과값 출력
        System.out.println(node);
        //노드 안에 있는 access_token값을 꺼내 문자열로 변환
        String token = node.get("access_token").toString();
        //세션에 담아준다.
        //session.setAttribute("token", token);
        logger.info("token : "+token);
        return null;
        
        ModelAndView model= new ModelAndView("/pages/goRedirect");
        
        final String RequestUrl = "https://kauth.kakao.com/oauth/token";
		
		StringBuffer localStringBuffer = new StringBuffer();
		localStringBuffer.append(RequestUrl);
		localStringBuffer.append(RequestUrl.contains("?") ? "&" : "?");
		localStringBuffer.append("grant_type=");
		localStringBuffer.append("authorization_code");
		localStringBuffer.append("&client_id=");
		localStringBuffer.append("f2718ce94cd09ac1b864827d1578caee");
		localStringBuffer.append("&redirect_uri=");
		localStringBuffer.append("http://localhost:7070/kakao/code.do");
		localStringBuffer.append("&code=");
		localStringBuffer.append(URLEncoder.encode(params.get("code"), "UTF-8"));
		
		model.addObject("redirectURL", localStringBuffer.toString());
        return model;

	}
	@RequestMapping(value="/acs.do", produces="application/json;charset=UTF-8")
	public String acs(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String,String> params) throws IOException{
		logger.info("acs.do/ : params : "+params);
		
		return null;
	}

	


	@RequestMapping(value="/setModeAdmin.json", produces="application/json;charset=UTF-8")
	public @ResponseBody ResObject setModeAdmin(HttpServletRequest request, @RequestParam Map<String, String> params) throws IOException{
		
		request.getSession().setAttribute(GSuiteAccessMode, "__YES__");
		return new ResObject(1, "시스템 관리자 모드로 변환되었습니다.", null);
	}

}
*/