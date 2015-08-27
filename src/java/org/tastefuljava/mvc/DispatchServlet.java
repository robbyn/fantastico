package org.tastefuljava.mvc;

import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatchServlet extends HttpServlet {
    private static final Logger LOG
            = LoggerFactory.getLogger(DispatchServlet.class);
    private static final Pattern STATIC_PATTERN
            = Pattern.compile("^.*\\.(css|js|html|jpg|png|gif)$",
                    Pattern.CASE_INSENSITIVE);

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        if (LOG.isDebugEnabled()) {
            dumpRequest(request);
        }
        String path = request.getServletPath();
        if (isStatic(path)) {
            forwardToServlet(request, response, "default");
        } else {
            response.setHeader("Cache-Control", "no-cache");
            response.addHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");
            Request req = Request.create(request, response,
                    getServletContext());
            try {
                request.setAttribute("request", req);
                handle(req);
            } finally {
                req.close();
            }
        }
    }

    protected boolean isStatic(String path) {
        return STATIC_PATTERN.matcher(path).matches();
    }

    protected void handle(Request req)
            throws IOException, ServletException {
        req.dispatch();
    }

    protected void forwardToServlet(HttpServletRequest request,
            HttpServletResponse response, String name)
            throws ServletException, IOException {
        ServletContext context = getServletContext();
        RequestDispatcher disp = context.getNamedDispatcher(name);
        disp.forward(request, response);
    }

    private void dumpRequest(HttpServletRequest request) {
        LOG.debug("RemoteUser:  " + request.getRemoteUser());
        LOG.debug("ContextPath: " + request.getContextPath());
        LOG.debug("ServletPath: " + request.getServletPath());
        LOG.debug("PathInfo:    " + request.getPathInfo());
        dumpHeaders(request);
        dumpParams(request);
    }

    private void dumpHeaders(HttpServletRequest request) {
        LOG.debug("Headers:");
        @SuppressWarnings("unchecked")
        Enumeration<String> enm = request.getHeaderNames();
        while (enm.hasMoreElements()) {
            String name = enm.nextElement();
            @SuppressWarnings("unchecked")
            Enumeration<String> enm2 = request.getHeaders(name);
            while (enm2.hasMoreElements()) {
                String value = enm2.nextElement();
                LOG.debug("    " + name + ": " + value);
            }
        }
    }

    private void dumpParams(HttpServletRequest request) {
        LOG.debug("Parameters:");
        @SuppressWarnings("unchecked")
        Enumeration<String> enm = request.getParameterNames();
        while (enm.hasMoreElements()) {
            String name = enm.nextElement();
            for (String value: request.getParameterValues(name)) {
                LOG.debug("    " + name + ": " + value);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
