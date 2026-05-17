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

@WebFilter(urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        User user = (User) SessionUtil.getAttribute(req, SessionUtil.USER_SESSION_KEY);
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            SessionUtil.setFlashMessage(req, SessionUtil.FLASH_ERROR, "You do not have permission to access the admin dashboard.");
            res.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        chain.doFilter(request, response);
    }
}
