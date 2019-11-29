<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%@
    taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%
%>
<section>
<h1>채팅방</h1>
<div>
        <input type="text" id="sender" value="${name}" style="display: none;">
        <input type="text" id="messageinput">
    </div>
    <div>
        <button type="button" onclick="openSocket();">Open</button>
        <button type="button" onclick="send();">Send</button>
        <button type="button" onclick="closeSocket();">Close</button>
    </div>
    <!-- Server responses get written here -->
    <div id="messages"></div>

</section>
<script>
var ws;
var messages=document.getElementById("messages");

function openSocket(){
    if(ws!==undefined && ws.readyState!==WebSocket.CLOSED){
        writeResponse("WebSocket is already opened.");
        return;
    }
    //웹소켓 객체 만드는 코드
    ws=new WebSocket("ws://localhost:7070/chat/echo.do");
    
    ws.onopen=function(event){
        if(event.data===undefined) return;
        
        writeResponse(event.data);
    };
    ws.onmessage=function(event){
        writeResponse(event.data);
    };
    ws.onclose=function(event){
        writeResponse("Connection closed");
    }
}

function send(){
    var text=document.getElementById("messageinput").value+","+document.getElementById("sender").value;
    ws.send(text);
    text="";
}

function closeSocket(){
    ws.close();
}
function writeResponse(text){
    messages.innerHTML+="<br/>"+text;
}


</script>