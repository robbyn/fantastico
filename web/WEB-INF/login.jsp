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
        <link rel="stylesheet" type="text/css" href="../css/stylesheet.css"/>
        <title><fmt:message key="title" bundle="${page.bundle}"/></title>
        <c:import url="/WEB-INF/common/analytics.jsp"/>
    </head>
    <body id="${page.name}">
        <div id="wrapper">
            <div id="content">
            <h2><fmt:message key="title" bundle="${page.bundle}"/></h2>
<c:forEach var="entry" items="${page.errors}">
    <c:forEach var="err" items="${entry.value}">
                <div class="error">${err}</div>
    </c:forEach>
</c:forEach>
                <form id="login-form" action="j_security_check" method="post">
                    <table>
                        <tbody>
                            <tr>
                                <td><label for="username"><fmt:message key="email.label" bundle="${page.bundle}"/>:&nbsp;</label></td>
                                <td><input type="text" name="j_username" id="username" /></td>
                            </tr>
                            <tr>
                                <td><label for="password"><fmt:message key="password.label" bundle="${page.bundle}"/>:&nbsp;</label></td>
                                <td><input type="password" name="j_password" id="password" /></td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <fmt:message var="label" key="submit" bundle="${page.bundle}"/>
                                <td><input type="submit" id="submit" name="submit" value="${label}" /></td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td><a href="../reg/create"><fmt:message key="register" bundle="${page.bundle}"/></a></td>
                            </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <c:import url="/WEB-INF/common/footer.jsp"/>
        </div>
    </body>
</html>
