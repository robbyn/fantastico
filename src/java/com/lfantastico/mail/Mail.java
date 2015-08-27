package com.lfantastico.mail;

import com.lfantastico.domain.MailTemplate;
import com.lfantastico.util.DateFormatter;
import com.lfantastico.util.Slf4jLogChute;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mail {
    private static final Logger LOG = LoggerFactory.getLogger(Mail.class);

    public static void send(MailTemplate template, Map<String,?> params) {
        try {
            VelocityContext context = createContext(params);
            VelocityEngine engine = new VelocityEngine();
            engine.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
            engine.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
            engine.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM,
                    new Slf4jLogChute(LOG));
            engine.init();
            String subtype = template.getSubtype();
            String from = evaluate(engine, context, template.getFrom());
            String to[] = evaluate(engine, context, template.getTos());
            String bcc[] = evaluate(engine, context, template.getBccs());
            String subject = evaluate(engine, context, template.getSubject());
            String body = evaluate(engine, context, template.getBody());
            send(from, to, bcc, subject, body, subtype);
        } catch (Exception e) {
            LOG.error("Error sending e-mail", e);
        }
    }

    public static void send(String from, String to[], String bcc[],
            String subject, String text, String subtype) {
        try {
            Session sess = getSession();
            MimeMessage message = new MimeMessage(sess);
            message.setSubject(subject, "ISO-8859-1");
            message.setText(text, "ISO-8859-1", subtype);
            if (from == null) {
                message.setFrom();
            } else {
                message.setFrom(new InternetAddress(from));
            }
            if (to == null) {
                String dest = sess.getProperty("mail.smtp.from");
                message.addRecipient(RecipientType.TO,
                        new InternetAddress(dest));
            } else {
                for (String dest: to) {
                    message.addRecipient(RecipientType.TO,
                            new InternetAddress(dest));
                }
            }
            if (bcc != null) {
                for (String dest: bcc) {
                    message.addRecipient(RecipientType.BCC,
                            new InternetAddress(dest));
                }
            }
            send(sess, message);
        } catch (MessagingException e) {
            LOG.error("Error sending e-mail", e);
        } catch (NamingException e) {
            LOG.error("Error sending e-mail", e);
        }
    }

    private static String evaluate(VelocityEngine engine,
            VelocityContext context, String input) throws Exception {
        StringWriter out = new StringWriter();
        engine.evaluate(context, out, "EVAL", input);
        return out.toString();
    }

    private static String[] evaluate(VelocityEngine engine,
            VelocityContext context, Collection<String> input)
            throws Exception {
        String result[] = input.toArray(new String[input.size()]);
        for (int i = 0; i < result.length; ++i) {
            result[i] = evaluate(engine, context, result[i]);
        }
        return result;
    }

    private static Session getSession() throws NamingException {
        Context context = new InitialContext();
        return (Session)context.lookup("java:comp/env/mail/session");
    }

    private static void send(Session sess, Message message)
            throws MessagingException {
        if (!"true".equals(sess.getProperty("mail.disabled"))) {
            Transport trans = sess.getTransport("smtp");
            try {
                if ("true".equalsIgnoreCase(sess.getProperty("mail.smtp.auth"))) {
                    String host = sess.getProperty("mail.smtp.host");
                    String port = sess.getProperty("mail.smtp.port");
                    String user = sess.getProperty("mail.smtp.user");
                    String password = sess.getProperty("mail.smtp.password");
                    int prt = port == null ? -1 : Integer.parseInt(port);
                    trans.connect(host, prt, user, password);
                } else {
                    trans.connect();
                }
                trans.sendMessage(message,
                    message.getRecipients(Message.RecipientType.TO));
            } finally {
                trans.close();
            }
        }
    }

    private static VelocityContext createContext(Map<String,?> params) {
        VelocityContext context = new VelocityContext();
        context.put("dateFormat", new DateFormatter());
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            context.put(entry.getKey(), entry.getValue());
        }
        return context;
    }
}
