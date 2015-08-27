package com.lfantastico.web;

import org.tastefuljava.mvc.JSonWriter;
import com.lfantastico.domain.MailTemplate;
import com.lfantastico.domain.SiteVersion;
import java.io.IOException;
import java.util.Map;
import org.tastefuljava.mvc.Param;
import org.tastefuljava.mvc.Request;

public class EmailPage extends BasePage {
    private String currentType;

    public String getCurrentType() {
        if (currentType == null) {
            currentType = Helper.current().getMailTemplateNames()[0];
        }
        return currentType;
    }

    public Map<String,MailTemplate> getMailTemplates() {
        return Helper.current().getMailTemplates(getLanguage());
    }

    public void save(@Param("type") String type,
            @Param("subtype") String subtype,
            @Param("from") String from,
            @Param("to") String to,
            @Param("bcc") String bcc,
            @Param("subject") String subject,
            @Param("body") String body) throws IOException {
        commonSave(currentType, subtype, from, to, bcc, subject, body);
        currentType = type;
        redirect();
    }

    public void savex(@Param("type") String type,
            @Param("subtype") String subtype,
            @Param("from") String from,
            @Param("to") String to,
            @Param("bcc") String bcc,
            @Param("subject") String subject,
            @Param("body") String body,
            @Param("new-type") String newType) throws IOException {
        commonSave(type, subtype, from, to, bcc, subject, body);
        Helper helper = Helper.current();
        MailTemplate template = helper.getMailTemplate(getLanguage(), newType);
        JSonWriter out = Request.current().getJSonWriter("UTF-8", true);
        try {
            out.startObject();
            if (template != null) {
                out.printField("from", template.getFrom());
                out.printField("to", template.getToStr());
                out.printField("bcc", template.getBccStr());
                out.printField("subject", template.getSubject());
                out.printField("body", template.getBody());
            }
            out.endBlock();
        } finally {
            out.close();
        }
    }

    private void commonSave(String type, String subtype, String from, String to,
            String bcc, String subject, String body) {
        Helper helper = Helper.current();
        SiteVersion version = helper.getSiteVersion(getLanguage());
        MailTemplate template = version.getMailTemplate(type);
        if (template == null) {
            template = new MailTemplate();
            version.setMailTemplate(type, template);
        }
        template.setSubtype(subtype);
        template.setFrom(from);
        template.setToStr(to);
        template.setBccStr(bcc);
        template.setSubject(subject);
        template.setBody(body);
        helper.commit();
    }
}
