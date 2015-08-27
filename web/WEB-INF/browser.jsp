<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${session.locale}"/>
<c:set var="names" value="${page.fileNames}"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="title" bundle="${page.bundle}"/></title>
        <link rel='stylesheet' type='text/css' href='../css/stylesheet.css'>
        <style type="text/css">
            #imgpan {
                width: 360px;
                height: 360px;
            }
        </style>
        <c:import url="/WEB-INF/common/analytics.jsp"/>
    </head>
    <body id="${page.name}">
        <h1><fmt:message key="title" bundle="${page.bundle}"/></h1>
        <form id="browserForm" action="change" method="POST">
            <table>
                <tbody>
                    <tr>
                        <td><select id="list" name="list" size="16">
<c:forEach var="name" items="${names}">
    <c:url var="url" value="../browser/file">
        <c:param name="name" value="${name}"/>
    </c:url>
    <option value="${url}"><c:out value="${name}"/></option>
</c:forEach>
                            </select></td>
                        <td><div id="imgpan"><img id="image" width="360" height="360" src="" alt=""></div></td>
                    </tr>
                    <tr><td colspan="2"><input type="submit" name="ok" id="ok" value="OK"></td></tr>
                </tbody>
            </table>
        </form>
        <c:import url="/WEB-INF/common/scripts.jsp"/>
        <script type="text/javascript">
            $(function() {
                $("#browserForm").submit(function() {
                    window.opener.CKEDITOR.tools.callFunction(
                        ${page.funcNumber}, $("#list").val());
                    window.close();
                    return true;
                });
                $('#list').change(function() {
                    var loadr = new Image();
                    $(loadr).load(function() {
                        var parent = $("#imgpan");
                        var width = $(parent).width();
                        var height = $(parent).height();
                        var imgWidth = this.width;
                        var imgHeight = this.height;
                        if (imgWidth*height < imgHeight*width) {
                            width = Math.floor(imgWidth*height/imgHeight);
                        } else {
                            height = Math.floor(imgHeight*width/imgWidth);
                        }
                        $("#image").attr("src", $(this).attr("src"));
                        $("#image").attr("width", width);
                        $("#image").attr("height", height);
                    });
                    loadr.src = $("#list").val();
                });
            });
        </script>
    </body>
</html>
