<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@
    taglib prefix="c"
	uri="http://java.sun.com/jsp/jstl/core"%>
<%
%><!DOCTYPE html>
<html>
<meta http-equiv="Content-Type" content="text.html; charset=utf-8" />
<head>
<title>lets ride</title>

<meta name="viewport" content="width=device-width,initial-scale=1">
<link rel="stylesheet" href="/res/css/main.css">

<!-- 제이쿼리모바일 -->
<link rel="stylesheet"
	href="/res/jquery.mobile-1.4.5/jquery.mobile-1.4.5.min.css">
<script src="/res/js/jquery-1.11.1.min.js"></script>
<script src="/res/jquery.mobile-1.4.5/jquery.mobile-1.4.5.min.js"></script>
<title>Let's Ride</title>


<!-- 카카오 RestAPI  -->
<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>

<script>
    $(document).ready(function(){
        $(".refresh").click(function(){
            location.reload();
        });
        $(".roomtitle1").click(function(){
        	$(this).children("ul").slideToggle(500);
        });
        $(".roomtitle2").click(function(){
        	$(this).children("ul").slideToggle(500);
        });
        $(".roomtitle3").click(function(){
        	$(this).children("ul").slideToggle(500);
        });
    });
</script>

</head>
<body>
	<script>
	var app={
			go : function(page, param, cb){
				let p = (typeof param !== 'object') ? {} : param;
				//window.location.hash ="#"+page;
				p.page = page;
				$.post("${contextPath}/page.do", p,  function(res,status,xhr){
					
					$("#pageWrap").html(res);
					if(typeof cb === 'function') cb(res);
				});
			},
			get : function(uri, param, cb){
				$.post(uri + ".json", this.setDefaultParam(param), function(res){
					if(typeof cb === "function") cb(res);
				}); 
			},
			loadInDiv : function(where,page, param, cb){
				var p = (typeof param !== 'object') ? {} : param;
				//window.location.hash ="#"+page;
				
				p.page = page;
				$.post("/page.do", p,  function(res){
					$(where).html(res);
					if(typeof cb === 'function') cb(res);
				});
			},
			
			setDefaultParam : function(param){
				var p = (typeof param !== 'object') ? {} : param;
				return p;
			}
		};
		
	</script>
	<section data-role="page" id="loding">
		<div data-role="content">
			<div id="letsride">Let's Ride2</div>
			<div id="logo">
				로딩중입니다.
			</div>
		</div>
	</section>
	<section data-role="page" id="login">
		<div data-role="content">
			<p id="letsride">Let's Ride2</p>
			<div id="logo">
				<a id="kakaoLogin" data-role="button" data-icon="comment"
					data-iconpos="bottom"> 카카오톡 로그인 </a>
					<input type="text" id="virtual-user">
			</div>

		</div>
		<div id="divResult"></div>
	</section>
	<!---------------------------홈화면------------------------->
	<section data-role="page" id="home">
		<div data-role="header" style="background-color: whitesmoke;">
			
		
			<h1 class="head">Let's Ride</h1>
			<a href="#menuPanel" data-icon="bars"
				data-iconpos="notext">Add</a>
		</div>
		<div data-role="content">
			<div id="homeimg">
				<div id="">Let’s ride는 경제적인 소비습관을 지향합니다.</div>

			</div>
			<br>
			<div id="all-chat-room">
				<div>전체 채팅방 접속자:<span id="chat-area-all-num">0</span>명</div>
				
				<div class="chat-area" id="chat-area-all"></div>
				<div id="chat-form">
					<form onsubmit="return false;">
					<div class="ui-grid-a">
						<div class="ui-block-a" style="width:80%;">
							<textarea class="chatInputText" style="resize: none" name="chatInputText"  onkeydown="JavaScript:Enter_Check(this.form);"></textarea>
						</div>
						<div class="ui-block-b" style="width:20%">
							<input type="button" data-mini="true" onclick="chat_send(this.form)" value="전송">
						</div>
					</div>
						<input type="hidden" name="chatRoomScope" value="all">
					</form>
				</div>
			</div>
		</div>
		<div data-role="footer" data-position="fixed"
			style="background-color: whitesmoke;">
			<div data-role="navbar">
				<ul>
					<li><a href="#room" data-icon="grid" data-transition="slide"
						data-direction="reverse">방목록</a></li>
					<li><a href="#home" data-icon="home">홈</a></li>
					<li><a href="#busstop" data-icon="location"
						data-transition="slide">버스 정보</a></li>
				</ul>
			</div>
		</div>
		<div data-role="panel" id="menuPanel" data-position="right"
			data-display="overlay">
			<h3>Menu</h3>
			<ul data-role="listview">
				<li><a href="#room">설정 메뉴</a></li>
				<li data-icon="power"><a id="kakaoLogout" data-role="button"
					data-icon="comment"> 로그아웃 </a></li>
			</ul>
		</div>
	</section>

	

	<!----------------------------------방목록--------------------------------->
	<section data-role="page" id="room">
            <div data-role="header">
                <a href="javascript:history.back()" data-icon="back" data-iconpos="notext"></a>
                <h1 class="head">Let's Ride</h1>
                <a href="#menuPanel" data-icon="bars" data-iconpos="notext">Add</a>
            </div>
            <div data-role="content">
                <div class="roomtitle1">
	                <div>내가 만든 방</div>
	                	<ul data-role="listview" class="myroom">
	                        <li style="margin-top: 25px"><a href="">
	                            <h2>11시 34분 명학도착 두분구해요^^</h2>
	                            <p><strong>여자만</strong>-<strong>성결관</strong></p>
	                        <p class="ui-li-aside"><strong>6:24</strong>PM</p></a></li>
	                </ul>
                </div>
                
                <div class="roomtitle2">
                	<div>내가 포함된 방</div>
	                <ul data-role="listview" class="roomin">
	                        <li style="margin-top: 25px"><a href="">
	                            <h2>I Want Taxi Plz</h2>
	                            <p><strong>성별무관</strong>-<strong>중생관</strong></p>
	                            <p class="ui-li-aside"><strong>7:21</strong>PM</p></a></li>
	                </ul>
                </div>
                <div class="roomtitle3">
                <div>모든 방</div>
	                <ul data-role="listview" class="allroom">
	                        <li style="margin-top: 25px"><a href="">
	                            <h2>4교시 택시팟구함</h2>
	                            <p><strong>여자만</strong>-<strong>기념관</strong></p>
	                            <p class="ui-li-aside"><strong>12:00</strong>PM</p></a></li>
	                        <li style="margin-top: 25px"><a href="">
	                            <h2>15학번 송혜지랑 타실분~</h2>
	                            <p><strong>남자만</strong>-<strong>성결관</strong></p>
	                            <p class="ui-li-aside"><strong>11:34</strong>PM</p></a></li>
	                        <li style="margin-top: 25px"><a href="">
	                            <h2>1교시빠르게 두분빨리</h2>
	                            <p><strong>성별무관</strong>-<strong>정문</strong></p>
	                            <p class="ui-li-aside"><strong>4:24</strong>PM</p></a></li>
	                        <li style="margin-top: 25px"><a href="">
	                            <h2>컴온컴온</h2>
	                            <p><strong>성별무관</strong>-<strong>중생관</strong></p>
	                            <p class="ui-li-aside"><strong>3:14</strong>PM</p></a></li>
	                </ul>
                </div>
                <a data-role="button" style="position : fixed; bottom : 10%; right : 5%; border-radius: 10%;" data-rel="popup" href="#roommake" 
                data-position-to="window" >방 만들기</a>
               
            </div>
            <div data-role="footer" data-position="fixed">
                <div data-role="navbar">
                    <ul>
                        <li><a href="#room" data-icon="grid">방목록</a></li>
                        <li><a href="#home" data-icon="home" data-transition="slide">홈</a></li>
                        <li><a href="#busstop" data-icon="gear" data-transition="slide">버스 정보</a></li>
                    </ul>
                </div> 
            </div>
            <div data-role="panel" id="menuPanel" data-position="right" data-display="overlay">
                <h3>Menu</h3>
                <ul data-role="listview">
                    <li><a href="#room">설정 메뉴</a></li>
                    <li data-icon="power">
                    <a id="kakaoLogout" data-role="button" data-icon="comment"> 로그아웃 </a></li>
                </ul>
            </div>
            <div data-role="popup" id="roommake" style="padding:30px;">
                    <!--<header data-role="header" data-add-back-btn="true"><h1>방 만들기</h1></header>-->
                        <!--<div data-role="content">-->
                            <form class="rm">
                                <label for="rn"><p class="ft">방제목*</p></label>
                                <input type="text" name="rn" id="rn">
                                <fieldset data-role="controlgroup" data-type="horizontal">
                                    <legend class="ft">성별*</legend>
                                    <input type="radio" name="sex" id="sex1">
                                    <label for="sex1">남자만</label>
                                    <input type="radio" name="sex" id="sex2">
                                    <label for="sex2">여자만</label>
                                    <input type="radio" name="sex" id="sex3">
                                    <label for="sex3">성별무관</label>
                                </fieldset>
                                <fieldset data-role="controlgroup" data-type="horizontal">
                                    <legend class="ft">도착지*</legend>
                                    <input type="radio" name="arrive" id="arrive1">
                                    <label for="arrive1">정문</label>
                                    <input type="radio" name="arrive" id="arrive2">
                                    <label for="arrive2">중생관</label>
                                    <input type="radio" name="arrive" id="arrive3">
                                    <label for="arrive3">성결관</label>
                                </fieldset>
                                <!--<label for="time">도착예정시간</label>
                                <input type="time" data-clear-btn="true" name="time" id="time">-->
                                <label for="rn"><p class="ft">인상착의를 알려주세요!*</p></label>
                                <input type="text" name="rn" id="rn">
                                <input type="submit" value="확인">
                            </form>
                        </div>
                    
        </section>
	<!---------------------------------------------------------------버스정보------------------------------------------------------------------------------------->
	<section data-role="page" id="busstop">
		<div data-role="header" style="background-color: whitesmoke;">
			<a href="javascript:history.back()" data-icon="back"
				data-iconpos="notext"></a> <a href="#menuPanel" data-icon="bars"
				data-iconpos="notext">Add</a>
			<h1 class="head">Let's Ride</h1>
		</div>
		<div data-role="content">
				
				<div data-role="collapsible" data-collapsed="false" data-inset="false">
			    <h3>명학역 마을버스</h3>
				 <a onClick="drowBus()" style="float: right" data-role="button" data-icon="refresh" data-iconpos="notext"></a>			    
			    <div>
			    	<p>10-1</p>
			    	<div>
			    		<p>1번째 : 
				    		<span id="bus10_1-first">
				    			<span id="bus10_1-first-time"></span>분 후 (<span id="bus10_1-first-loc"></span>번째 전)
				    		</span>
			    		</p>
			    		<p>2번째 : 
			    			<span id="bus10_1-second">
			    				<span id="bus10_1-second-time"></span>분 후 (<span id="bus10_1-second-loc"></span>번째 전)
			    			</span>
			    		</p>
			    	</div>
			    	<p>10-2</p>
			    	<div>
			    		<p>1번째 :
				    		<span id="bus10_2-first"> 
				    			<span id="bus10_2-first-time"></span>분 후 (<span id="bus10_2-first-loc"></span>번째 전)
				    		</span>
			    		</p>
			    		<p>2번째 : 
				    		<span id="bus10_2-second">
				    			<span id="bus10_2-second-time"></span>분 후 (<span id="bus10_2-second-loc"></span>번째 전)
				    		</span>
			    		</p>
			    	</div>
			    </div>
			  </div>
 			
			  <div data-role="collapsible" data-collapsed="false" data-inset="false">
			    <h3>통학버스 운행시간표</h3>
			    <p>명학역 => 성결대학교</p>
			    <div>
			    	<p><span class="red">08:00 ~ 10:50</span> 매시 10분 간격 운행 </p>
			    	<p><span class="red">12:30 ~ 13:30</span> 매시 10분 간격 운행 </p>
			    	<p><span class="red">16:10 ~ 18:00</span> 매시 10분 간격 운행 </p>
			    </div>
			    <p>성결대학교 => 명학역</p>
			    <div>
			    	<p><span class="red">12:20 ~ 13:20</span> 매시 10분 간격 운행 </p>
			    	<p><span class="red">16:00 ~ 17:50</span> 매시 10분 간격 운행 </p>
			    	<p><span class="red">21:20, 21:40 (2회)</span> 성결관 앞</p>
			    </div>
			  </div>
		</div>
		<div data-role="footer" data-position="fixed"
			style="background-color: whitesmoke;">
			<div data-role="navbar">
				<ul>
					<li><a href="#room" data-icon="grid" data-transition="slide"
						data-direction="reverse">방목록</a></li>
					<li><a href="#home" data-icon="home" data-transition="slide"
						data-direction="reverse">홈</a></li>
					<li><a href="#busstop" data-icon="gear">버스정보</a></li>
				</ul>
			</div>
		</div>
		
	</section>
</body>
<script>
var host='http://localhost:7070/';
var wshost='ws://localhost:7070/echo.do';
//var host='https://letsride.donghee.site/';
//var wshost='wss://letsride.donghee.site/echo.do';
var defaultImage='/res/img/defaultImage.jpg';
var busDataKey='QSvyyLPWs8JnMdaTdRnMdm05Rk1QGE%2F2yIpA%2FZ2uk2pmEBCmceoErVoMCf2GarMegjV8feESa26JjCuDqmKtCA%3D%3D';

function Enter_Check(form){
    // 엔터키의 코드는 13입니다.
	if(event.keyCode == 13){
		if(form.chatInputText.value!="")
			chat_send(form);  // 실행할 이벤트
	}
}

//document.addEventListener("deviceready", function(){
$(document).ready(function(){
	console.log("deviceready"); 
	if(window.localStorage.getItem("l_token")&&window.localStorage.getItem("access_token")){
		app.get(host+'init',{l_token:window.localStorage.getItem("l_token"),
			access_token:window.localStorage.getItem("access_token"),
			id:window.localStorage.getItem("id")},function(res){
				if(res.rc==1){
					$.mobile.changePage("#home",{});
				}else if(res.rc==2){
					window.localStorage.setItem("l_token", res.result);
					$.mobile.changePage("#home",{});
				}else{
					$.mobile.changePage("#login",{});
				}
			});
	}else{
		$.mobile.changePage("#login",{});
	}
});
//}, false);

$('#kakaoLogin').tap(function(){
	console.log('로그인 탭 이벤트 발생');
	var id,nickname,profile_image,access_token,gender;
	if($('#virtual-user').val()=='8'){
		id=8;
		nickname="김누구";
	    profile_image="https://t1.daumcdn.net/qna/image/1542632018000000528";
	    access_token="88888888888888888888888888888888";
	    gender="f";
	}else if($('#virtual-user').val()=='1'){
		id=1;
	    nickname="이승현";
	    profile_image="https://t1.daumcdn.net/qna/image/1542632018000000528";
	    access_token="11111111111111111111111111111111";
	    gender="m";
	}else if($('#virtual-user').val()=='2'){
		id=2;
	    nickname="함동키";
	    profile_image="https://t1.daumcdn.net/qna/image/1542632018000000528";
	    access_token="22222222222222222222222222222222";
	    gender="m";
	}else if($('#virtual-user').val()=='3'){
		id=3;
	    nickname="정경석";
	    profile_image="https://t1.daumcdn.net/qna/image/1542632018000000528";
	    access_token="33333333333333333333333333333333";
	    gender="m";
	}else if($('#virtual-user').val()=='4'){
		id=4;
	    nickname="사보현";
	    profile_image="https://t1.daumcdn.net/qna/image/1542632018000000528";
	    access_token="44444444444444444444444444444444";
	    gender="f";
	}else if($('#virtual-user').val()=='5'){
		id=5;
	    nickname="송혜지";
	    profile_image="https://t1.daumcdn.net/qna/image/1542632018000000528";
	    access_token="55555555555555555555555555555555";
	    gender="f";
	}else if($('#virtual-user').val()=='6'){
		id=6;
	    nickname="황규준";
	    profile_image="https://t1.daumcdn.net/qna/image/1542632018000000528";
	    access_token="66666666666666666666666666666666";
	    gender="m";
	}else if($('#virtual-user').val()=='7'){
		id=7;
	    nickname="이성욱";
	    profile_image="https://t1.daumcdn.net/qna/image/1542632018000000528";
	    access_token="77777777777777777777777777777777";
	    gender="m";
	}else if($('#virtual-user').val()=='9'){
		id=9;
	    nickname="함동희";
	    profile_image="https://t1.daumcdn.net/qna/image/1542632018000000528";
	    access_token="CEi_81tlprNuwFd-IJ4y8Wjs1qSsyDhg55uMiwo9dqQAAAFukSY8cg";
	    gender="m";
	}
//	app.get(host+'join',{access_token:access_token},function(res){
	app.get(host+'join',{id:id,nickname:nickname,profile_image:profile_image,access_token:access_token,gender:gender},function(res){
		if(res.rc>0){
			console.log("join success - res : "+res);
			window.localStorage.setItem("id",id );
			window.localStorage.setItem("nickname",nickname );
			window.localStorage.setItem("profile_image", profile_image);
			window.localStorage.setItem("access_token", access_token);
			window.localStorage.setItem("gender", gender);
			window.localStorage.setItem("l_token", res.result);
			location.href="#home";
		}else{
			console.log("join fail : "+res.resMessage);
			alert("join fail : "+res.resMessage);
		}
	});
	console.log('탭이벤트 종료');
});
   
$('#home').on('pagebeforeshow',function(event){
	console.log('pagebeforeshow 발생 in #home');
	
	if(ws==null)chat_openSocket();
});


// ****************** 채팅 모듈 ********************** 
var ws;
var messages=document.getElementById("chat-area-all");

function chat_openSocket(){
	console.log('chat_openSocket()');
    if(ws!==undefined && ws.readyState!==WebSocket.CLOSED){
        chat_writeResponse("WebSocket is already opened.");
        return;
    }
    
    //웹소켓 객체 만드는 코드
    //ws=new WebSocket(wshost+JSON.stringify(params));
    ws=new WebSocket(wshost+"?"+window.localStorage.getItem("l_token"));
    
    ws.onopen=function(event){
        if(event.data===undefined) return;
        console.log('onopen(event) : ',event);
        chat_writeResponse(event.data);
    };
    ws.onmessage=function(event){
    	console.log('onmessage(event) : ',event);
    	chat_writeResponse(JSON.parse(event.data));
    };
    ws.onclose=function(event){
    	chat_writeResponse("Connection closed");
    }
}

function chat_send(form){
    //var text=document.getElementById("all-chat-input-text").value+","+window.localStorage.getItem("nickname");
    console.log("chatInputText.value : ("+form.chatInputText.value+")");
    if(form.chatInputText.value=="" ){
    	console.log("글이 없음");
    	return false;
    }
    var message={
    		message:form.chatInputText.value,
    		scope:form.chatRoomScope.value,
    		nickname:window.localStorage.getItem("nickname"),
    		imagepath:window.localStorage.getItem("profile-image")
    }
    ws.send(JSON.stringify(message));
    text="";
    var messageHtml='<div class="my-message">'
    	+'<div class="my-text">'
			+'<div>'+message.message+'</div>'
    	+'</div>'
    +'</div>'
    form.chatInputText.value="";
    document.getElementById("chat-area-"+message.scope).innerHTML+=messageHtml;
    $('#chat-area-all').scrollTop(99999999);
}

function chat_closeSocket(){
    ws.close();
}
function chat_writeResponse(data){
    console.log("data : ",data);
	if(data.scope=="news"){
		$('#chat-area-all-num').html(data.num);
		var messageHtml='<div class="news-massage">'
					+'<div class="news-text">'+data.message+'</div>'
					+'</div>';
		document.getElementById("chat-area-all").innerHTML+=messageHtml;
	}else{
		var scope=data.scope;
	    var message=data.message;
	    var sender=data.nickname;
	    var imagepath=data.imagepath==null?defaultImage:data.imagepath;
	    
	    console.log('scope'+scope+' | message'+message+' | sender'+sender+' | imagepath'+imagepath);
	    var messageHtml='<div class="someones-message">'
	    	+'<div class="someones-image"><img src="'+imagepath+'"></div>'
	    	+'<div class="someones-text">'
				+'<div class="sender">'+sender+'</div>'
				+'<div class="text">'+message+'</div>'
	    	+'</div>'
	    +'</div>';

	    document.getElementById("chat-area-"+scope).innerHTML+=messageHtml;
	}
	$('#chat-area-all').scrollTop(99999999);
}

var url = "http://codenamu.org/blog/";
// ************** 버스정보 ***************
$('#busstop').on('pagebeforeshow',function(event){
	console.log('pagebeforeshow 발생 in #busstop');
	
	drowBus();
});
function drowBus(){
	$.mobile.loading('show');
	getBus(function(busList){
		console.log('busList:',busList);
		$('#bus10_1-first-time').html(busList[0].predictTime1);
		$('#bus10_1-first-loc').html(busList[0].locationNo1);
		$('#bus10_1-second-time').html(busList[0].predictTime2);
		$('#bus10_1-second-loc').html(busList[0].locationNo2);
		$('#bus10_2-first-time').html(busList[1].predictTime1);
		$('#bus10_2-first-loc').html(busList[1].locationNo1);
		$('#bus10_2-second-time').html(busList[1].predictTime2);
		$('#bus10_2-second-loc').html(busList[1].locationNo2);
		
		if(busList[0].predictTime1 ==""){
			$('#bus10_1-first').html('도착예정정보 없음');			
		}
		if(busList[0].predictTime2 ==""){
			$('#bus10_1-second').html('도착예정정보 없음');			
		}
		if(busList[1].predictTime1 ==""){
			$('#bus10_2-first').html('도착예정정보 없음');			
		}
		if(busList[1].predictTime2 ==""){
			$('#bus10_2-second').html('도착예정정보 없음');			
		}
		$.mobile.loading( 'hide');
	});
	
	
}
function getBus(cb){ //bus[0]=10-1, bus[1]=10-2
	var url='http://www.gbis.go.kr/gbis2014/schBusAPI.action?cmd=searchBusStationJson&stationId=208000128';
	
	var bus=new Array(2);
	$.get(url,function(res){
		console.log('res:',res);
		var resList=res.result.busArrivalInfo;
		
		for(var i=0;i<resList.length;i++){
			if(resList[i].routeName=='10-1'){
				console.log("10-1: ",resList[i]);
				bus[0]=resList[i];
			}
			if(resList[i].routeName=='10-2'){
				console.log("10-2: ",resList[i]);
				bus[1]=resList[i];
			}
		}
		cb(bus);
	});
	
}
function getBusId(name){
	var url = 'http://openapi.gbis.go.kr/ws/rest/busrouteservice'; /*URL*/
	var queryParams = '?' + encodeURIComponent('ServiceKey') + '='+encodeURIComponent(busDataKey); /*Service Key*/
	queryParams += '&' + encodeURIComponent('keyword') + '=' + encodeURIComponent(name); /*노선 ID*/
	$.get(url+queryParams, function(data, textStatus, req) {
		console.log('data:',data);
		console.log('textStatus:',textStatus);
		console.log('req:',req);
	});
}

$('.make_room_button').tap(function(event){
	console.log("버튼 눌림");
	$.mobile.changePage("#make_room_dialog", { role: "dialog" } );
});
// 실시간 버스 위치 http://www.gbis.go.kr/gbis2014/schBusAPI.action?cmd=searchRouteJson&routeId=241249002
</script>
</html>

