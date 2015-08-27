<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${session.locale}"/>
<c:set var="names" value="${helper.mailTemplateNames}"/>
<c:set var="template" value="${page.mailTemplates[page.currentType]}"/>
<c:set var="from" value=""/>
<c:set var="tos" value=""/>
<c:set var="bccs" value=""/>
<c:set var="subject" value=""/>
<c:set var="body" value=""/>
<c:if test="${not empty template}">
    <c:set var="from" value="${template.from}"/>
    <c:set var="tos" value="${template.toStr}"/>
    <c:set var="bccs" value="${template.bccStr}"/>
    <c:set var="subject" value="${template.subject}"/>
    <c:set var="body" value="${template.body}"/>
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
    <body id="${page.name}">
        <div id="wrapper">
            <h1><fmt:message key="title" bundle="${page.bundle}"/></h1>
            <div id="content">
                <c:import url="/WEB-INF/common/menu.jsp"/>
                <form id="form" name="template" action="save" method="POST">
                    <input id="subtype" type="hidden" name="subtype" value="plain">
                    <table border="0" cellpadding="2" cellspacing="2">
                        <tbody>
                        <tr>
                            <td>*<fmt:message key="type.label" bundle="${page.bundle}"/>: </td>
                            <td><select size="1" name="type" id="type">
<c:forEach var="type" items="${names}">
                                <option value="${type}" <c:if test="${page.currentType==type}">selected</c:if>>${type}</option>
</c:forEach>
                                </select></td>
                            </tr>
                            <tr>
                                <td class="label-col">*<fmt:message key="from.label" bundle="${page.bundle}"/>: </td>
                                <td><input id="from" name="from" value="<c:out value="${from}"/>"></td>
                            </tr>
                            <tr>
                                <td class="label-col">*<fmt:message key="to.label" bundle="${page.bundle}"/>: </td>
                                <td><input id="to" name="to" value="<c:out value="${tos}"/>"></td>
                            </tr>
                            <tr>
                                <td class="label-col"><fmt:message key="bcc.label" bundle="${page.bundle}"/>: </td>
                                <td><input id="bcc" name="bcc" value="<c:out value="${bccs}"/>"></td>
                            </tr>
                            <tr>
                                <td class="label-col">*<fmt:message key="subject.label" bundle="${page.bundle}"/>: </td>
                                <td><input id="subject" name="subject" value="<c:out value="${subject}"/>"></td>
                            </tr>
                            <tr>
                                <td class="label-col"><fmt:message key="body.label" bundle="${page.bundle}"/>: </td>
                                <td><textarea id="body" rows="20" cols="80" name="body"><c:out value="${body}"/></textarea></td>
                            </tr>
                            <tr>
                                <td class="label-col">&nbsp;</td>
                                <fmt:message var="label" key="submit.label" bundle="${page.bundle}"/>
                                <td><input type="submit" value="<c:out value="${label}"/>" name="ok"></td>
                            </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <c:import url="/WEB-INF/common/footer.jsp"/>
        </div>
        <c:import url="/WEB-INF/common/scripts.jsp"/>
        <script type="text/javascript">
            function createEditor() {
                $("#body").ckeditor({
                    toolbar: [
                        ['Source'],
                        ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
                        ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
                        ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
                        ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote','CreateDiv'],
                        ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
                        ['Link','Unlink','Anchor'],
                        ['Image','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
                        ['Maximize', 'ShowBlocks','-','About']
                    ],
                    skin: 'v2',
                    language: '${session.language}',
                    contentsLanguage: '${session.language}',
                    scayt_sLang: '${session.language}_CH',
                    fullPage: true,
                    resize_enabled: false
                });
                $("#body").ckeditor(function() {
                    this.dataProcessor.writer.setRules('p',
                    {
                        indent : false,
                        breakBeforeOpen : true,
                        breakAfterOpen : false,
                        breakBeforeClose : false,
                        breakAfterClose : true
                    });
                });
            }
            $(function() {
                window.oldtype = $("#type").val();
                createEditor();
                $("#subtype").val("html");
                $("#type").change(function() {
                    $("form[action='save']").submit();
                });
            });
        </script>
        <c:import url="/WEB-INF/common/inplace.jsp"/>
    </body>
</html>
