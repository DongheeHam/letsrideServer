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
		<div data-role="content" style="background-image: url(/res/img/loading1.jpg);height:100%;width:auto;">
			<div id="letsride">Let's Ride2</div>
			<div id="logo">로딩중입니다.</div>
		</div>
	</section>
	<section data-role="page" id="login">
		
			
			<!-- <div style="background-image: url(/res/img/ride.jpg);">
				<a id="kakaoLogin" data-role="button" data-icon="comment"
					data-iconpos="bottom"> 카카오톡 로그인 </a> <input type="text"
					id="virtual-user">
			</div> -->
			
		<div data-role="content" style="background-image: url(/res/img/loading2.jpg);height:100%;width:auto;">
			<div id="letsride">Let's Ride</div>
			<a id="kakaoLogin" data-role="button" data-icon="comment"
					data-iconpos="bottom"> 카카오톡 로그인 </a> <input type="text"
					id="virtual-user">
		</div>
	</section>
	<!---------------------------홈화면------------------------->
	<section data-role="page" id="home">
		<div data-role="header" data-tap-toggle="false" data-position="fixed">


			<h1 class="head">Let's Ride</h1>
			<a href="#homeMenuPanel" data-icon="bars" data-iconpos="notext">Add</a>
		</div>
		<div data-role="content">
			<div id="homeimg">
				<div id="">Let’s ride는 경제적인 소비습관을 지향합니다.</div>
				<!-- <a data-role="button" href="#mappage">지 도</a> -->
			</div>
			<br>
			<div id="all-chat-room">
				<div>
					전체 채팅방 (<span id="chat-area-num-all">0</span>명)
				</div>

				<div class="chat-area " id="chat-area-all"></div>
				<div id="chat-form">
					<form onsubmit="return false;">
						<div class="ui-grid-a">
							<div class="ui-block-a" style="width: 80%;">
								<textarea class="chatInputText" style="resize: none"
									name="chatInputText"
									onkeydown="JavaScript:Enter_Check(this.form);"></textarea>
							</div>
							<div class="ui-block-b" style="width: 20%">
								<input type="button" data-mini="true"
									onclick="chat_send(this.form)" value="전송">
							</div>
						</div>
						<input type="hidden" name="chatRoomScope" class="chatRoomScope" value="all">
					</form>
				</div>
			</div>
		</div>
		<div data-role="footer" data-tap-toggle="false" data-position="fixed" >
			<div data-role="navbar">
				<ul>
					<li><a href="#room" data-icon="grid">방목록</a></li>
					<li><a href="#home" data-icon="home">홈</a></li>
					<li><a href="#busstop" data-icon="gear">버스 정보</a></li>
				</ul>
			</div>
		</div>
		<div data-role="panel" id="homeMenuPanel" data-position="right"
			data-display="overlay">
			<h3>Menu</h3>
			<ul data-role="listview">
				<li><a href="#room">설정 메뉴</a></li>
				<li data-icon="power"><a id="kakaoLogout" data-role="button"
					data-icon="comment"> 로그아웃 </a></li>
				<li data-icon="power"><a id="byForever" data-role="button"
				data-icon="comment"> 회원탈퇴 </a></li>
			</ul>
		</div>
		<div data-role="popup" id="today-form" style="padding: 30px;"
			data-overlay-theme="b" class="ui-corner-all">
			<div>오늘의 의상을 한줄로 표현해주세요!</div>
			<textarea rows="" cols="" id="today-text"></textarea>
			<div class="ui-grid-a" data-mini="true">
				<div class="ui-block-a">
					<a data-role="button" data-rel="back" >귀찮아요</a>
				</div>
				<div class="ui-block-b">
					<a data-role="button" data-rel="back" >확인</a>
				</div>
			</div>
		</div>
	</section>
	<!----------------------------------방목록--------------------------------->
	<section data-role="page" id="room">
		<div data-role="header" data-tap-toggle="false" data-position="fixed">
			<a href="javascript:history.back()" data-icon="back"
				data-iconpos="notext"></a>
			<h1 class="head">Let's Ride</h1>
			<a href="#roomMenuPanel" data-icon="bars" data-iconpos="notext">Add</a>
		</div>
		<div data-role="content">
			<div class="ui-bar ui-bar-a my">내가 만든 방</div>
			<div class="my-room-area"></div>

			<div class="ui-bar ui-bar-a attended">내가 포함된 방</div>
			<div class="attended-room-area"></div>

			<div class="ui-bar ui-bar-a all">모든 방</div>
			<div class="all-room-area"></div>

			<a data-role="button"
				style="position: fixed; bottom: 10%; right: 5%; border-radius: 10%;"
				data-rel="popup" href="#roommake" class="make_room_button" data-position-to="window">
				방 만들기</a>
			<div data-role="footer" data-tap-toggle="false" data-position="fixed">
				<div data-role="navbar">
					<ul>
						<li><a href="#room" data-icon="grid">방목록</a></li>
						<li><a href="#home" data-icon="home">홈</a></li>
						<li><a href="#busstop" data-icon="gear">버스 정보</a></li>
					</ul>
				</div>
			</div>
		</div>
		<div data-role="panel" id="roomMenuPanel" data-position="right"
			data-display="overlay">
			<h3>Menu</h3>
			<ul data-role="listview">
				<li><a href="#room">설정 메뉴</a></li>
				<li data-icon="power"><a id="kakaoLogout" data-role="button"
					data-icon="comment"> 로그아웃 </a></li>
				<li data-icon="power"><a id="byForever" data-role="button"
				data-icon="comment"> 회원탈퇴 </a></li>
			</ul>
		</div>
		<div data-role="popup" id="room_delete_check" style="padding: 30px;"
			data-overlay-theme="b" class="ui-corner-all">
			<div>이미 만든 방이 있습니다. 기존 방을 삭제해주세요.</div>
			<div class="ui-grid-a" data-mini="true">
				<div class="ui-block-a"></div>
				<div class="ui-block-b">
					<a data-role="button" data-rel="back" >확인</a>
				</div>
			</div>
		</div>
		<div data-role="popup" id="roommake" style="padding: 30px; width:250px;"
			data-overlay-theme="b"  data-position-to="window" class="ui-corner-all">
			<form id="roommake-form">
				<label for="rn">방제목</label> 
				<input type="text" name="rn" id="rn">
				<fieldset data-role="controlgroup" data-type="horizontal">
					<legend class="ft">성별</legend>
					<input type="radio" data-mini="true" name="gender" value="m" id="gender1">
					<label for="gender1">남자만</label>
					<input type="radio" data-mini="true" name="gender" value="f" id="gender2"> 
					<label for="gender2">여자만</label>
					<input type="radio" data-mini="true" checked="checked" value="a" name="gender" id="gender3"> 
					<label for="gender3">성별무관</label>
				</fieldset>
				<fieldset data-role="controlgroup" data-type="horizontal">
					<legend class="ft">도착지</legend>
					<input type="radio" data-mini="true" name="destination" value="정문" id="destination1">
					<label for="destination1">정문</label> 
					<input type="radio" data-mini="true" name="destination" value="중생관" id="destination2"> 
					<label for="destination2">중생관</label> 
					<input type="radio" data-mini="true" checked="checked" name="destination" value="성결관" id="destination3"> 
					<label for="destination3">성결관</label>
				</fieldset>
				<div class="ui-grid-a" data-mini="true">
					<div class="ui-block-a">
						<input type="range" class="leave-schedule"
							id="leave-schedule-hour" data-highlight="true" value="9" min="0"
							max="23">
					</div>
					<div class="ui-block-b">
						<input type="range" class="leave-schedule"
							id="leave-schedule-minute" data-highlight="true" value="0"
							min="0" max="59">
					</div>
				</div>
				<div id="leave-schedule-text">약 9시 0분 출발예정</div>

				<div class="ui-grid-a" data-mini="true">
					<div class="ui-block-a">
						<a data-mini="true" data-role="button"  data-rel="back">취소</a>
					</div>
					<div class="ui-block-b">
						<a data-mini="true" data-role="button" onclick="makeRoom(this.form)" data-rel="back">확인</a>
					</div>
				</div>
			</form>
		</div>
	</section>
	<!-- ---------------------------------내가 만든 채팅방------------------------------- -->
	<section data-role="page" id="my-chat-room" style="background: #b2c7d9;">
		<div data-role="header" data-tap-toggle="false" data-position="fixed">
			<a href="javascript:history.back()" data-icon="back"
				data-iconpos="notext"></a>
			<h1 class="head" id="my-chat-room-header"></h1>
			<a href="#myRoomMenuPanel" data-icon="bars" data-iconpos="notext">Add</a>
		</div>
		<div data-role="content" style="padding:0">
			<!-- <div style="position: fixed; top: 10%; right: 5%; border-radius: 10%;">
				<span class="chat-room-num">0</span>명 접속중
			</div> -->
			<div class="my-chat-area room-chat-area"></div>
			
			
		</div>
		<div data-role="footer" data-tap-toggle="false" data-position="fixed">
			<div id="chat-form">
				<form onsubmit="return false;">
					<div class="ui-grid-a">
						<div class="ui-block-a" style="width: 80%;">
							<textarea class="chatInputText" style="resize: none"
								name="chatInputText"
								onkeydown="JavaScript:Enter_Check(this.form);"></textarea>
						</div>
						<div class="ui-block-b" style="width: 20%">
							<input type="button" data-mini="true"
								onclick="chat_send(this.form)" value="전송">
						</div>
					</div>
					<input type="hidden" name="chatRoomScope" class="myChatRoomScope" value="">
				</form>
			</div>
		</div>
		
		<div data-role="panel" id="myRoomMenuPanel" data-position="right"
			data-display="overlay">
			
		</div>
		<div data-role="popup" id="room_delete_check" style="padding: 30px;"
			data-overlay-theme="b" class="ui-corner-all">
			
		</div>
		
	</section>
	<!-- ---------------------------------내가 참여한 채팅방------------------------------- -->
	<section data-role="page" id="attended-chat-room" style="background: #b2c7d9;">
		<div data-role="header" data-tap-toggle="false" data-position="fixed">
			<a href="javascript:history.back()" data-icon="back"
				data-iconpos="notext"></a>
			<h1 class="head" id="attended-chat-room-header"></h1>
			<a href="#attendedRoomMenuPanel" data-icon="bars" data-iconpos="notext">Add</a>
		</div>
		<div data-role="content" style="padding:0">
			<!-- <div style="position: fixed; top: 10%; right: 5%; border-radius: 10%;">
				<span class="chat-room-num">0</span>명 접속중
			</div> -->
			<div class="attended-chat-area room-chat-area"></div>
		</div>
		<div data-role="footer" data-tap-toggle="false" data-position="fixed">
			<div id="chat-form">
				<form onsubmit="return false;">
					<div class="ui-grid-a">
						<div class="ui-block-a" style="width: 80%;">
							<textarea class="chatInputText" style="resize: none"
								name="chatInputText"
								onkeydown="JavaScript:Enter_Check(this.form);"></textarea>
						</div>
						<div class="ui-block-b" style="width: 20%">
							<input type="button" data-mini="true"
								onclick="chat_send(this.form)" value="전송">
						</div>
					</div>
					<input type="hidden" name="chatRoomScope" class="attendedChatRoomScope" value="">
				</form>
			</div>
		</div>
		<div data-role="panel" id="attendedRoomMenuPanel" data-position="right"
			data-display="overlay">
			<h3>Menu</h3>
			<ul data-role="listview" id="appended-room-panel" >
				
			</ul>
		</div>
		
		<div data-role="popup" id="room_delete_check" style="padding: 30px;"
			data-overlay-theme="b" class="ui-corner-all">
			
		</div>
		
	</section>
	<!---------------------------------------------------------------버스정보------------------------------------------------------------------------------------->
	<section data-role="page" id="busstop">
		<div data-role="header" data-tap-toggle="false"  
			data-position="fixed">
			<a href="javascript:history.back()" data-icon="back"
				data-iconpos="notext"></a> <a href="#busMenuPanel" data-icon="bars"
				data-iconpos="notext">Add</a>
			<h1 class="head">Let's Ride</h1>
		</div>
		<div data-role="content">

			<div data-role="collapsible" data-collapsed="false"
				data-inset="false">
				<h3>명학역 마을버스</h3>
				<a onClick="drowBus()" style="float: right" data-role="button"
					data-icon="refresh" data-iconpos="notext"></a>
				<div>
					<p>10-1</p>
					<div>
						<p>
							1번째 : <span id="bus10_1-first"> <span
								id="bus10_1-first-time"></span>분 후 (<span id="bus10_1-first-loc"></span>번째
								전)
							</span>
						</p>
						<p>
							2번째 : <span id="bus10_1-second"> <span
								id="bus10_1-second-time"></span>분 후 (<span
								id="bus10_1-second-loc"></span>번째 전)
							</span>
						</p>
					</div>
					<p>10-2</p>
					<div>
						<p>
							1번째 : <span id="bus10_2-first"> <span
								id="bus10_2-first-time"></span>분 후 (<span id="bus10_2-first-loc"></span>번째
								전)
							</span>
						</p>
						<p>
							2번째 : <span id="bus10_2-second"> <span
								id="bus10_2-second-time"></span>분 후 (<span
								id="bus10_2-second-loc"></span>번째 전)
							</span>
						</p>
					</div>
				</div>
			</div>

			<div data-role="collapsible" data-collapsed="false"
				data-inset="false">
				<h3>통학버스 운행시간표</h3>
				<p>명학역 => 성결대학교</p>
				<div>
					<p>
						<span class="red">08:00 ~ 10:50</span> 매시 10분 간격 운행
					</p>
					<p>
						<span class="red">12:30 ~ 13:30</span> 매시 10분 간격 운행
					</p>
					<p>
						<span class="red">16:10 ~ 18:00</span> 매시 10분 간격 운행
					</p>
				</div>
				<p>성결대학교 => 명학역</p> 
				<div>
					<p>
						<span class="red">12:20 ~ 13:20</span> 매시 10분 간격 운행
					</p>
					<p>
						<span class="red">16:00 ~ 17:50</span> 매시 10분 간격 운행
					</p>
					<p>
						<span class="red">21:20, 21:40 (2회)</span> 성결관 앞
					</p>
				</div>
			</div>
		</div>
		<div data-role="footer" data-tap-toggle="false" data-position="fixed"
			 >
			<div data-role="navbar">
				<ul>
					<li><a href="#room" data-icon="grid">방목록</a></li>
					<li><a href="#home" data-icon="home">홈</a></li>
					<li><a href="#busstop" data-icon="gear">버스정보</a></li>
				</ul>
			</div>
		</div>
		<div data-role="panel" id="busMenuPanel" data-position="right"
			data-display="overlay">
			<h3>Menu</h3>
			<ul data-role="listview">
				<li><a href="#room">설정 메뉴</a></li>
				<li data-icon="power"><a id="kakaoLogout" data-role="button"
					data-icon="comment"> 로그아웃 </a></li>
				<li data-icon="power"><a id="byForever" data-role="button"
				data-icon="comment"> 회원탈퇴 </a></li>
			</ul>
		</div>
	</section>
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=ad4e3b960536dab102751f783b0d0ceb"></script>
	<section data-role="page" id="mappage">
		<div id="map" style="width:400px;height:500px;"></div>
	
	</section>
</body>
<script>
//var host='http://localhost:7070/';
//var wshost='ws://localhost:7070/echo.do';
var host='https://letsride.donghee.site/';
var wshost='wss://letsride.donghee.site/echo.do';
var defaultImage='/res/img/defaultImage.jpg';


var sessionId;

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
	$('.rm').change(function(){
		console.log("change이벤트");
		var str="약 "+$('#leave-schedule-hour').val()+"시 "+$('#leave-schedule-minute').val()+"분 출발예정";
		$('#leave-schedule-text').html(str);
	});
	$.mobile.loading( "show", {
        text: 'LetsRide!!\nloading...',
        textVisible: true,
        theme: theme,
        textonly: false
	});
/* 	var container = document.getElementById('map');
	var options = {
		center: new kakao.maps.LatLng(37.385483, 126.935436),
		level: 3
	};

	var map = new kakao.maps.Map(container, options); */
	/* var head_height=$('[data-role=header]').outerHeight();
	var footer_height=$('[data-role=footer]').outerHeight();
	
	var content_height= $(document).height() - head_height - footer_height -50;
	console.log("head_height:"+head_height+"|footer_height:"+footer_height+"|content_height:"+content_height);
	$('[data-role=content]').css('max-height', content_height+'px'); */
	 
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
	/* setTimeout(function() {
		$("#today-form").popup("open"); 
	}, 1000) */
	if(ws==null)chat_openSocket();
});


// ****************** 채팅 모듈 ********************** 
var ws;
var messages=document.getElementById("chat-area-all");

function chat_openSocket(){
	console.log('chat_openSocket()');
    if(ws!==undefined && ws.readyState!==WebSocket.CLOSED){
        //chat_writeResponse("WebSocket is already opened.");
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
    	//chat_writeResponse("Connection closed");
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
    $('#chat-area-'+message.scope).scrollTop(99999999);
}

function chat_closeSocket(){
    ws.close();
}
function chat_writeResponse(data){
    console.log("data : ",data);
    if(data.sessionId) {
    	sessionId=data.sessionId;
    	return false;
    }
    if(data.deletedRoom){
    	alert(data.deletedRoom+"방이 방장에 의해 제거되었습니다.");
    	attendedRoom=undefined;
    	$.mobile.changePage("#room", {allowSamePageTransition: true,
	        transition: 'none'});
    	return false;
    }
	if(data.news=="news"){
		$('#chat-area-num-'+data.scope).html(data.num);
		var messageHtml='<div class="news-massage">'
					+'<div class="news-text">'+data.message+'</div>'
					+'</div>';
		document.getElementById("chat-area-"+data.scope).innerHTML+=messageHtml;
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
	$('#chat-area-'+data.scope).scrollTop(99999999);
}

//var url = "http://codenamu.org/blog/";
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


// 실시간 버스 위치 http://www.gbis.go.kr/gbis2014/schBusAPI.action?cmd=searchRouteJson&routeId=241249002
		
// ********** 방관련**************************************************************************************

var myRoom;
var attendedRoom;

$('#room').on('pagebeforeshow',function(event){
	console.log('pagebeforeshow 발생 in #room');
	$('.make_room_button').attr('href',myRoom?'#room_delete_check':'#roommake');
	$.post(host+"getRoomList.json",{l_token:window.localStorage.getItem("l_token")},function(res){
		$('.my-room-area').html('');
		$('.attended-room-area').html('');
		$('.all-room-area').html('');
		console.log("res:",res);
		if(res.rc<0) alert("토큰이 유효하지 않습니다.");
		var my=res.result.my;
		var attended=res.result.attended;
		var all=res.result.all;
		
		var myHtml,attendedHtml,allHtml;
		if(my==null){
			myHtml=$('<div>내가 만든 방이 없습니다.</div>').addClass('ui-body ui-body-a');
		}else{
			var myGender=convGender(my.gender);
			var myNum=$('<span>('+(Number(my.num)+1)+'/4)</span>').addClass('roomnum');
			if(my.num==3){
				myNum.css('color','red');
			}
			var myHtmlItem=$('<div></div>').addClass('go-room').data('rno',my.rno)
						.css('float','left').css('width','90%')
						.append($('<div></div>').addClass('roomtitle')
								.html(my.title).append(myNum))
						.append($('<div></div>').addClass('roominfo')
								.html(myGender+' | '+my.destination+' | '+my.schedule));
			var myHtmlIcon=$('<a></a>')
			.addClass('ui-btn ui-icon-delete ui-btn-icon-notext ui-corner-all delete-my-room')
			.css('float','right').css('max-width','10%');
			myHtml=$('<div></div>').addClass('ui-body ui-body-a').append(myHtmlItem).append(myHtmlIcon);
		}
		
		if(attended==null){
			attendedHtml=$('<div>내가 만든 방이 없습니다.</div>').addClass('ui-body ui-body-a');
		}else{
			var attendedGender=convGender(attended.gender);
			var attendedNum=$(' <span>('+(Number(attended.num)+1)+'/4)</span>').addClass('roomnum');
			if(attended.num==3){
				attendedNum.css('color','red');
			}
			var attendedHtmlItem=$('<div></div>').addClass('go-room').data('rno',attended.rno)
						.css('float','left').css('width','90%')
						.append($('<div></div>').addClass('roomtitle')
								.html(attended.title).append(attendedNum))
						.append($('<div></div>').addClass('roominfo')
								.html(attendedGender+' | '+attended.destination+' | '+attended.schedule));
			var attendedHtmlIcon=$('<a></a>')
			.addClass('ui-btn ui-icon-delete ui-btn-icon-notext ui-corner-all go-out-room')
			.css('float','right').css('max-width','10%');
			attendedHtml=$('<div></div>').addClass('ui-body ui-body-a').append(attendedHtmlItem).append(attendedHtmlIcon);
		}
		
		if(all==null){
			allHtml=$('<div>아직 만들어진 방이 없네요. 먼저 만들어 보세요.</div>');
			$('.all-room-area').html(allHtml);
		}else{
			for(var i=0;i<all.length;i++){
				console.log("all i:"+i);
				var allGender=convGender(all[i].gender);
				var allNum=$(' <span>('+(Number(all[i].num)+1)+'/4)</span>').addClass('roomnum');
				if(all[i].num==3){
					allNum.css('color','red');
				}
				allHtml=$('<div></div>').addClass('ui-body ui-body-a go-room').data('rno',all[i].rno)
				.append($('<div></div>').addClass('roomtitle')
						.html(all[i].title).append(allNum))
				.append($('<div></div>').addClass('roominfo')
						.html(allGender+' | '+all[i].destination+' | '+all[i].schedule));
				$('.all-room-area').append(allHtml);
			}
		}
		$('.my-room-area').append(myHtml);
		$('.attended-room-area').append(attendedHtml);
		
		$('.go-room').on('click',function(){
			var tempRno=$(this).data('rno');
			if(attendedRoom==undefined){ //go attendedroom
				if(myRoom&&tempRno==myRoom.rno){
					$.mobile.changePage('#my-chat-room',{});
				}else{  //방접속
					inroom(tempRno);
				}
			}else{
				if(tempRno==attendedRoom.rno){ //go attendedroom
					$.mobile.changePage('#attended-chat-room',{});
				}else if(myRoom&&tempRno==myRoom.rno){
					$.mobile.changePage('#my-chat-room',{});
				}else{// 방바꿀래?
					if(confirm(attendedRoom.title+'방에 접속중입니다. 기존 방을 나가고 새로운 방에 입장하시겠어요?')){
						inroom(tempRno);
					}
				}
			}
		});
		$('.go-out-room').on('click',function(){
			if(!confirm(attendedRoom.title+"에서 나가시겠어요?"))return false;
			var params={
				l_token:window.localStorage.getItem("l_token"),
				sessionId:sessionId,
				rno:attendedRoom.rno
			}
			$.post(host+'goOutRoom.json',params,function(res){
				if(res.rc>0){
					$.mobile.changePage('#room', {
				        allowSamePageTransition: true,
				        transition: 'none'
				    });
				}else{
					alert(res.resMessage);
					$.mobile.changePage('#room', {
				        allowSamePageTransition: true,
				        transition: 'none'
				    });
				}
			});
			attendedRoom=undefined;
		});
		$('.delete-my-room').on('click',function(){
			if(!confirm(myRoom.title+"을 삭제하시겠어요?"))return false;
			var params={
				l_token:window.localStorage.getItem("l_token"),
				sessionId:sessionId,
				rno:myRoom.rno
			}
			$.post(host+'deleteMyRoom.json',params,function(res){
				console.log('deleteMyRoom.json res:',res);
				if(res.rc>0){
					
					$.mobile.changePage('#room', {
				        allowSamePageTransition: true,
				        transition: 'none'
				    });
				}else{
					alert(res.resMessage);
					$.mobile.changePage('#room', {
				        allowSamePageTransition: true,
				        transition: 'none'
				    });
				}
			});
			myRoom=undefined;
		});
	});
});
$("#myRoomMenuPanel").on("panelbeforeopen",function(){
	var panel=$(this);
	panel.html('');
	panel.append($('<h3>'+myRoom.title+'</h3>'));
	panel.append($('<div></div>')
			.append($('<div class="someones-image"></div>').html('<img src="'+window.localStorage.getItem("profile_image")+'">'))
			.append($('<div class="someones-text"></div>').html(window.localStorage.getItem('nickname'))));
	var params={
		l_token:window.localStorage.getItem("l_token"),
		rno:myRoom.rno
	}
	$.post(host+'getUserInfoThisRoom.json',params,function(res){
		console.log('res',res);
		if(res.rc>0){
			var div=$('<div></div>')
			for(var i=0;i<res.result.length;i++){
				div.append($('<div class="someones-image"></div>').html('<img src="'+res.result[i].imagepath+'">'));
				div.append($('<div class="someones-text"></div>').html(res.result[i].nickname));
			}
			panel.append(div);
		}else{
			alert(res.resMessage);
		}
	});
});
function inroom(rno){
	var params={
		l_token:window.localStorage.getItem("l_token"),
		sessionId:sessionId,
		rno:rno
	}
	$.post(host+'goInRoom.json',params,function(res){
		if(res.rc<0){
			alert(res.resMessage);
			return false;
		}else{
			attendedRoom=res.result;
			$('#attended-chat-room-header').html(attendedRoom.title);
			$('.attended-chat-area').attr('id','chat-area-'+attendedRoom.rno);
			$('.attendedChatRoomScope').val(attendedRoom.rno);
			//$('.chat-room-num').attr('id','chat-area-num-'+attendedRoom.rno);
			$.mobile.changePage('#attended-chat-room',{});
		}
	});
	
}

function convGender(gender){
	if(gender=='f'){
		return '여자만';
	}else if(gender=='m'){
		return '남자만';
	}else{
		return '성별 무관';
	}
}
function makeRoom(){
	var form=document.getElementById('roommake-form');
	console.log('form:',form);
	var a="====================\n";
	a+="maker : "+window.localStorage.getItem("l_token")+"\n";
	a+="제목 : "+form.rn.value+"\n";
	a+="성별 : "+form.gender.value+"\n";
	a+="도착지 : "+form.destination.value+"\n";
	a+="예상출발 : "+$('#leave-schedule-text').html()+"\n";
	
	console.log(a);
	
	var params={
		l_token:window.localStorage.getItem("l_token"),
		title:form.rn.value,
		gender:form.gender.value,
		destination:form.destination.value,
		schedule:$('#leave-schedule-text').html(),
		sessionId:sessionId,
	}
	
	$.post(host+'makeMyRoom.json',params,function(res){
		console.log(res);
		if(res.rc<0){
			alert(res.resMessage);
			return false;
		}else{
			myRoom=res.result;
			$('#my-chat-room-header').html(params.title);
			$('.my-chat-area').attr('id','chat-area-'+myRoom.rno);
			$('.myChatRoomScope').val(myRoom.rno);
			//$('.chat-room-num').attr('id','chat-area-num-'+myRoom.rno);
			$.mobile.changePage("#my-chat-room", {});
		}
		
	});
}

</script>
</html>

