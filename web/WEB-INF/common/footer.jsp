<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<jsp:useBean id="now" class="java.util.Date"/>
<div id="footer">
    <div id="copyright">
        &copy;2010-<fmt:formatDate value="${now}" pattern="yyyy"/> &ndash; L. Fantastico &ndash; Soir&eacute;e Speed Dating &ndash; Gen&egrave;ve &ndash; Lausanne
    </div>
    <div id="credit">
        <a href="http://www.studiostrob.ch" title="Besoin d'un site? Contactez-nous pour une offre gratuite et sans engagement" target="_blank">Webdesign Studiostrob Gen&egrave;ve</a>
    </div>
    <div class="clear"></div>
</div>
