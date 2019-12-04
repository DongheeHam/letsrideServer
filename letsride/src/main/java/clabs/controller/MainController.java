package clabs.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
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
import clabs.tools.EchoHandler;
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
	@Autowired EchoHandler echoHandler;
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
	private ResObject checkParams(Map<String, String> params,String[] keys) {
		for(int i=0;i<keys.length; i++) {
			if(!params.containsKey(keys[i]))return new ResObject(-1,"필수 파라미터 누락",null);
		}
		if(letsMapper.getUserByLT(params)==null) {
			return new ResObject(-2,"유저 정보 없음",null);
		}return new ResObject(1,"");
	}
	@RequestMapping(value="/getRoomList.json", produces="application/json;charset=UTF-8")
	public @ResponseBody ResObject getAllRoom(HttpServletRequest request, @RequestParam Map<String, String> params) throws IOException{
		logger.info("getAllRoom.json - params : "+params);
		String[] needKeys= {"l_token"};
		ResObject checkResult=checkParams(params,needKeys);
		if(checkResult.getRc()<0) return checkResult;
		
		Map<String,Object> my=letsMapper.getMyRoom(params);
		Map<String,Object> attended=letsMapper.getAttendedRoom(params);
		List<Map<String,Object>> all=letsMapper.getAllRoomByAct();
		 
		Map<String,Object> re=new HashMap<String, Object>();
		re.put("my", my);
		re.put("attended", attended);
		re.put("all", all);
		return new ResObject(1, "success", re);
	}
	@RequestMapping(value="/makeMyRoom.json", produces="application/json;charset=UTF-8")
	public @ResponseBody ResObject makeMyRoom(HttpServletRequest request, @RequestParam Map<String, String> params) throws IOException{
		logger.info("makeMyRoom.json - params : "+params);
		String[] needKeys= {"l_token","title","schedule","gender","destination","sessionId"};
		ResObject checkResult=checkParams(params,needKeys);
		if(checkResult.getRc()<0) return checkResult;
		if(letsMapper.getMyRoom(params)!=null) {
			return new ResObject(-3,"이미 만든 방이 있습니다!",null);
		}
		
		try {
			int re=letsMapper.makeMyRoom(params);
			Map<String,Object> room=letsMapper.getMyRoom(params);
			int rno=(int)room.get("rno");
			EchoHandler.sessionList.get(params.get("sessionId")).put("my_rno",rno);
			echoHandler.sendNews(echoHandler.buildNewsString(letsMapper.getUserByLT(params).get("nickname")+"님이 들어왔습니다.", ""+rno));
			return new ResObject(re, "success", room);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResObject(-4,e.getMessage());
		}
	}
	@RequestMapping(value="/deleteMyRoom.json", produces="application/json;charset=UTF-8")
	public @ResponseBody ResObject deleteMyRoom(HttpServletRequest request, @RequestParam Map<String, String> params) throws IOException{
		logger.info("deleteMyRoom.json - params : "+params);
		String[] needKeys= {"l_token","sessionId","rno"};
		ResObject checkResult=checkParams(params,needKeys);
		if(checkResult.getRc()<0) return checkResult;
		// userinroom 에서 삭제되는 방에 인원이 있다면 삭제되었다는 메세지 보내기
		if(letsMapper.getUserInRoom(params)!=null) {
			String message="{\"deletedRoom\":\""+letsMapper.getRoom(params).get("title")+"\"}";
			echoHandler.sendRoomMessage(params.get("sessionId"), message, Integer.parseInt(params.get("rno")));
		}
		
		//echoHandler.han
		int a=letsMapper.deleteMyRoom(params);
		EchoHandler.sessionList.get(params.get("sessionId")).remove("my_rno");
		
		return new ResObject(1, "success", null);
	}
	@RequestMapping(value="/goInRoom.json", produces="application/json;charset=UTF-8")
	public @ResponseBody ResObject goInRoom(HttpServletRequest request, @RequestParam Map<String, String> params) throws IOException{
		logger.info("goInRoom.json - params : "+params);
		// params : l_token,sessionId,rno
		String[] needKeys= {"l_token","sessionId","rno"};
		ResObject checkResult=checkParams(params,needKeys);
		if(checkResult.getRc()<0) return checkResult;
		Map<String,Object> room=letsMapper.getRoom(params);
		try {
			if((int)room.get("num")>2) {
				return new ResObject(-3, "이미 가득 찬 방입니다.", null);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResObject(-4, "이미 사라진 방입니다.", null);
		}
		
		try {
			letsMapper.goOutRoom(params);
			int re=letsMapper.attendRoom(params);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResObject(-5, e.getMessage(), room);
		}
		EchoHandler.sessionList.get(params.get("sessionId")).put("attended_rno",Integer.parseInt(params.get("rno")));
		echoHandler.sendNews(echoHandler.buildNewsString(letsMapper.getUserByLT(params).get("nickname")+"님이 들어왔습니다.", params.get("rno")));
		return new ResObject(1, "success", room);
	}
	@RequestMapping(value="/goOutRoom.json", produces="application/json;charset=UTF-8")
	public @ResponseBody ResObject goOutRoom(HttpServletRequest request, @RequestParam Map<String, String> params) throws IOException{
		logger.info("goOutRoom.json - params : "+params);
		String[] needKeys= {"l_token","sessionId","rno"};
		ResObject checkResult=checkParams(params,needKeys);
		if(checkResult.getRc()<0) return checkResult;
		try {
			EchoHandler.sessionList.get(params.get("sessionId")).remove("attended_rno");
			letsMapper.goOutRoom(params);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResObject(-3,e.getMessage(),null);
		}
		echoHandler.sendNews(echoHandler.buildNewsString(letsMapper.getUserByLT(params).get("nickname")+"님이 나갔습니다.", params.get("rno")));
		return new ResObject(1, "success", null);
	}
	@RequestMapping(value="/getUserInfoThisRoom.json", produces="application/json;charset=UTF-8")
	public @ResponseBody ResObject getUserInfoThisRoom(HttpServletRequest request, @RequestParam Map<String, String> params) throws IOException{
		logger.info("getUserInfoThisRoom.json - params : "+params);
		String[] needKeys= {"l_token","rno"}; 
		ResObject checkResult=checkParams(params,needKeys);
		if(checkResult.getRc()<0) return checkResult;
		try {
			List<Map<String,Object>> users=letsMapper.getUserInfoThisRoom(params);
			return new ResObject(1, "success", users);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResObject(-3,e.getMessage(),null);
		}
	}
	@RequestMapping(value="/byForever.json", produces="application/json;charset=UTF-8")
	public @ResponseBody ResObject byForever(HttpServletRequest request, @RequestParam Map<String, String> params) throws IOException{
		logger.info("byForever.json - params : "+params);
		String[] needKeys= {"l_token","access_token","id"}; 
		ResObject checkResult=checkParams(params,needKeys);
		if(checkResult.getRc()<0) return checkResult;
		try {
			ResObject res=Kakao_restapi.unlink(params.get("access_token"));
			if(res.getRc()<0) {
				return res;
			}else {
				int id=(int)((double)((Map<String,Object>)res.getResult()).get("id"));
			
				letsMapper.unlink(params);
				return new ResObject(1, "success", null); 
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResObject(-3,e.getMessage(),null);
		}
	}
}
