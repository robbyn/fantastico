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
                <table cellpadding="2" cellspacing="2">
                    <col id="label">
                    <col id="value">
                    <tbody>
                        <tr>
                            <td><fmt:message key="event.date.label" bundle="${page.bundle}"/>:&nbsp;</td>
                            <td><fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${event.dateTime}"/></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="event.time.label" bundle="${page.bundle}"/>:&nbsp;</td>
                            <td><fmt:formatDate type="date" pattern="HH:mm" value="${event.dateTime}"/></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="event.location.label" bundle="${page.bundle}"/>:&nbsp;</td>
                            <td><c:out value="${event.location}"/></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="event.min-age.label" bundle="${page.bundle}"/>:&nbsp;</td>
                            <td><c:out value="${event.ageRange.minAge}"/></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="event.max-age.label" bundle="${page.bundle}"/>:&nbsp;</td>
                            <td><c:out value="${event.ageRange.maxAge}"/></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="event.type.label" bundle="${page.bundle}"/>:&nbsp;</td>
                            <td><c:out value="${helper.eventSummaries[event][session.language]}"/></td>
                        </tr>
<c:if test="${not empty event.language}">
                        <tr>
                            <td><fmt:message key="event.language.label" bundle="${page.bundle}"/>: </td>
                            <td>${event.language.labels[session.language]}</td>
                        </tr>
</c:if>
                        <tr>
                            <td><fmt:message key="event.price.label" bundle="${page.bundle}"/>: </td>
                            <td><fmt:formatNumber value="${event.price}" pattern="0.00"/></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="credit" bundle="${page.bundle}"/>: </td>
                            <td><fmt:formatNumber value="${user.credit}" pattern="0.00"/></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="to-pay" bundle="${page.bundle}"/>: </td>
<c:choose>
    <c:when test="${user.credit ge event.price}">
                            <td>-</td>
    </c:when>
    <c:otherwise>
                            <td><fmt:formatNumber value="${event.price - user.credit}" pattern="0.00"/></td>
    </c:otherwise>
</c:choose>
                        </tr>
                    </tbody>
                </table>
                ${page.texts['bottom']}
<c:choose>
    <c:when test="${user.credit ge event.price}">
                ${page.texts['enough-credit']}
    </c:when>
    <c:otherwise>
                ${page.texts['payment-required']}
                <fmt:message var="label" key="payment-button" bundle="${page.bundle}"/>
                <c:set target="${page.paymentButton}" property="label" value="${label}"/>
                ${page.paymentButton.html}
    </c:otherwise>
</c:choose>
            </div>
            <c:import url="/WEB-INF/common/footer.jsp"/>
        </div>
        <c:import url="/WEB-INF/common/scripts.jsp"/>
        <c:import url="/WEB-INF/common/inplace.jsp"/>
    </body>
</html>
