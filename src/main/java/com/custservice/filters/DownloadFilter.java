package com.custservice.filters;

import com.cust.domain.vo.ElegantUser;
import java.io.IOException;
import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//@WebFilter(filterName = "downloadFilter", urlPatterns = {"/*"})
public class DownloadFilter implements Filter {

    private static final String AJAX_REDIRECT_XML = "<partial-response><redirect url=\"%s\"></redirect></partial-response>";

    public DownloadFilter() {
    }

    public void destroy() {
        System.out.println("Download Filter Destroyed");
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        ElegantUser user = null;
        try {

            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            HttpSession session = req.getSession(false);
            String loginURL = req.getContextPath() + "/pges/loginpanel.irp";

            boolean loggedIn = (session != null) && (session.getAttribute("loggedInUser") != null);
            boolean loginRequest = req.getRequestURI().equals(loginURL);
            boolean resourceRequest = req.getRequestURI().startsWith(req.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER + "/");
            boolean ajaxRequest = "partial/ajax".equals(req.getHeader("Faces-Request"));

            if (loggedIn || loginRequest || resourceRequest) {
                if (!resourceRequest) { // Prevent browser from caching restricted resources. See also http://stackoverflow.com/q/4194207/157882
                    res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                    res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                    res.setDateHeader("Expires", 0); // Proxies.
                }

                chain.doFilter(request, response); // So, just continue request.
            } else if (ajaxRequest) {
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().printf(AJAX_REDIRECT_XML, loginURL); // So, return special XML response instructing JSF ajax to send a redirect.
            } else {
                res.sendRedirect(loginURL); // So, just perform standard synchronous redirect.
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void init(FilterConfig fConfig) throws ServletException {
        System.out.println("Download Filter initialized");
    }

    private boolean resourceURIFound(HttpServletRequest re) {
        boolean found = false;
        System.out.println(re.getRequestURI());
        found = re.getRequestURI().contains(".css") || re.getRequestURI().contains(".js") || re.getRequestURI().contains(".png") || re.getRequestURI().contains(".gif");
        return found;
    }
}
