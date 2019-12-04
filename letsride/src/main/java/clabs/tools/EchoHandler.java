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

import com.google.gson.Gson;

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
        map.put("l_token", l_token);
        map.put("nickname", re.get("nickname"));
        map.put("imagepath", re.get("imagepath"));
        
        
        sessionList.put(session.getId(), map);
        
        logger.info("현재 sessionList : "+sessionList);
        session.sendMessage(new TextMessage("{\"sessionId\":\""+session.getId()+"\"}"));
        sendNews(buildNewsString(""+map.get("nickname")+"님이 접속했습니다.","all"));
	}

	// 클라이언트가 서버로 메시지를 전송했을 때 실행되는 메서드
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		logger.info("{}로 부터 {} 받음", session.getId(), message.getPayload());
		
		
		logger.info("Message :"+message);
		
        //session.sendMessage(new TextMessage("to : "+message));
        sendSessionToMessage(session, message);	
        //session.sendMessage(new TextMessage("{\"sessionID\":\""+session.getId()+"\"}"));
	}
	public String buildNewsString(String message,String scope) {
		String num;
		if(scope.equals("all")) {
			num=""+sessionList.size();
		}else {
			Map<String,String> m=new HashMap<String,String>();
			m.put("rno", scope);
			num=""+((int)letsMapper.getRoom(m).get("num")+1);
		}
		return "{\"message\":\""+message+"\",\"scope\":\""+scope+"\",\"news\":\"news\",\"num\":\""+num+"\"}";
	}
	// 클라이언트와 연결을 끊었을 때 실행되는 메소드
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		Map<String, String> m=new HashMap<String,String>();
		logger.info("{} 연결 끊김", session.getId());
		if(sessionList.get(session.getId()).containsKey("my_rno")) {
			m.put("rno", ""+sessionList.get(session.getId()).get("my_rno"));
			if(letsMapper.getUserInRoom(m)!=null) {
				String message="{\"deletedRoom\":\""+letsMapper.getRoom(m).get("title")+"\"}";
				sendRoomMessage(session.getId(), message, Integer.parseInt(m.get("rno")));
			}
			try {
				letsMapper.deleteMyRoom(m);
			}catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			sessionList.get(session.getId()).remove("my_rno");
		}
		if(sessionList.get(session.getId()).containsKey("attended_rno")) {
			m.put("l_token", (String)sessionList.get(session.getId()).get("l_token"));
			
			try {
				logger.info("m:"+m);
				letsMapper.goOutRoom(m);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			sessionList.get(session.getId()).remove("attended_rno");
		}
		
		
		sendNews(buildNewsString(""+sessionList.get(session.getId()).get("nickname")+"님이 나갔습니다.","all"));
		sessionList.remove(session.getId());
	}
	
	
    // 메시지 전송시나 접속해제시 오류가 발생할 때 호출되는 메소드
    @Override
    public void handleTransportError(WebSocketSession session,
            Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        logger.info("전송오류 발생 : "+exception.getMessage());
    }
    @SuppressWarnings("unchecked")
	public void sendNews(String message) {
    	logger.info("sendNewsAll-message = "+message);
    	Map<String, Object> tmpMap = new HashMap<String, Object>();
    	tmpMap = new Gson().fromJson(message, HashMap.class);
    	String scope=""+tmpMap.get("scope");
        try {
            if(scope.equals("all")) {
            	for(String sessionid : sessionList.keySet()) {
            		WebSocketSession s=(WebSocketSession)sessionList.get(sessionid).get("session");
            		s.sendMessage(new TextMessage(message));
                }
        	}else {
        		for(String sessionid : sessionList.keySet()) {
        			logger.info("scope : "+scope+" | "+sessionList.get(sessionid).get("nickname")+" attended_rno : "+sessionList.get(sessionid).get("attended_rno")+" | my_rno : "+sessionList.get(sessionid).get("my_rno"));
                	if(scope.equals(""+sessionList.get(sessionid).get("attended_rno"))
                			||scope.equals(""+sessionList.get(sessionid).get("my_rno"))) {
                		WebSocketSession s=(WebSocketSession)sessionList.get(sessionid).get("session");
                		logger.info(sessionList.get(sessionid).get("nickname")+"한테 보냄");
                		s.sendMessage(new TextMessage(message));
                	}
                }
        	}
        }catch (Exception e) {
        	e.printStackTrace();
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }
      //모든 사용자에게 메시지를 전달한다.
      //@param self
      //@param message
    @SuppressWarnings("unchecked")
	private void sendSessionToMessage(WebSocketSession session,TextMessage message) {
    	logger.info("sendSessionToMessage-message = "+message);
    	Map<String, Object> tmpMap = new HashMap<String, Object>();
    	tmpMap = new Gson().fromJson(message.getPayload(), HashMap.class);
    	String scope=""+tmpMap.get("scope");
    	logger.info("scope:"+tmpMap.get("scope")+"scope.class:"+tmpMap.get("scope").getClass());
        try {
        	if(scope.equals("all")) {
        		for(String sessionid : sessionList.keySet()) {
                	if(!session.getId().equals(sessionid)) {
                		WebSocketSession s=(WebSocketSession)sessionList.get(sessionid).get("session");
                		s.sendMessage(new TextMessage(message.getPayload()));
                	}
                }
        	}else {
        		for(String sessionid : sessionList.keySet()) {
        			logger.info("scope : "+scope+" | "+sessionList.get(sessionid).get("nickname")+" attended_rno : "+sessionList.get(sessionid).get("attended_rno")+" | my_rno : "+sessionList.get(sessionid).get("my_rno"));
                	if(!session.getId().equals(sessionid)
                			&&(scope.equals(""+sessionList.get(sessionid).get("attended_rno"))
                			||scope.equals(""+sessionList.get(sessionid).get("my_rno")) )) {
                		WebSocketSession s=(WebSocketSession)sessionList.get(sessionid).get("session");
                		logger.info(sessionList.get(sessionid).get("nickname")+"한테 보냄");
                		s.sendMessage(new TextMessage(message.getPayload()));
                	}
                }
        	}
            
        }catch (Exception e) {
        	e.printStackTrace();
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }
    public void sendRoomMessage(String session,String message,int rno) {
    	logger.info("sendRoomMessage-message = "+message);
    	
        try {
            for(String sessionid : sessionList.keySet()) {
            	if(!session.equals(sessionid)&&
            			((int)sessionList.get(sessionid).get("attended_rno")==rno||
            					(int)sessionList.get(sessionid).get("my_rno")==rno)){
            		WebSocketSession s=(WebSocketSession)sessionList.get(sessionid).get("session");
            		
            		s.sendMessage(new TextMessage(message));
            	}
            }
        }catch (Exception e) {
            // TODO: handle exception
        	e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
