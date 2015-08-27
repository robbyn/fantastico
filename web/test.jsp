<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel='stylesheet' type='text/css' href='css/stylesheet.css'>
    </head>
    <body>
        <table>
            <c:forEach var="r" begin="${1}" end="${50}">
            <tr>
                <c:if test="${r == 1}">
                <td rowspan="50">
                    <c:forEach var="l" begin="${1}" end="${50}">
                    <div class="draggable female">${l}.2</div>
                    </c:forEach>
                </td>
                <td rowspan="50">
                    <c:forEach var="l" begin="${1}" end="${50}">
                    <div class="draggable male">${l}.2</div>
                    </c:forEach>
                </td>
                </c:if>
                <c:forEach var="c" begin="${1}" end="${5}">
                    <td><div class="droppable">XXXX</div></td>
                </c:forEach>
            </tr>
            </c:forEach>
        </table>
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="js/jquery-ui-1.8.4.custom.min.js"></script>
        <script type="text/javascript">
            $(function() {
                $(".draggable").draggable({ revert: 'invalid' });
                $(".droppable").droppable({
                    drop: function(event, ui) {
                        $(this).text(ui.draggable.text());
                        $(this).ch
                        if (ui.draggable.hasClass("male")) {
                            $(this).addClass("male");
                            $(this).removeClass("female");
                        }
                        if (ui.draggable.hasClass("female")) {
                            $(this).addClass("female");
                            ui.draggable.removeClass("male");
                        }
                        $(this).addClass("draggable");
                        $(this).draggable({ revert: 'invalid' });
                        if (ui.draggable.hasClass("droppable")) {
                            ui.draggable.removeClass("male");
                            ui.draggable.removeClass("female");
                            ui.draggable.removeClass("draggable");
                            ui.draggable.draggable("destroy");
                            ui.draggable.text("XXXX");
                        } else {
                            ui.draggable.remove();
                        }
                    }
                });
            });
        </script>
    </body>
</html>
