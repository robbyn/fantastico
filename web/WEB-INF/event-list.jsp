<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${session.locale}"/>
<c:set var="events" value="${page.queryEvents}"/>
<c:set var="dateMin" scope="request" value=""/>
<c:if test="${not empty page.dateMin}">
<fmt:formatDate var="dateMin" scope="request" type="date" pattern="dd/MM/yyyy" value="${page.dateMin}"/>
</c:if>
<c:set var="dateMax" scope="request" value=""/>
<c:if test="${not empty page.dateMax}">
<fmt:formatDate var="dateMax" scope="request" type="date" pattern="dd/MM/yyyy" value="${page.dateMax}"/>
</c:if>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="event-list.title" bundle="${page.bundle}"/></title>
        <link rel='stylesheet' type='text/css' href='../css/stylesheet.css'>
        <c:import url="/WEB-INF/common/analytics.jsp"/>
    </head>
    <body id="${page.name}">
        <div id="wrapper">
            <h1><fmt:message key="event-list.title" bundle="${page.bundle}"/></h1>
            <div id="content">
                <c:import url="/WEB-INF/common/menu.jsp"/>
            <div id="search">
            <form method="POST" action="changeDates">
                <fmt:message key="event-list.from.label" bundle="${page.bundle}"/> <input name="dateMin" value="${dateMin}" maxlength="10" size="10" class="date">
                <fmt:message key="event-list.to.label" bundle="${page.bundle}"/> <input name="dateMax" value="${dateMax}" maxlength="10" size="10" class="date">
                <fmt:message var="label" key="event-list.search.label" bundle="${page.bundle}"/>
                <input type="submit" name="submit" value="<c:out value="${label}"/>">
            </form>
            <form method="POST" action="../event/create">
                <fmt:message var="label" key="event-list.new-event.label" bundle="${page.bundle}"/>
                <input type="submit" name="submit" value="<c:out value="${label}"/>">
            </form>
            </div>
<c:if test="${not empty events}">
        <table cellpadding="2" cellspacing="2" class="list">
            <thead>
                <tr>
                    <th><fmt:message key="event.date-time.label" bundle="${page.bundle}"/></th>
                    <th><fmt:message key="event.location.label" bundle="${page.bundle}"/></th>
                    <th><fmt:message key="event.language.label" bundle="${page.bundle}"/></th>
                    <th><fmt:message key="event.type.label" bundle="${page.bundle}"/></th>
                    <th><fmt:message key="event.age-range.label" bundle="${page.bundle}"/></>
                    <th><fmt:message key="event.address.label" bundle="${page.bundle}"/></th>
                    <th>&nbsp;</th>
                </tr>
            </thead>
            <tbody>
    <c:forEach var="event" items="${events}">
                <tr>
                    <td><fmt:formatDate type="date" pattern="dd/MM/yyyy HH:mm" value="${event.dateTime}"/></td>
                    <td><c:out value="${event.location}"/></td>
                    <td><c:out value="${event.language.labels[session.language]}"/></td>
                    <td><c:out value="${helper.eventSummaries[event][session.language]}"/></td>
                    <td>${event.ageRange.minAge}&nbsp;-&nbsp;${event.ageRange.maxAge}</td>
                    <td>${event.address}</td>
                    <td><c:if test="${event.canEdit}">
                            <c:url var="url" value="/event/edit">
                                <c:param name="id" value="${event.id}"/>
                            </c:url>
                            <a href="${url}" class="button"><fmt:message key="event-list.edit.label" bundle="${page.bundle}"/></a>
                        </c:if>
                        <c:if test="${event.canPlace}">
                            <c:url var="url" value="/place/edit">
                                <c:param name="id" value="${event.id}"/>
                            </c:url>
                            <a href="${url}" class="button"><fmt:message key="event-list.place.label" bundle="${page.bundle}"/></a>
                        </c:if>
                        <c:if test="${event.canCancel}">
                            <c:url var="url" value="cancel">
                                <c:param name="id" value="${event.id}"/>
                            </c:url>
                            <a href="${url}" class="button"><fmt:message key="cancel.label" bundle="${page.bundle}"/></a>
                        </c:if></td>
                </tr>
    </c:forEach>
            </tbody>
        </table>
</c:if>
            </div>
            <c:import url="/WEB-INF/common/footer.jsp"/>
        </div>
        <c:import url="/WEB-INF/common/scripts.jsp"/>
    </body>
</html>
