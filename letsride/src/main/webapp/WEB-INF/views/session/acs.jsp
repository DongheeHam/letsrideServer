<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%@
    taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%
%><html>
<body onload="autoSubmit();">	
<!-- RelayState로 이동. -->
<form name="loginform" method="post" action="${RelayState}">
	<input type="hidden" name="ssoId" value="${ssoId}">
	<c:forEach var="param" items="${params}">
		<input type="hidden" name="${param.key}" value="${param.value}">
	</c:forEach>	
</form>
<script type="text/javascript">
function autoSubmit() {
	document.loginform.submit();
}
</script>
</body>
</html>