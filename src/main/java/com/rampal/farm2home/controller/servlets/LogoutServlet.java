package com.rampal.farm2home.controller.servlets;

import com.rampal.farm2home.utilities.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionUtil.removeAttribute(request, "user");
        SessionUtil.invalidateSession(request);
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
