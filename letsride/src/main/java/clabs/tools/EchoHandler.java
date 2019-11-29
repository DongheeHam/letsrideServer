package clabs.tools;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

//import clabs.tools.JsonEncoder;

import clabs.srv.mapper.LetsMapper;
import clabs.srv.service.LetsService;

//import javax.websocket.RemoteEndpoint.Basic;

public class EchoHandler  extends TextWebSocketHandler{
	public static final Map<String,Map<String,Object>> sessionList=new HashMap<String,Map<String,Object>>();
	//public static final List<Session> sessionList=new ArrayList<Session>();
    private static final Logger logger = LoggerFactory.getLogger(EchoHandler.class);

	@Autowired LetsService letsService;
	@Autowired LetsMapper letsMapper;
    
	// 클라이언트와 연결 이후에 실행되는 메서드
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info("Open spring websocket!!! session id:"+session.getId());
		
        Map<String,Object> map =new HashMap<String,Object>();
        map.put("session", session);
        String l_token=session.getUri().getQuery();
        
        Map<String,Object> re=letsMapper.getUserByLT(l_token);
        logger.info("wwwwwww re : " +re);
        
        map.put("nickname", re.get("nickname"));
        map.put("imagepath", re.get("imagepath"));
        map.put("nickname", re.get("nickname"));
        
        
        sessionList.put(session.getId(), map);
        
        logger.info("현재 sessionList : "+sessionList);
        sendNews(buildNewString(""+map.get("nickname")+"님이 접속했습니다."));
	}

	// 클라이언트가 서버로 메시지를 전송했을 때 실행되는 메서드
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		logger.info("{}로 부터 {} 받음", session.getId(), message.getPayload());
		
		
		logger.info("Message :"+message);
		
        //session.sendMessage(new TextMessage("to : "+message));
        sendAllSessionToMessage(session, message);	
	}
	private String buildNewString(String message) {
		return "{\"message\":\""+message+"\",\"scope\":\"news\",\"num\":\""+sessionList.size()+"\"}";
	}
	// 클라이언트와 연결을 끊었을 때 실행되는 메소드
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
		logger.info("{} 연결 끊김", session.getId());
		sendNews(buildNewString(""+sessionList.get(session.getId()).get("nickname")+"님이 나갔습니다."));
		sessionList.remove(session.getId());
	}
	
	 
    // 메시지 전송시나 접속해제시 오류가 발생할 때 호출되는 메소드
    @Override
    public void handleTransportError(WebSocketSession session,
            Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        logger.info("전송오류 발생 : "+exception.getMessage());
    }
    private void sendNews(String message) {
    	logger.info("sendNewsAll-message = "+message);
        try {
            for(String sessionid : sessionList.keySet()) {
        		WebSocketSession s=(WebSocketSession)sessionList.get(sessionid).get("session");
        		s.sendMessage(new TextMessage(message));
            }
        	
        }catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }
      //모든 사용자에게 메시지를 전달한다.
      //@param self
      //@param message
     
    private void sendAllSessionToMessage(WebSocketSession session,TextMessage message) {
    	logger.info("sendAllSessionToMessage-message = "+message);
        try {
            for(String sessionid : sessionList.keySet()) {
            	if(!session.getId().equals(sessionid)) {
            		WebSocketSession s=(WebSocketSession)sessionList.get(sessionid).get("session");
            		s.sendMessage(new TextMessage(message.getPayload()));
            	}
            }
        }catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }

}
