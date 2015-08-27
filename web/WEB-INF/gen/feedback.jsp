<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${session.locale}"/>
<c:set var="email" value=""/>
<c:if test="${not empty helper.user}">
    <c:set var="email" value="${helper.user.email}"/>
</c:if>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="title" bundle="${page.bundle}"/></title>
        <link rel='stylesheet' type='text/css' href='../css/stylesheet.css'>
        <c:import url="/WEB-INF/common/analytics.jsp"/>
    </head>
    <body id="${page.name}" class="feedback">
        <div id="wrapper">
            <div id="content">
                <c:import url="/WEB-INF/common/menu.jsp"/>
                ${page.texts['content']}
                <form id="feedback-form" name="feedback" action="feedback" method="POST">
                    <input id="subtype" type="hidden" name="subtype" value="plain">
                    <table border="0" cellpadding="2" cellspacing="2">
                        <tbody>
                            <tr>
                                <td class="label-col">*<fmt:message key="email.label" bundle="${page.bundle}"/>: </td>
                                <td><input name="email" value="<c:out value="${email}"/>"></td>
                            </tr>
                            <tr>
                                <td class="label-col">*<fmt:message key="subject.label" bundle="${page.bundle}"/>: </td>
                                <td><input id="subject" name="subject" value=""></td>
                            </tr>
                            <tr>
                                <td class="label-col"><fmt:message key="content.label" bundle="${page.bundle}"/>: </td>
                                <td><textarea id="text" rows="20" cols="80" name="text"></textarea></td>
                            </tr>
                            <tr>
                                <td class="label-col">&nbsp;</td>
                                <fmt:message var="label" key="submit.label" bundle="${page.bundle}"/>
                                <td><input type="submit" value="${label}" name="ok"></td>
                            </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <c:import url="/WEB-INF/common/footer.jsp"/>
        </div>
        <c:import url="/WEB-INF/common/scripts.jsp"/>
        <script type="text/javascript">
            $(function() {
                var txtarea = $("#text");
                txtarea.ckeditor({
                    toolbar: [
                        ['Undo','Redo'],
                        ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
                        ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote','CreateDiv'],
                        ['Link','Unlink'],
                        ['Smiley','SpecialChar'],
                    ],
                    skin: 'v2',
/*                    contentsCss: '../css/stylesheet.css', */
                    language: '${session.language}',
                    contentsLanguage: '${session.language}',
                    scayt_sLang: '${session.language}_CH',
                    ignoreEmptyParagraph: true,
                    fullPage: false,
                    resize_enabled: false,
                    width: '480px'
                });
                $("#subtype").val("html");
            });
        </script>
        <c:import url="/WEB-INF/common/inplace.jsp"/>
    </body>
</html>
