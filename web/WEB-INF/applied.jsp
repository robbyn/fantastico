<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${session.locale}"/>
<c:set var="user" value="${helper.user}"/>
<c:set var="event" value="${page.event}"/>
<c:set var="relationship" value="any"/>
<c:if test="${not empty event.relationship}">
    <c:set var="relationship" value="${event.relationship.name}"/>
</c:if>
<c:if test="${not empty event.language}">
    <c:set var="language" value="${event.language.code}"/>
</c:if>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="title" bundle="${page.bundle}"/></title>
        <link rel='stylesheet' type='text/css' href='../css/stylesheet.css'>
        <c:import url="/WEB-INF/common/analytics.jsp"/>
    </head>
    <body id="${page.name}">
        <div id="wrapper">
            <h1><fmt:message key="title" bundle="${page.bundle}"/></h1>
            <div id="content">
                <c:import url="/WEB-INF/common/menu.jsp"/>
                ${page.texts['top']}
<c:choose>
    <c:when test="${not empty user.photo}">
                ${page.texts['has-photo']}
    </c:when>
    <c:otherwise>
                ${page.texts['no-photo']}
        <form action="changePhoto" method="POST" enctype="multipart/form-data">
            <table>
                <tbody>
                    <tr>
                        <td><fmt:message key="photo.label" bundle="${page.bundle}"/>:&nbsp;</td>
                        <td><input type="file" name="file"></td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <fmt:message var="label" key="send-photo.label" bundle="${page.bundle}"/>
                        <td><input type="submit" value="${label}" name="ok"></td>
                    </tr>
                </tbody>
            </table>
        </form>
    </c:otherwise>
</c:choose>
                ${page.texts['bottom']}
            </div>
            <c:import url="/WEB-INF/common/footer.jsp"/>
        </div>
        <c:import url="/WEB-INF/common/scripts.jsp"/>
        <c:import url="/WEB-INF/common/inplace.jsp"/>
    </body>
</html>
