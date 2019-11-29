package clabs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.standard.SpringConfigurator;


import clabs.tools.JsonEncoder;

import clabs.srv.service.LetsService;

import javax.websocket.RemoteEndpoint.Basic;

@Controller
@ServerEndpoint(value="/echo/{params}" , encoders = {JsonEncoder.class}, configurator = SpringConfigurator.class)
public class ChatController extends TextWebSocketHandler{
	public static final Map<String,Map<String,Object>> sessionList=new HashMap<String,Map<String,Object>>();
	//public static final List<Session> sessionList=new ArrayList<Session>();
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

	LetsService letsService;
	
    
	
	
    public ChatController() {
        // TODO Auto-generated constructor stub
        logger.info("웹소켓(서버) 객체생성");
        letsService=new LetsService();
    }
    
    
    @RequestMapping(value="/chat/chat.do")
    public ModelAndView getChatViewPage(ModelAndView model) {
    	logger.info("chat.do - model : "+model.getModel());
        model.setViewName("chat");
       
        return model;
    }
    @SuppressWarnings("unchecked")
	@OnOpen
    public void onOpen(Session session, @PathParam("clientId") String clientId,
    		@PathParam("nickname") String nickname, @PathParam("profile_image") String profile_image) {
        logger.info("Open session id:"+session.getId());
        logger.info("clientId:"+clientId);
        String l_token=session.getRequestParameterMap().get("params").get(0);
        //Map<String, Object> params = new Gson().fromJson(json, HashMap.class);
        logger.info("123session.getRequestParameterMap() : "+session.getRequestParameterMap());
        logger.info("11현재 sessionList : "+sessionList);
        
        Map<String,Object> map =new HashMap<String,Object>();
        map.put("session", session);
        logger.info("eeeeeeeee");
        map.put("l_token", l_token);
        logger.info("rrrrrrrrrrr"); //여기까지 뜨고 끊기네 ?? 왜?
        /*logger.info("db에서 가쟈온 nickname : "+letsService.getUserByLT(l_token).get("nickname"));
        Map<String,Object> re=letsMapper.getUserByLT(l_token);
        logger.info("wwwwwww re : " +re);
        
        //session.getUserProperties().put("l_token", l_token);
        session.getUserProperties().put("nickname", re.get("nickname"));
        session.getUserProperties().put("imagepath", re.get("imagepath"));
        */
        try {
            final Basic basic=session.getBasicRemote();
            basic.sendText("Connection Established");
        }catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        
        sessionList.put(session.getId(), map);
        //sessionList.add(session);
        logger.info("22현재 sessionList : "+sessionList.size());
    }
    
  /*    모든 사용자에게 메시지를 전달한다.
      @param self
      @param message
     */
    private void sendAllSessionToMessage(Session self,String message) {
    	logger.info("sendAllSessionToMessage-message = "+message);
    	
        try {
            for(String sessionid : ChatController.sessionList.keySet()) {
            	
            	
                if(!self.getId().equals(sessionid)) {
                    Session s=(Session)sessionList.get(sessionid).get("session");
                    s.getBasicRemote().sendText(message);
                    /*JsonObject event = Json.createObjectBuilder().
                            add("sender",(String)s.getUserProperties().get("nickname")).
                            add("message",message).
                            add("imagepath",(String)s.getUserProperties().get("imagepath")).
                            add("l_token",(String)s.getUserProperties().get("l_token")).
                            build();
                    s.getBasicRemote().sendObject(event);*/
                } 
            }
        	/*for(Session s: sessionList) {
        		JsonObject event = Json.createObjectBuilder().
                        add("sender",(String)s.getUserProperties().get("nickname")).
                        add("message",message).
                        add("imagepath",(String)s.getUserProperties().get("imagepath")).
                        add("l_token",(String)s.getUserProperties().get("l_token")).
                        build();
                s.getBasicRemote().sendObject(event);
        	} */
        	
        }catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }
    @OnMessage
    public void onMessage(String message,Session session) {
        logger.info("Message :"+message);
        
        try {
            final Basic basic=session.getBasicRemote();
            basic.sendText("to : "+message);
        }catch (Exception e) {
            // TODO: handle exception 
            System.out.println(e.getMessage());
        }
        sendAllSessionToMessage(session, message);
    }
    @OnError
    public void onError(Throwable e,Session session) {
        logger.info("exception : "+e.getMessage());
    }
    @OnClose
    public void onClose(Session session) {
        logger.info("Session "+session.getId()+" has ended");
        sessionList.remove(session);
    }

}
