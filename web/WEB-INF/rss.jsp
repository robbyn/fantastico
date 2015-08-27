<%@page contentType="application/rss+xml"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${session.locale}"/>
<rss version="2.0">
    <channel>
        <title><fmt:message key="title" bundle="${page.bundle}"/></title>
        <link><c:url value="${request.baseUrl}"/></link>
        <description><fmt:message key="description" bundle="${page.bundle}"/></description>
<c:forEach items="${helper.recentEvents}" var="event">
        <item>
            <title><fmt:formatDate type="both" pattern="EEEE dd/MM/yyyy HH:mm" value="${event.dateTime}"/> - <c:out value="${event.location}"/></title>
            <link><c:url value="${request.baseUrl}"/></link>
            <pubDate><fmt:formatDate type="both" pattern="EEEE dd/MM/yyyy HH:mm" value="${event.creation}"/></pubDate>
            <description>
                <fmt:formatDate type="both" pattern="EEEE dd/MM/yyyy HH:mm" value="${event.dateTime}"/> - <c:out value="${event.location}"/>
                - <c:out value="${event.language.labels[page.language]}"/>
                - <fmt:message key="event.orientation.${event.orientation.name}" bundle="${page.bundle}"/>
<c:if test="${not empty event.relationship}">
                - <fmt:message key="event.relationship.${event.relationship.name}" bundle="${page.bundle}"/>
</c:if>
            </description>
        </item>
</c:forEach>
    </channel>
</rss>
