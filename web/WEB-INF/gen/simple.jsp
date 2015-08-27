<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${session.locale}"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="title" bundle="${page.bundle}"/></title>
        <link rel='stylesheet' type='text/css' href='../css/stylesheet.css'>
        <c:import url="/WEB-INF/common/analytics.jsp"/>
    </head>
    <body id="${page.name}" class="simple">
        <div id="wrapper">
            <div id="content">
                <c:import url="/WEB-INF/common/menu.jsp"/>
                ${page.texts['content']}
            </div>
            <c:import url="/WEB-INF/common/footer.jsp"/>
        </div>
        <c:import url="/WEB-INF/common/scripts.jsp"/>
        <c:import url="/WEB-INF/common/inplace.jsp"/>
    </body>
</html>
