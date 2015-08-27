package org.tastefuljava.mvc;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageRegistry {
    private static final Logger LOG
            = LoggerFactory.getLogger(PageRegistry.class);
    private Map<String,Class<? extends Page>> definitions
            = new HashMap<String,Class<? extends Page>>();

    public static PageRegistry getInstance(ServletContext context) {
        PageRegistry result
                = (PageRegistry)context.getAttribute("page-registry");
        if (result == null) {
            result = new PageRegistry();
            context.setAttribute("page-registry", result);
        }
        return result;
    }

    public static Page getPage(HttpSession sess, String name) {
        try {
            Page result = (Page)sess.getAttribute(name);
            if (result == null) {
                ServletContext context = sess.getServletContext();
                PageRegistry reg = getInstance(context);
                Class<? extends Page> clazz = reg.get(name);
                if (clazz == null) {
                    return null;
                }
                result = clazz.newInstance();
                result.setName(name);
                sess.setAttribute(name, result);
            }
            return result;
        } catch (InstantiationException ex) {
            LOG.error("Error instanciating page " + name, ex);
            throw new RuntimeException(ex.getMessage());
        } catch (IllegalAccessException ex) {
            LOG.error("Error instanciating page " + name, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void register(String name, Class<? extends Page> clazz) {
        definitions.put(name, clazz);
    }

    public Class<? extends Page> get(String name) {
        return definitions.get(name);
    }
}
