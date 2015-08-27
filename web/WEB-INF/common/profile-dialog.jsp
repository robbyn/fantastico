<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${session.locale}"/>
<c:set var="user" value="${page.user}"/>
<c:set var="relationship" value="any"/>
<c:if test="${not empty user.relationship}">
    <c:set var="relationship" value="${user.relationship.name}"/>
</c:if>
<fmt:formatDate var="dateOfBirth" scope="request" type="date" pattern="dd/MM/yyyy" value="${user.dateOfBirth}"/>
<c:set var="language" value=""/>
<c:if test="${not empty user.favoriteLanguage}">
    <c:set var="language" value="user.favoriteLanguage.code"/>
</c:if>
<div id="profile-dlg" class="dialog">
    <h1><fmt:message key="title" bundle="${page.bundle}">
            <fmt:param value="${user.firstName} ${user.lastName}"/>
        </fmt:message></h1>
    <c:url var="url" value="/photo">
        <c:param name="user" value="${user.email}"/>
    </c:url>
    <fmt:message var="alt" key="photo.label" bundle="${page.bundle}"/>
    <div id="photo"><img alt="${alt}" src="${url}"></div>
    <table border="0" cellpadding="2" cellspacing="2">
        <tbody>
            <tr>
                <td><fmt:message key="email.label" bundle="${page.bundle}"/>: </td>
                <td><c:out value="${user.email}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="sex.label" bundle="${page.bundle}"/>: </td>
                <td><fmt:message key="sex.${user.sex.name}" bundle="${page.bundle}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="orientation.label" bundle="${page.bundle}"/>: </td>
                <td><fmt:message key="orientation.${user.orientation.name}" bundle="${page.bundle}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="relationship.label" bundle="${page.bundle}"/>: </td>
                <td><fmt:message key="relationship.${relationship}" bundle="${page.bundle}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="language.label" bundle="${page.bundle}"/>: </td>
                <td><c:out value="${helper.languages[language].labels[session.language]}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="dateOfBirth.label" bundle="${page.bundle}"/>: </td>
                <td><c:out value="${dateOfBirth}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="lastName.label" bundle="${page.bundle}"/>: </td>
                <td><c:out value="${user.lastName}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="firstName.label" bundle="${page.bundle}"/>: </td>
                <td><c:out value="${user.firstName}"/>"></td>
            </tr>
            <tr>
                <td><fmt:message key="address.label" bundle="${page.bundle}"/>: </td>
                <td><c:out value="${user.address1}"/></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><c:out value="${user.address2}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="zip.label" bundle="${page.bundle}"/>: </td>
                <td><c:out value="${user.zip}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="city.label" bundle="${page.bundle}"/> </td>
                <td><c:out value="${user.city}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="country.label" bundle="${page.bundle}"/>: </td>
                <td><c:out value="${helper.countries[user.country].labels[session.language]}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="state.label" bundle="${page.bundle}"/>: </td>
                <td><c:choose>
                        <c:when test="${user.country=='US'}"><c:out value="${helper.states[user.state].name}"/></c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose></td>
            </tr>
            <tr>
                <td><fmt:message key="phone.label" bundle="${page.bundle}"/>: </td>
                <td><c:out value="${user.phoneNumber}"/></td>
            </tr>
<c:if test="${request.roles['admin']}">
            <tr>
                <td><fmt:message key="admin.label" bundle="${page.bundle}"/>: </td>
                <td><c:choose>
                        <c:when test="${user.admin}">oui</c:when>
                        <c:otherwise>non</c:otherwise>
                    </c:choose></td>
            </tr>
            <tr>
                <td><fmt:message key="credit.label" bundle="${page.bundle}"/>: </td>
                <fmt:formatNumber var="credit" value="${user.credit}" pattern="0.00"/>
                <td><c:out value="${credit}"/></td>
            </tr>
</c:if>
        </tbody>
    </table>
</div>
<c:import url="/WEB-INF/common/scripts.jsp"/>
<script type="text/javascript">
    function showProfileDialog(userid) {
        $("#profile-dlg").dialog({
            width: 640,
            modal: true,
            close: function() {
                $("#profile-dlg").dialog("destroy");
            }
        });
    }
</script>
