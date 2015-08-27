<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<ul id="nav">
    <li id="item-main"><a href="../main/start"><fmt:message key="menu.home" bundle="${page.bundle}"/></a></li>
    <li id="item-info"><a href="../info/"><fmt:message key="menu.help" bundle="${page.bundle}"/></a></li>
    <li id="item-faq"><a href="../faq/"><fmt:message key="menu.faq" bundle="${page.bundle}"/></a></li>
    <li id="item-contact"><a href="../contact/"><fmt:message key="menu.contact" bundle="${page.bundle}"/></a></li>
</ul>
<ul id="user">
<c:choose>
    <c:when test="${empty helper.user}">
    <li><a href="../main/login"><fmt:message key="menu.login" bundle="${page.bundle}" /></a></li>
    </c:when>
    <c:otherwise>
    <li><a href="../profile/myProfile"><c:out value="${helper.user.firstName} ${helper.user.lastName}"/></a></li>
    </c:otherwise>
</c:choose>
</ul>
<ul id="lang-mnu">
<c:forEach var="lc" items="${helper.site.supportedLanguages}">
<c:set var="lng" value="${helper.languages[lc]}"/>
<c:url var="url" value="setLanguage">
    <c:param name="language" value="${lng.code}"/>
</c:url>
<c:choose>
    <c:when test="${lng.code==page.language}">
    </c:when>
    <c:otherwise>
    <li><a href="${url}"><c:out value="${lng.defaultLabel}"/></a></li>
    </c:otherwise>
</c:choose>
</c:forEach>
</ul>
<c:if test="${request.roles['admin']}">
<ul id="admin-menu">
    <li id="item-user-list"><a href="../user-list/"><fmt:message key="menu.users" bundle="${page.bundle}"/></a></li>
    <li id="item-event-list"><a href="../event-list/"><fmt:message key="menu.events" bundle="${page.bundle}"/></a></li>
    <li id="item-emails"><a href="../email/"><fmt:message key="menu.emails" bundle="${page.bundle}"/></a></li>
    <li id="mode-sel">
    <c:choose>
        <c:when test="${session.editMode}">
            <a href="setEditMode?on=false"><fmt:message key="menu.normal-mode" bundle="${page.bundle}"/></a>
        </c:when>
        <c:otherwise>
            <a href="setEditMode?on=true"><fmt:message key="menu.edit-mode" bundle="${page.bundle}"/></a>
        </c:otherwise>
    </c:choose>
    </li>
</ul>
</c:if>
<div class="clear"></div>
