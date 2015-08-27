<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${session.locale}"/>
<c:set var="ptor" value="${page.paginator}"/>
<c:set var="events" value="${ptor.pageItems}"/>
<c:set var="ranges" value="${ptor.pageRanges}"/>
<c:if test="${not empty events}">
    <div id="upcoming">
        <table cellpadding="2" cellspacing="2">
            <thead>
                <tr>
                    <th><fmt:message key="event.date-time.label" bundle="${page.bundle}"/></th>
                    <th><fmt:message key="event.location.label" bundle="${page.bundle}"/></th>
                    <th><fmt:message key="event.language.label" bundle="${page.bundle}"/></th>
                    <th><fmt:message key="event.type.label" bundle="${page.bundle}"/></th>
                    <th><fmt:message key="event.age-range.label" bundle="${page.bundle}"/></>
                    <th>&nbsp;</th>
                </tr>
            </thead>
    <c:if test="${ptor.pageCount > 1}">
            <tfoot>
                <tr>
                    <td colspan="6">
                        <div class="pager">
                        <c:set var="first" value="${true}"/>
                        <c:forEach var="range" items="${ranges}">
                            <c:if test="${not first}">
                            <span>&hellip;</span>
                            </c:if>
                            <c:set var="first" value="${false}"/>
                            <c:forEach var="page" begin="${range.min}" end="${range.max}">
                                <c:url var="url" value="setPage">
                                    <c:param name="page" value="${page}"/>
                                </c:url>
                            <a href="${url}"<c:if test="${page==ptor.current}"> class="current"</c:if>>${page+1}</a>
                            </c:forEach>
                        </c:forEach>
                        </div>
                        <div class="clear"></div>
                    </td>
                </tr>
            </tfoot>
    </c:if>
            <tbody>
    <c:forEach var="event" items="${events}">
                <tr>
                    <td><fmt:formatDate type="date" pattern="dd/MM/yyyy HH:mm" value="${event.dateTime}"/></td>
                    <td><c:out value="${event.location}"/></td>
                    <td><c:out value="${event.language.labels[page.language]}"/></td>
                    <td><c:out value="${helper.eventSummaries[event][session.language]}"/></td>
                    <td>${event.ageRange.minAge}&nbsp;-&nbsp;${event.ageRange.maxAge}</td>
                    <c:url var="href" value="/event/edit">
                        <c:param name="id" value="${event.id}"/>
                    </c:url>
                    <td>
<c:if test="${event.canApply && (!request.roles['user'] || empty event.attendances[helper.user])}">
                        <form method="POST" action="../apply/start">
                            <input type="hidden" name="id" value="${event.id}">
                            <fmt:message var="label" key="book.label" bundle="${page.bundle}"/>
                            <input type="submit" name="submit" value="${label}">
                        </form>
</c:if>
                    </td>
                </tr>
    </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>
