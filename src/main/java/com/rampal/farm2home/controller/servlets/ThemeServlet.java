package com.rampal.farm2home.controller.servlets;

import com.rampal.farm2home.utilities.CookieUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ThemeServlet", urlPatterns = "/theme")
public class ThemeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        updateTheme(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        updateTheme(request, response);
    }

    private void updateTheme(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String selectedTheme = request.getParameter("theme");
        if (!"dark".equalsIgnoreCase(selectedTheme)) {
            selectedTheme = "light";
        }
        CookieUtil.addCookie(request, response, "theme", selectedTheme.toLowerCase(), 60 * 60 * 24 * 30, true);

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            response.sendRedirect(referer);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/home");
    }
}
