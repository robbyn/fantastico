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
        <title><fmt:message key="title" bundle="${page.bundle}"/></title>
        <link rel='stylesheet' type='text/css' href='../css/stylesheet.css'>
        <link rel="alternate" type="application/rss+xml" title="<fmt:message key="title" bundle="${page.bundle}"/>" href="../rss/get?language=${page.language}">
        <link rel="image_src" href="${request.baseUrl}/img/logo.jpg" />
        <c:import url="/WEB-INF/common/analytics.jsp"/>
    </head>
    <body id="${page.name}">
        <div id="wrapper">
            <div id="content">
                <c:import url="/WEB-INF/common/menu.jsp"/>
                <div id="top-text">${page.texts['top']}</div>
                <c:import url="/WEB-INF/upcoming.jsp"/>
<!-- AddThis Button BEGIN -->
<div class="addthis_toolbox addthis_default_style addthis_32x32_style">
<a class="addthis_button_facebook"></a>
<a class="addthis_button_twitter"></a>
<a class="addthis_button_email"></a>
<a class="addthis_button_compact"></a>
</div>
<script type="text/javascript">var addthis_config = {"data_track_clickback":true};</script>
<script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#username=lfantastico"></script>
<!-- AddThis Button END -->
                <div id="bottom-text">${page.texts['bottom']}</div>
            </div>
            <c:import url="/WEB-INF/common/footer.jsp"/>
        </div>
        <c:import url="/WEB-INF/common/scripts.jsp"/>
        <c:import url="/WEB-INF/common/inplace.jsp"/>
    </body>
</html>
