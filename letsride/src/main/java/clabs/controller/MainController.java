package clabs.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import clabs.srv.mapper.LetsMapper;
import clabs.srv.service.LetsService;
import clabs.tools.Kakao_restapi;
import clabs.tools.ResObject;
import clabs.tools.StringUtils;


/*
 * this controller is 
 * for to install package.
 */
@Controller
@RequestMapping("/")
public class MainController extends BaseController {

	private final static Logger logger = Logger.getLogger(MainController.class.getName());	
	
	@Autowired LetsService letsService;
	@Autowired LetsMapper letsMapper;
	
	/*@Autowired SiSSO	sisso;*/
	
	/*
	 * index.do?page=xxx
	 * ----------------------------------
	 * default page is '/home'
	 */
	
	
	@RequestMapping(value="/index.do")
	public ModelAndView index(HttpServletRequest request, @RequestParam Map<String,String> params) throws IOException{
		ModelAndView model = new ModelAndView("/index");
		model.addObject("serverName", String.format("%s%s", request.getServerName(), (request.getServerPort() != 80) ? ":" + Integer.toString(request.getServerPort()) : ""));
		model.addObject("contextPath", context.getContextPath());
		model.addObject("page", params.get("page") != null ? params.get("page") : "/login");
		
		return model;
	}
	
	@RequestMapping(value="/page.do")
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String,String> params) throws IOException{
		
		logger.info("requestParam:" + params);
		String page = params.get("page");
		ModelAndView model = new ModelAndView("/pages" + page);

		model.addObject("contextPath", context.getContextPath());
		
		if("/chatRoom".equals(page)) {
			model.addAllObjects(params);
		}
		

		return model;
	} 

	@RequestMapping(value="/init.json", produces="application/json;charset=UTF-8")
	public @ResponseBody ResObject init(HttpServletRequest request, @RequestParam Map<String, String> params) throws IOException{
		logger.info("init.json - params : "+params);
		ResObject atCheck=letsService.tokenCheck(params.get("access_token"));
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
		  String headerName = (String)headerNames.nextElement();
		  System.out.println("" + headerName+" : "+request.getHeader(headerName));
		}
		logger.info("atCheck : "+atCheck);
		if(atCheck.getRc()!=1) { // at 유효성 체크
			return atCheck;
		}
		String id;
		try { // 실제 빌드땐 try내부문만 넣어도됨 단지 테스트용 아이디들 통과시키기 위함.
			id=""+(Math.round((Double)Kakao_restapi.getUserAttr(params.get("access_token")).get("id")));
		}catch (Exception e) {
			id=params.get("access_token").substring(0,1);
		}

		params.put("id", id);
		String new_l_token=StringUtils.getUUID();
		logger.info("new_l_token : "+ new_l_token);
		params.put("new_l_token", new_l_token);
		Map<String,Object> re=letsMapper.getUserByLT(/*params.get("l_token")*/params);
		logger.info("re : "+re);
		if(re!=null) { // 유효하되 변경된경우를 찾기위함.
			if(re.get("accessToken").equals(params.get("access_token"))){
				logger.info("같음");
				return new ResObject(1,"",null);				
			}else {
				logger.info("다름");
				int rc = letsMapper.updateLtoken(params);
				return new ResObject(2,"",new_l_token);
			}
		}else {//lt까지 손실했다면? 
			logger.info("lt가 유효하지 않음.");
			int rc = letsMapper.updateLtoken(params);
			return new ResObject(2,"",new_l_token);
		}
	}
	@RequestMapping(value="/join.json", produces="application/json;charset=UTF-8")
	public @ResponseBody ResObject join(HttpServletRequest request, @RequestParam Map<String, String> params) throws IOException{
		logger.info("join.json - params : "+params);
		Map<String,Object> re=letsMapper.getUserById(params);
		logger.info("re : "+re);
		ResObject atCheck=letsService.tokenCheck(params.get("access_token"));
		logger.info("atCheck : "+atCheck);
		if(atCheck.getRc()!=1) {
			return atCheck;
		}
		String l_token=StringUtils.getUUID();
		params.put("l_token", l_token);
		int a;
		if(re!=null) {
			a=letsMapper.updateUser(params);
		}else {
			a=letsMapper.insertUser(params);
		}
		return new ResObject(a, "", l_token);
	}
	@RequestMapping(value="/joinTest.json", produces="application/json;charset=UTF-8")
	public @ResponseBody ResObject joinTest(HttpServletRequest request, @RequestParam Map<String, String> params) throws IOException{
		logger.info("join.json - params : "+params);
		ResObject a=letsService.tokenCheck(params.get("access_token"));
		return new ResObject(1, "시스템 관리자 모드로 변환되었습니다.", null);
	}
}
