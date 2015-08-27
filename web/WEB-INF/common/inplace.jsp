<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:if test="${session.editMode}">
<div class="dialog" id="textedit" title="Edit text">
    <form action="setPageText" method="post">
        <input id="text-key" type="hidden" name="key" value=""/>
        <div><textarea id="text-value" name="text" cols="80" rows="12"></textarea></div>
        <div><input type="submit" name="submit" value="OK"></div>
    </form>
</div>
<script type="text/javascript">
    $(function() {
        $("#text-value").ckeditor({
            toolbar: [
                ['Source'],
                ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
                ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
                ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
                ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote','CreateDiv'],
                ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
                ['Link','Unlink','Anchor'],
                ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
                ['Format'],
                ['Maximize', 'ShowBlocks','-','About']
            ],
            skin: 'v2',
            filebrowserImageBrowseUrl : '../browser/browse',
            filebrowserImageUploadUrl : '../browser/upload',
/*            contentsCss: '../css/stylesheet.css', */
            language: '${session.language}',
            contentsLanguage: '${session.language}',
            scayt_sLang: '${session.language}_CH',
            ignoreEmptyParagraph: true,
            fullPage: false,
            resize_enabled: false
        });
        $(".editable>a").click(function() {
            var href = $(this).attr("href");
            var re = /(editPageText|editSiteText)[?]key=([A-Za-z0-9_\-.]+)/;
            var res = re.exec(href);
            if (!res) {
                return true;
            }
            if (res[1] === "editPageText") {
                $("#textedit form").attr("action", "setPageText");
            } else {
                $("#textedit form").attr("action", "setSiteText");
            }
            $("#text-key").val(res[2]);
            $("#text-value").val($(this).parent().children("div").html());
            $("#textedit").dialog({
                width: 640,
                modal: false,
                close: function() {
                    $("#textedit").dialog("destroy");
                }
            });
            return false;
        });
    });
</script>
</c:if>
