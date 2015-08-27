<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${session.locale}"/>
<c:set var="date" scope="request" value=""/>
<c:set var="time" scope="request" value=""/>
<c:set var="event" scope="request" value="${page.event}"/>
<c:choose>
    <c:when test="${event.orientation.name == 'HETERO'}">
        <c:set var="keya" value="females"/>
        <c:set var="keyb" value="males"/>
        <c:set var="classa" value="fdroppable"/>
        <c:set var="classb" value="mdroppable"/>
    </c:when>
    <c:otherwise>
        <c:set var="keya" value="groupa"/>
        <c:set var="keyb" value="groupb"/>
        <c:set var="classa" value="droppable"/>
        <c:set var="classb" value="droppable"/>
    </c:otherwise>
</c:choose>
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
                <table>
                    <col id="available">
                    <col id="groupa">
                    <col id="groupb">
                    <thead>
                        <tr>
                            <th><fmt:message key="available" bundle="${page.bundle}"/></th>
                            <th><fmt:message key="${keya}" bundle="${page.bundle}"/></th>
                            <th><fmt:message key="${keyb}" bundle="${page.bundle}"/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="r" begin="1" end="${event.coupleCount}">
                        <tr>
                            <c:if test="${r == 1}">
                            <td rowspan="${event.coupleCount}" id="av">
                                <c:forEach var="entry" items="${event.attendances}">
                                    <c:set var="user" value="${entry.key}"/>
                                    <c:set var="att" value="${entry.value}"/>
                                    <c:if test="${att.number == 0}">
                                        <c:set var="pclass" value="${user.sex.name == 'FEMALE' ? 'female' : 'male'}"/>
                                <span id="u${user.id}" class="${pclass}"><c:out value="${user.firstName} ${user.lastName}"/></span>
                                    </c:if>
                                </c:forEach>
                            </td>
                            </c:if>
                            <c:set var="user" value="${helper.userAtPlace[event][r]}"/>
                            <c:choose>
                                <c:when test="${empty user}">
                                    <td class="groupa ${classa}" id="p${r}">&nbsp;</td>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="pclass" value="${user.sex.name == 'FEMALE' ? 'female' : 'male'}"/>
                                    <td class="groupa ${classa}" id="p${r}"><span id="u${user.id}" class="${pclass}"><c:out value="${user.firstName} ${user.lastName}"/></span></td>
                                </c:otherwise>
                            </c:choose>
                            <c:set var="user" value="${helper.userAtPlace[event][r+event.coupleCount]}"/>
                            <c:choose>
                                <c:when test="${empty user}">
                                    <td class="groupb ${classb}" id="p${r+event.coupleCount}">&nbsp;</td>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="pclass" value="${user.sex.name == 'FEMALE' ? 'female' : 'male'}"/>
                                    <td class="groupb ${classb}" id="p${r+event.coupleCount}"><span id="u${user.id}" class="${pclass}"><c:out value="${user.firstName} ${user.lastName}"/></span></td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                        </c:forEach>
                        <tr></tr>
                    </tbody>
                </table>
                <div><form method="POST" action="validate">
                    <fmt:message var="label" key="validate.label" bundle="${page.bundle}"/>
                    <input type="submit" name="submit" value="${label}">
                </form></div>
            </div>
            <c:import url="/WEB-INF/common/footer.jsp"/>
        </div>
        <script type="text/javascript" src="../js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="../js/jquery-ui-1.8.4.custom.min.js"></script>
        <script type="text/javascript">
            $(function() {
                function move(elm, to) {
                    var id = elm.attr("id");
                    var n = $("<span/>").appendTo(to);
                    if (elm.hasClass("female")) {
                        n.addClass("female");
                    } else if (elm.hasClass("male")) {
                        n.addClass("male");
                    }
                    n.text(elm.text());
                    elm.remove();
                    n.attr("id", id);
                    n.draggable({ revert: 'invalid' });
                }

                function placeIt(event, ui) {
                    var that = this;
                    var drag = ui.draggable;
                    $.ajax({
                        type: "POST",
                        url: "place",
                        data: "user=" + drag.attr("id") +
                                "&place=" + $(that).attr("id"),
                        dataType: "text",
                        success: function(data) {
                            var parent = drag.parent();
                            var cur = $("span", that);
                            if (cur.length) {
                                move(cur, $("#av"));
                            }
                            $(that).empty();
                            move(drag, that);
                            if (parent.attr("id") !== "av") {
                                parent.text("\u00A0"); // &nbsp;
                            }
                        }
                    });
                }

                function removeIt(event, ui) {
                    var that = this;
                    var drag = ui.draggable;
                    $.ajax({
                        type: "POST",
                        url: "remove",
                        data: "user=" + drag.attr("id"),
                        dataType: "text",
                        success: function(data) {
                            var parent = drag.parent();
                            move(drag, that);
                            parent.text("\u00A0"); // &nbsp;
                        }
                    });
                }

                $(".female").draggable({ revert: 'invalid' });
                $(".male").draggable({ revert: 'invalid' });
                $("#av").droppable({
                    accept: function(drag) {
                        if ($(drag).parent().attr("id") === $(this).attr("id")) {
                            return false;
                        }
                        return true;
                    },
                    drop: removeIt
                })
                $(".droppable").droppable({
                    accept: function(drag) {
                        if ($(drag).parent().attr("id") === $(this).attr("id")) {
                            return false;
                        }
                        return true;
                    },
                    drop: placeIt
                });
                $(".fdroppable").droppable({
                    accept: function(drag) {
                        if ($(drag).parent().attr("id") === $(this).attr("id")) {
                            return false;
                        }
                        return $(drag).hasClass("female");
                    },
                    drop: placeIt
                });
                $(".mdroppable").droppable({
                    accept: function(drag) {
                        if ($(drag).parent().attr("id") === $(this).attr("id")) {
                            return false;
                        }
                        return $(drag).hasClass("male");
                    },
                    drop: placeIt
                });
            });
        </script>
    </body>
</html>
