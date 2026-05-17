package com.rampal.farm2home.filter;

import com.rampal.farm2home.model.User;
import com.rampal.farm2home.utilities.SessionUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String path = req.getRequestURI().substring(contextPath.length());

        if (isPublicResource(path) || isPublicPage(path) || !isProtectedPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        User user = (User) SessionUtil.getAttribute(req, SessionUtil.USER_SESSION_KEY);
        if (user == null) {
            res.sendRedirect(contextPath + "/login");
            return;
        }

        if ("/login".equals(path) || "/register".equals(path)) {
            res.sendRedirect(contextPath + "/home");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isProtectedPath(String path) {
        return path.startsWith("/admin") || "/logout".equals(path)
                || "/orders".equals(path) || "/checkout".equals(path);
    }

    private boolean isPublicPage(String path) {
        return "/".equals(path) || "/index.jsp".equals(path) || "/home".equals(path)
                || "/login".equals(path) || "/register".equals(path)
                || "/login.jsp".equals(path) || "/register.jsp".equals(path)
                || "/home.jsp".equals(path) || "/error404.jsp".equals(path);
    }

    private boolean isPublicResource(String path) {
        return path.startsWith("/css/") || path.startsWith("/images/")
                || path.startsWith("/resources/") || path.startsWith("/uploads/")
                || path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg")
                || path.endsWith(".gif") || path.endsWith(".svg") || path.endsWith(".css");
    }
}
