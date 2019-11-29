<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%@
    taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%
%>
<!-- <section>
<a href="https://kauth.kakao.com/oauth/authorize?client_id=f2718ce94cd09ac1b864827d1578caee&redirect_uri=http://localhost:7070/kakao/code.do&response_type=code">
<img alt="카카오로그인" src="../res/img/kakao_account_login_btn_large_wide.png" width="70%" height="20%">
</a>
<form>
	이름 : <input type="text" name="name">
	<input type="button" value="채팅방가기" onclick="javascript:app.go('/chatRoom',{name:this.form.name.value})">
</form>

</section> -->


<div data-role="content">
    <div id="homeimg">
        <p id="homeimgtext">같이타</p>
        <br><p id="homeimgtext2">명학역 택시 동승 애플리케이션</p>
    </div><br>
    <div id="noticeboard">
        <p id="hroom">실시간 방목록</p>
        <input  type="text" placeholder="search">
    </div><br>
    <div id="ad">
        <img src="/res/img/ad.PNG" width="100%" height="70px;">
    </div>
    <div class="refresh"><img src="/res/img/re50.png"></div>
</div>

