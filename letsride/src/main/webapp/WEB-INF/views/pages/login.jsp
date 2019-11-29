<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div data-role="content">
   <p id="letsride">Let's Ride</p>
   <div id="logo">
       <a id="kakao-login-btn" data-transition="pop" data-role="button" data-icon="comment" data-iconpos="bottom">
              	카카오톡 로그인
           </a>
   	</div>    
</div>
<script>
$(document).ready(function(){
	Kakao.init('ad4e3b960536dab102751f783b0d0ceb');
    // 카카오 로그인 버튼을 생성합니다.
    Kakao.Auth.createLoginButton({
      container: '#kakao-login-btn',
      success: function(authObj) {
        alert(JSON.stringify(authObj));
      },
      fail: function(err) {
         alert(JSON.stringify(err));
      }
    });
})
$('#kakaoLogin').on('click',function(){
	/* app.loadInDiv('#home','/home'); */
	console.log('눌림')
})
</script>
