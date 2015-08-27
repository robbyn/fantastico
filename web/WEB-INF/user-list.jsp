<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${session.locale}"/>
<c:set var="users" value="${page.queryUsers}"/>
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
            <div id="search">
            <form method="POST" action="changeFilter">
                <fmt:message key="filter.label" bundle="${page.bundle}"/> <input name="nameFilter" value="${page.nameFilter}" size="10">
                <fmt:message var="label" key="filter-submit.label" bundle="${page.bundle}"/>
                <input type="submit" name="submit" value="${label}">
            </form>
            </div>
<c:if test="${not empty users}">
        <table cellpadding="2" cellspacing="2" class="list">
            <thead>
                <tr>
                    <th><fmt:message key="userid.label" bundle="${page.bundle}"/></th>
                    <th><fmt:message key="status.label" bundle="${page.bundle}"/></th>
                    <th><fmt:message key="name.label" bundle="${page.bundle}"/></th>
                    <th><fmt:message key="email.label" bundle="${page.bundle}"/></th>
                    <th><fmt:message key="age.label" bundle="${page.bundle}"/></>
                    <th><fmt:message key="language.label" bundle="${page.bundle}"/></th>
                    <th><fmt:message key="orientation.label" bundle="${page.bundle}"/></th>
                    <th>&nbsp;</th>
                </tr>
            </thead>
            <tbody>
<c:forEach var="user" items="${users}">
                <tr>
                    <td>${user.id}</td>
                    <td><fmt:message key="status.${user.status.name}" bundle="${page.bundle}"/></td>
                    <td><c:out value="${user.lastName}, ${user.firstName}"/></td>
                    <td><a href="mailto:<c:out value="${user.email}"/>"><c:out value="${user.email}"/></a></td>
                    <td>${user.ageNow}</td>
                    <td><c:out value="${user.favoriteLanguage.labels[session.language]}"/></td>
                    <td><fmt:message key="orientation.${user.orientation.name}" bundle="${page.bundle}"/></td>
                    <td>
                        <form method="POST" action="../profile/edit">
                            <input type="hidden" name="user" value="<c:out value="${user.email}"/>">
                            <fmt:message var="label" key="update" bundle="${page.bundle}"/>
                            <input type="submit" name="submit" value="${label}">
                        </form>
<c:if test="${user.status.name!='DISABLED'}">
                        <form method="POST" action="activate">
                            <input type="hidden" name="activate" value="false">
                            <input type="hidden" name="user" value="<c:out value="${user.email}"/>">
                            <fmt:message var="label" key="deactivate" bundle="${page.bundle}"/>
                            <input type="submit" name="submit" value="${label}">
                        </form>
</c:if>
<c:if test="${user.status.name!='ENABLED'}">
                        <form method="POST" action="activate">
                            <input type="hidden" name="activate" value="true">
                            <input type="hidden" name="user" value="<c:out value="${user.email}"/>">
                            <fmt:message var="label" key="activate" bundle="${page.bundle}"/>
                            <input type="submit" name="submit" value="${label}">
                        </form>
</c:if>
                    </td>
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
