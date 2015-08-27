<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${session.locale}"/>
<c:url var="url" value="../browser/file">
    <c:param name="name" value="${page.fileName}"/>
</c:url>
<html>
    <body>
        <script type="text/javascript" src="../js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript">
            var fct = ${page.funcNumber};
            window.parent.CKEDITOR.tools.callFunction(
                    fct, '${url}', '');
        </script>
    </body>
</html>
