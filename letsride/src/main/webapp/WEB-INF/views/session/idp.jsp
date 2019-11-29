<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%@
    taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%
%><session>
<%-- <form name="acsForm" action="${ ssoURL }" method="post" > --%>
<%-- <textarea name="SAMLRequest" style="visibility: hidden;">${ SAMLRequest }</textarea > --%>
<%-- <textarea name="RelayState"  style="visibility: hidden;">${ RelayState }</textarea > --%>
<!-- </form> -->
<script type="text/javascript">
document.location='${redirectURL}';
//document.acsForm.submit();
</script>
</session>