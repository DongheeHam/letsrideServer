package clabs.srv.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import clabs.srv.mapper.LetsMapper;
import clabs.tools.Kakao_restapi;
import clabs.tools.ResObject;


@Service("LetsService")
public class LetsService {
private final static Logger logger = Logger.getLogger(LetsService.class.getName());
	
	@Autowired LetsMapper letsMapper;
	HashMap<String, String> map = new HashMap<String, String>();
	public ResObject tokenCheck(String at) {
	 
		// 테스트용
		if(at.equals("11111111111111111111111111111111")||
				at.equals("22222222222222222222222222222222")||
				at.equals("33333333333333333333333333333333")||
				at.equals("44444444444444444444444444444444")||
				at.equals("55555555555555555555555555555555")||
				at.equals("66666666666666666666666666666666")||
				at.equals("77777777777777777777777777777777")/*||
				at.equals("88888888888888888888888888888888")*/) {
			return new ResObject(1,"test용",null);
		}
		
		 return Kakao_restapi.checkAccessToken(at);
	}
	public Map<String, Object> getUserByLT(String lt) {
		logger.info("gdgdgdgd");
		logger.info(letsMapper);
		map.put("l_token", lt);
		Map<String,Object> re=letsMapper.getUserByLT(map);
		map.clear();
		logger.info("zdzdzdzd");
		return re;
	}
}
