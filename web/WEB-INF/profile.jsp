<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${session.locale}"/>
<c:set var="errors" value="${page.errors}"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="title" bundle="${page.bundle}">
                    <fmt:param value="${page.firstName} ${page.lastName}"/>
                </fmt:message></title>
        <link rel='stylesheet' type='text/css' href='../css/stylesheet.css'>
        <c:import url="/WEB-INF/common/analytics.jsp"/>
    </head>
    <body id="${page.name}">
        <div id="wrapper">
            <h1><fmt:message key="title" bundle="${page.bundle}">
                    <fmt:param value="${page.firstName} ${page.lastName}"/>
                </fmt:message></h1>
            <div id="content">
                <c:import url="/WEB-INF/common/menu.jsp"/>
            <c:url var="url" value="/photo/get">
                <c:param name="user" value="${page.email}"/>
            </c:url>
            <fmt:message var="alt" key="photo.label" bundle="${page.bundle}"/>
            <div id="photo"><img alt="${alt}" src="${url}"></div>
            <form action="save" method="POST">
                <table border="0" cellpadding="2" cellspacing="2">
                    <tbody>
                        <tr>
                            <td>*<fmt:message key="email.label" bundle="${page.bundle}"/>: </td>
<c:choose>
    <c:when test="${request.roles['admin']}">
                            <td><input name="email" value="<c:out value="${page.email}"/>">
<c:forEach var="err" items="${errors['email']}">
                                <div class="error"><c:out value="${err}"/></div>
</c:forEach>
                            </td>
    </c:when>
    <c:otherwise>
                            <td><c:out value="${page.email}"/></td>
    </c:otherwise>
</c:choose>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="password.label" bundle="${page.bundle}"/>: </td>
                            <td><input name="password" type="password" autocomplete="off">
<c:forEach var="err" items="${errors['password']}">
                                <div class="error"><c:out value="${err}"/></div>
</c:forEach>
                            </td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="verification.label" bundle="${page.bundle}"/>: </td>
                            <td><input name="verification" type="password" autocomplete="off">
<c:forEach var="err" items="${errors['verification']}">
                                <div class="error"><c:out value="${err}"/></div>
</c:forEach>
                            </td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="sex.label" bundle="${page.bundle}"/>: </td>
                            <td><c:forEach var="sex" items="FEMALE,MALE">
                                    <input id="${sex}" name="sex" value="${sex}" type="radio" <c:if test="${page.sex.name==sex}">checked</c:if>><label class="radio" for="${sex}"><fmt:message key="sex.${sex}" bundle="${page.bundle}"/></label>
                                </c:forEach>
<c:forEach var="err" items="${errors['sex']}">
                                <div class="error"><c:out value="${err}"/></div>
</c:forEach>
                            </td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="orientation.label" bundle="${page.bundle}"/>: </td>
                            <td><c:forEach var="orientation" items="HETERO,HOMO,BI">
                                    <input id="${orientation}" name="orientation" value="${orientation}" type="radio" <c:if test="${page.orientation.name==orientation}">checked</c:if>><label class="radio" for="${orientation}"><fmt:message key="orientation.${orientation}" bundle="${page.bundle}"/></label>
                                </c:forEach></td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="relationship.label" bundle="${page.bundle}"/>:<br>
                            </td>
                            <td><c:forEach var="rel" items="SERIOUS,CASUAL,any">
                                    <input id="${rel}" name="relationship" value="${rel}" type="radio" <c:if test="${page.relationship==rel}">checked</c:if>><label class="radio" for="${rel}"><fmt:message key="relationship.${rel}" bundle="${page.bundle}"/></label>
                                </c:forEach></td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="language.label" bundle="${page.bundle}"/>: </td>
                            <td><select size="1" name="language" id="userLanguage">
<c:forEach var="lc" items="${helper.site.supportedLanguages}">
    <c:set var="lng" value="${helper.languages[lc]}"/>
    <option value="${lng.code}" <c:if test="${lng.code==page.userLanguage}">selected</c:if>><c:out value="${lng.labels[session.language]}"/></option>
</c:forEach>
                            </select></td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="dateOfBirth.label" bundle="${page.bundle}"/>: </td>
                            <td><input maxlength="10" size="10" name="dateOfBirth" value="${page.dateOfBirth}" placeholder="dd/mm/yyyy">
<c:forEach var="err" items="${errors['dateOfBirth']}">
                                <div class="error"><c:out value="${err}"/></div>
</c:forEach>
                            </td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="lastName.label" bundle="${page.bundle}"/>: </td>
                            <td><input maxlength="32" size="32" name="lastName" value="<c:out value="${page.lastName}"/>">
<c:forEach var="err" items="${errors['lastName']}">
                                <div class="error"><c:out value="${err}"/></div>
</c:forEach>
                            </td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="firstName.label" bundle="${page.bundle}"/>: </td>
                            <td><input maxlength="32" size="32" name="firstName" value="<c:out value="${page.firstName}"/>">
<c:forEach var="err" items="${errors['firstName']}">
                                <div class="error"><c:out value="${err}"/></div>
</c:forEach>
                            </td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="address.label" bundle="${page.bundle}"/>: </td>
                            <td><input maxlength="255" size="72" name="address1" value="<c:out value="${page.address1}"/>"></td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input maxlength="255" size="72" name="address2" value="<c:out value="${page.address2}"/>"></td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="zip.label" bundle="${page.bundle}"/>: </td>
                            <td><input size="6" maxlength="6" name="zip" value="<c:out value="${page.zip}"/>"></td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="city.label" bundle="${page.bundle}"/> </td>
                            <td><input size="32" maxlength="32" name="city" value="<c:out value="${page.city}"/>"></td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="country.label" bundle="${page.bundle}"/>: </td>
                            <td>
                                <select size="1" name="country">
<c:forEach var="country" items="${page.allCountries}">
                                    <option value="${country.code}" <c:if test="${page.country==country.code}"> selected</c:if>><c:out value="${country.labels[session.language]}"/></option>
</c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td><fmt:message key="state.label" bundle="${page.bundle}"/>: </td>
                            <td><select size="1" name="state">
                                    <option value="-" <c:if test="${empty page.state}"> selected</c:if>><c:out value="-"/></option>
<c:forEach var="state" items="${helper.allStates}">
                                    <option value="${state.code}" <c:if test="${page.state==state.code}"> selected</c:if>><c:out value="${state.name}"/></option>
</c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td><fmt:message key="phone.label" bundle="${page.bundle}"/>: </td>
                            <td><input size="32" maxlength="32" name="phone" value="<c:out value="${page.phone}"/>">
                            </td>
                        </tr>
                        <tr>
                            <td><fmt:message key="about-me.label" bundle="${page.bundle}"/>: </td>
                            <td><textarea cols="80" id="aboutMe" name="aboutMe" rows="5"><c:out value="${page.aboutMe}"/></textarea></td>
                        </tr>
<c:if test="${request.roles['admin']}">
                        <tr>
                            <td>*<fmt:message key="admin.label" bundle="${page.bundle}"/>: </td>
                            <td><input id="admin" name="admin" type="checkbox" value="true" <c:if test="${page.admin}">checked</c:if>></td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="credit.label" bundle="${page.bundle}"/>: </td>
                            <td><input name="credit" value="${page.credit}" maxlength="10" size="10"></td>
                        </tr>
</c:if>
                        <tr>
                            <td> <br>
                            </td>
                            <fmt:message var="label" key="submit.label" bundle="${page.bundle}"/>
                            <td><input type="submit" value="${label}" name="ok"></td>
                        </tr>
                    </tbody>
                </table>
            </form>
            </div>
            <c:import url="/WEB-INF/common/footer.jsp"/>
        </div>
        <div id="photodlg" class="dialog">
            <form action="changePhoto" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="email" value="${page.email}">
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
        </div>
        <c:import url="/WEB-INF/common/scripts.jsp"/>
        <script type="text/javascript">
            $(function() {
                $("input[type='password']").val("");
                var country = $("select[name='country']");
                country.change(function() {
                    var state = $("select[name='state']");
                    if ($(this).val() === "US") {
                        state.removeAttr("disabled");
                    } else {
                        state.attr("disabled", "true");
                    }
                    return false;
                });
                country.change();
                $("#photo").append("<div><a href='change-photo' class='button'><fmt:message key="change-photo.label" bundle="${page.bundle}"/></a></div>");
                $("#photo a").click(function() {
                    $("#photodlg").dialog({
                        width: 480,
                        modal: true,
                        open: function() {
                            $("#photodlg input[name='file']").focus();
                        },
                        close: function() {
                            $("#photodlg").dialog("destroy");
                        }
                    });
                    return false;
                });
            });
        </script>
    </body>
</html>
