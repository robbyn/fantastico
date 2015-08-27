package org.tastefuljava.mvc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Request {
    private static final Logger LOG
            = LoggerFactory.getLogger(Request.class);
    private static final ThreadLocal<Request> CURRENT
            = new ThreadLocal<Request>();
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private ServletContext context;
    private String pageName;
    private String action;

    public static Request create(HttpServletRequest request,
            HttpServletResponse response, ServletContext context) {
        Request cxt = new Request(request, response, context);
        CURRENT.set(cxt);
        return cxt;
    }

    public static Request current() {
        return CURRENT.get();
    }

    private Request(HttpServletRequest request,
            HttpServletResponse response, ServletContext context) {
        this.req = request;
        this.resp = response;
        this.context = context;
        String path = req.getServletPath();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        int pos = path.indexOf('/');
        if (pos < 0) {
            pageName = path;
            action = null;
        } else {
            pageName = path.substring(0, pos);
            action = path.substring(pos+1);
        }
    }

    public void close() {
        CURRENT.remove();
    }

    public Page getPage(String name) {
        return PageRegistry.getPage(req.getSession(), name);
    }

    public Locale getLocale() {
        return req.getLocale();
    }

    public String getServerName() {
        String hostName = req.getHeader("X-Forwarded-Host");
        if (hostName == null) {
            hostName = req.getServerName();
        }
        return hostName;
    }

    public String getHostName() {
        String hostName = req.getHeader("X-Forwarded-Host");
        if (hostName == null) {
            int port = req.getServerPort();
            if (req.getScheme().equals("http") && port == 80
                    || req.getScheme().equals("https") && port == 443) {
                hostName = req.getServerName();
            } else {
                hostName = req.getServerName() + ":" + port;
            }
        }
        return hostName;
    }

    public String getPageName() {
        return pageName;
    }

    public String getBaseUrl() {
        return req.getScheme() + "://" + getHostName() + req.getContextPath();
    }

    public Object getAttribute(String name) {
        return req.getAttribute(name);
    }

    public void setAttribute(String name, Object obj) {
        if (obj != null) {
            req.setAttribute(name, obj);
        } else {
            req.removeAttribute(name);
        }
    }

    public Object getSessionAttribute(String name) {
        HttpSession sess = req.getSession(false);
        return sess == null ? null : sess.getAttribute(name);
    }

    public void setSessionAttribute(String name, Object obj) {
        if (obj != null) {
            HttpSession sess = req.getSession();
            sess.setAttribute(name, obj);
        } else {
            HttpSession sess = req.getSession(false);
            if (sess != null) {
                sess.removeAttribute(name);
            }
        }
    }

    public String getParameter(String name) {
        return req.getParameter(name);
    }

    public Iterable<String> getParameterNames() {
        return new Iterable<String>() {
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    @SuppressWarnings("unchecked")
                    Enumeration<String> enm = req.getParameterNames();

                    public boolean hasNext() {
                        return enm.hasMoreElements();
                    }

                    public String next() {
                        return enm.nextElement();
                    }

                    public void remove() {
                        throw new UnsupportedOperationException(
                                "Not supported");
                    }
                };
            }
        };
    }

    public boolean dispatch() throws IOException, ServletException {
        if (pageName == null || pageName.equals("")) {
            redirect("/main/start");
            return true;
        }
        Page page = PageRegistry.getPage(req.getSession(), pageName);
        if (page == null) {
            return false;
        } else {
            req.setAttribute("page", page);
            if (action == null) {
                redirect("");
                return true;
            } else if (action.equals("")) {
                page.forward();
                return true;
            } else {
                try {
                    return methodDispatch(page);
                } catch (ParseException e) {
                    LOG.error("Error dispatching action " + action, e);
                    throw new IOException(e.getMessage());
                } catch (IllegalAccessException e) {
                    LOG.error("Error dispatching action " + action, e);
                    throw new IOException(e.getMessage());
                } catch (InvocationTargetException e) {
                    LOG.error("Error dispatching action " + action, e);
                    throw new IOException(e.getMessage());
                }
            }
        }
    }

    public void forward(String url) throws ServletException, IOException {
        RequestDispatcher disp = req.getRequestDispatcher("/WEB-INF/" + url);
        disp.forward(req, resp);
    }

    public void redirect() throws IOException {
        resp.sendRedirect("");
    }

    public void redirect(String url) throws IOException {
        String pfx = req.getContextPath();
        if (url.startsWith("/")) {
            url = pfx + url;
        } else {
            url = pfx + "/" + pageName + "/" + url;
        }
        resp.sendRedirect(url);
    }

    public String getRemoteUser() {
        return req.getRemoteUser();
    }

    public boolean hasRole(String role) {
        return req.isUserInRole(role);
    }

    public Map<String,Boolean> getRoles() {
        return new QuickMap<String,Boolean>() {
            @Override
            public Boolean get(Object key) {
                return hasRole((String)key);
            }
        };
    }

    public OutputStream getOutputStream(String mimeType) throws IOException {
        resp.setContentType(mimeType);
        return resp.getOutputStream();
    }

    public PrintWriter getWriter(String mimeType, String encoding)
            throws IOException {
        resp.setContentType(mimeType);
        resp.setCharacterEncoding(encoding);
        return resp.getWriter();
    }

    public JSonWriter getJSonWriter(String encoding, boolean format)
            throws IOException {
        return new JSonWriter(getWriter("application/json", encoding), format);
    }

    public void save(Object obj) {
        Set<String> names = new HashSet<String>();
        @SuppressWarnings("unchecked")
        Enumeration<String> enm = req.getParameterNames();
        while (enm.hasMoreElements()) {
            names.add(enm.nextElement());
        }
        Class clazz = obj.getClass();
        while (clazz != Object.class && !names.isEmpty()) {
            for (Field f: clazz.getDeclaredFields()) {
                if (!Modifier.isTransient(f.getModifiers())) {
                    String name = f.getName();
                    String pattern = null;
                    if (f.isAnnotationPresent(Param.class)) {
                        Param par = f.getAnnotation(Param.class);
                        name = par.value();
                        pattern = par.pattern();
                    }
                    if (names.contains(name)) {
                        try {
                            names.remove(name);
                            f.setAccessible(true);
                            Object val = convertValue(f.getType(),
                                    name, pattern);
                            f.set(obj, val);
                        } catch (ParseException ex) {
                            LOG.error("Error assigning field", ex);
                        } catch (IllegalAccessException ex) {
                            LOG.error("Error assigning field", ex);
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    public Iterable<FileItemStream> getItems() {
        if (!ServletFileUpload.isMultipartContent(req)) {
            throw new RuntimeException(
                    "Expecting a multipart/form-data enctype");
        }
        return new Iterable<FileItemStream>() {
            public Iterator<FileItemStream> iterator() {
                return new Iterator<FileItemStream>() {
                    private final FileItemIterator iter;
                    {
                        try {
                            ServletFileUpload upload = new ServletFileUpload();
                            iter = upload.getItemIterator(req);
                        } catch (FileUploadException ex) {
                            LOG.error("Error creating iterator", ex);
                            throw new RuntimeException(ex.getMessage());
                        } catch (IOException ex) {
                            LOG.error("Error creating iterator", ex);
                            throw new RuntimeException(ex.getMessage());
                        }
                    }

                    public boolean hasNext() {
                        try {
                            return iter.hasNext();
                        } catch (FileUploadException ex) {
                            LOG.error("Error creating iterator", ex);
                            throw new RuntimeException(ex.getMessage());
                        } catch (IOException ex) {
                            LOG.error("Error creating iterator", ex);
                            throw new RuntimeException(ex.getMessage());
                        }
                    }

                    public FileItemStream next() {
                        try {
                            return iter.next();
                        } catch (FileUploadException ex) {
                            LOG.error("Error creating iterator", ex);
                            throw new RuntimeException(ex.getMessage());
                        } catch (IOException ex) {
                            LOG.error("Error creating iterator", ex);
                            throw new RuntimeException(ex.getMessage());
                        }
                    }

                    public void remove() {
                        throw new UnsupportedOperationException("remove");
                    }
                };
            }
        };
    }

    public String getMimeType(String name) {
        return context.getMimeType(name);
    }

    private boolean methodDispatch(Page page)
            throws IllegalAccessException, InvocationTargetException,
            ParseException {
        for (Method method: page.getClass().getMethods()) {
            if (method.getName().equals(action)) {
                Annotation anots[][] = method.getParameterAnnotations();
                Class<?> parmTypes[] = method.getParameterTypes();
                if (anots.length != parmTypes.length) {
                    throw new RuntimeException(
                            "Wrong number of arguments to request method "
                            + method);
                }
                Object actualParms[] = new Object[parmTypes.length];
                for (int i = 0; i < parmTypes.length; ++i) {
                    Param par = null;
                    for (Annotation a: anots[i]) {
                        if (a instanceof Param) {
                            par = (Param)a;
                        }
                    }
                    if (par == null) {
                        actualParms[i] = null;
                    } else {
                        actualParms[i] = convertValue(parmTypes[i], par.value(),
                                par.pattern());
                    }
                }
                method.invoke(page, actualParms);
                return true;
            }
        }
        return false;
    }

    private Object convertValue(Class<?> type,
            String name, String pattern) throws ParseException {
        if (type.isArray()) {
            Class<?> elemType = type.getComponentType();
            String strings[] = req.getParameterValues(name);
            int len = strings == null ? 0 : strings.length;
            Object array = Array.newInstance(elemType, len);
            for (int i = 0; i < len; ++i) {
                Object val = Conversion.parse(elemType, strings[i], pattern);
                Array.set(array, i, val);
            }
            return array;
        } else {
            String s = req.getParameter(name);
            if (s == null) {
                return null;
            }
            return Conversion.parse(type, s, pattern);
        }
    }
}
