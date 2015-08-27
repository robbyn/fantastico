<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${session.locale}"/>
<c:set var="date" scope="request" value=""/>
<c:set var="time" scope="request" value=""/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="event.title" bundle="${page.bundle}"/></title>
        <link rel='stylesheet' type='text/css' href='../css/stylesheet.css'>
        <c:import url="/WEB-INF/common/analytics.jsp"/>
    </head>
    <body id="${page.name}">
        <div id="wrapper">
            <h1><fmt:message key="event.title" bundle="${page.bundle}"/></h1>
            <div id="content">
                <c:import url="/WEB-INF/common/menu.jsp"/>
            <form action="save" method="POST">
                <table cellpadding="2" cellspacing="2">
                    <col id="label">
                    <col id="value">
                    <tbody>
                        <tr>
                            <td>*<fmt:message key="event.date.label" bundle="${page.bundle}"/>:&nbsp;</td>
                            <td><input name="date" value="${page.date}" maxlength="10" size="10" class="date"></td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="event.time.label" bundle="${page.bundle}"/>:&nbsp;</td>
                            <td><input name="time" value="${page.time}" maxlength="5" size="5"></td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="event.location.label" bundle="${page.bundle}"/>:&nbsp;</td>
                            <td><input name="location" value="<c:out value="${page.location}"/>"></td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="event.orientation.label" bundle="${page.bundle}"/>:&nbsp;</td>
                            <td><c:forEach var="orientation" items="HETERO,GAY,LESBIAN">
                                    <input id="${orientation}" name="orientation" value="${orientation}" type="radio" <c:if test="${page.orientation.name==orientation}">checked</c:if>><label class="radio" for="${orientation}"><fmt:message key="event.orientation.${orientation}" bundle="${page.bundle}"/></label>
                                </c:forEach></td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="event.min-age.label" bundle="${page.bundle}"/>:&nbsp;</td>
                            <td><input name="minAge" value="${page.minAge}" maxlength="2" size="2"></td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="event.max-age.label" bundle="${page.bundle}"/>:&nbsp;</td>
                            <td><input name="maxAge" value="${page.maxAge}" maxlength="2" size="2"></td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="event.relationship.label" bundle="${page.bundle}"/>:&nbsp;</td>
                            <td><c:forEach var="rel" items="SERIOUS,CASUAL,any">
                                    <input id="${rel}" name="relationship" value="${rel}" type="radio" <c:if test="${page.relationship==rel}">checked</c:if>><label class="radio" for="${rel}"><fmt:message key="event.relationship.${rel}" bundle="${page.bundle}"/></label>
                                </c:forEach></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="event.keywords.label" bundle="${page.bundle}"/>:&nbsp;</td>
                            <td><div id="keywords"><c:forEach var="kw" items="${page.allKeywords}">
                                    <span class="keyword" id="kw_${kw.id}"><input type="checkbox" name="keyword" value="${kw.id}"<c:if test="${page.keywordMap[kw]}"> checked</c:if>>
                                        <a href="editKeyword?id=${kw.id}"><c:out value="${kw.texts[session.language]}"/></a></span>
                                </c:forEach></div>
                                <div id="keywordctl"><a class="button" href="addKeyword"><fmt:message key="event.add-keyword.label" bundle="${page.bundle}"/></a></div>
                            </td>
                        </tr>
                        <tr>
                            <td>*<fmt:message key="event.language.label" bundle="${page.bundle}"/>: </td>
                            <td><select size="1" name="language" id="language">
<c:forEach var="lng" items="${page.allLanguages}">
    <option value="${lng.code}" <c:if test="${lng.code==page.language}">selected</c:if>><c:out value="${lng.labels[session.language]}"/></option>
</c:forEach>
                            </select></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="event.price.label" bundle="${page.bundle}"/>: </td>
                            <fmt:formatNumber var="price" value="${page.price}" pattern="0.00"/>
                            <td><input name="price" value="${price}" maxlength="10" size="10"></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="event.attendees.label" bundle="${page.bundle}"/>: </td>
                            <td><input name="maxAttendees" value="${page.maxAttendees}" maxlength="3" size="3"></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="event.address.label" bundle="${page.bundle}"/>: </td>
                            <td><textarea cols="80" id="address" name="address" rows="5"><c:out value="${page.address}"/></textarea></td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <fmt:message var="label" key="event.submit.label" bundle="${page.bundle}"/>
                            <td><input type="submit" value="${label}" name="ok"></td>
                        </tr>
                    </tbody>
                </table>
            </form>
            </div>
            <c:import url="/WEB-INF/common/footer.jsp"/>
        </div>
        <div id="keyworddlg" class="dialog">
            <form action="saveKeyword">
                <input type="hidden" name="id" value="">
                <table>
                    <tbody>
<c:forEach var="lc" items="${helper.site.supportedLanguages}">
    <c:set var="lng" value="${helper.languages[lc]}"/>
                        <tr>
                            <td>*<c:out value="${lng.labels[session.language]}"/>:&nbsp;</td>
                            <td><input name="text_${lc}" value=""></td>
                        </tr>
</c:forEach>
                        <tr>
                            <td>&nbsp;</td>
                            <fmt:message var="label" key="event.save-keyword.label" bundle="${page.bundle}"/>
                            <td><input type="submit" value="${label}" name="ok"></td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
        <c:import url="/WEB-INF/common/scripts.jsp"/>
        <c:import url="/WEB-INF/common/profile-dialog.jsp"/>
        <script type="text/javascript">
            $(function() {
                function editKeyword() {
                    var href = $(this).attr("href");
                    var re = /editKeyword[?]id=([0-9]+)/;
                    var res = re.exec(href);
                    if (!res) {
                        return true;
                    }
                    var id = res[1];
                    $.ajax({
                        type: "POST",
                        url: "getKeywordx",
                        data: "id=" + id,
                        dataType: "json",
                        success: function(data) {
                            $("#keyworddlg input[name='id']").val(data.id);
<c:forEach var="lc" items="${helper.site.supportedLanguages}">
                            if (data.texts['${lc}']) {
                                $("#keyworddlg input[name='text_${lc}']").val(data.texts['${lc}']);
                            } else {
                                $("#keyworddlg input[name='text_${lc}']").val("");
                            }
</c:forEach>
                            $("#keyworddlg").dialog({
                                width: 480,
                                modal: true,
                                close: function() {
                                    $("#keyworddlg").dialog("destroy");
                                }
                            });
                        }
                    });
                    return false;
                }

                // Initialize the editor.
                // Callback function can be passed and executed after full instance creation.
                $("#address").ckeditor({
                    toolbar: [
                        ['Undo','Redo'],
                        ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
                        ['Link','Unlink'],
                        ['Smiley','SpecialChar']
                    ],
                    skin: 'v2',
/*                    contentsCss: '../css/stylesheet.css',*/
                    language: '${session.language}',
                    contentsLanguage: '${session.language}',
                    scayt_sLang: '${session.language}_CH',
                    ignoreEmptyParagraph: true,
                    fullPage: false,
                    resize_enabled: false,
                    width: '480px'
                });
                $("a.user").click(function() {
                    showProfileDialog("");
                });
                $(".keyword a").click(editKeyword);
                $("a[href='addKeyword']").click(function() {
                    $("#keyworddlg input[name='id']").val("-1");
<c:forEach var="lc" items="${helper.site.supportedLanguages}">
                    $("#keyworddlg input[name='text_${lc}']").val("");
</c:forEach>
                    $("#keyworddlg").dialog({
                        width: 480,
                        modal: true,
                        close: function() {
                            $("#keyworddlg").dialog("destroy");
                        }
                    });
                    return false;
                });
                $("#keyworddlg form").submit(function() {
                    $.ajax({
                        type: "POST",
                        url: "saveKeyword",
                        data: $(this).serialize(),
                        dataType: "json",
                        success: function(data) {
                            var checked = data.checked ? " checked" : "";
                            var text = data.texts['${session.language}'];
                            if (!text) {
                                text = "";
                            }
                            if (data.isNew) {
                                $("#keywords").append(
                                    "<span class='keyword' id='kw_" + data.id + "'>" +
                                    "<input type='checkbox' name='keyword' value='" + data.id + "'" + checked + "> " +
                                    "<a href='editKeyword?id=" + data.id + "'></a></span> ");
                            }
                            var lnk = $("a[href='editKeyword?id=" + data.id + "']");
                            lnk.text(text);
                            if (data.isNew) {
                                lnk.click(editKeyword);
                            }
                            $("#keyworddlg").dialog("destroy");
                        }
                    });
                    return false;
                });
            });
        </script>
    </body>
</html>
