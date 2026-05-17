package com.rampal.farm2home.controller.servlets;

import com.rampal.farm2home.dao.UserDAO;
import com.rampal.farm2home.model.User;
import com.rampal.farm2home.utilities.CartUtil;
import com.rampal.farm2home.utilities.CookieUtil;
import com.rampal.farm2home.utilities.PasswordUtil;
import com.rampal.farm2home.utilities.SessionUtil;
import com.rampal.farm2home.utilities.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("savedEmail", CookieUtil.getCookieValue(request, "rememberUser"));
        request.setAttribute("flashSuccess", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_SUCCESS));
        request.setAttribute("flashError", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_ERROR));
        request.setAttribute("redirect", request.getParameter("redirect"));
        request.setAttribute("cartCount", CartUtil.getCartCount(request));
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        String redirect = request.getParameter("redirect");

        request.setAttribute("savedEmail", email);
        request.setAttribute("redirect", redirect);

        if (ValidationUtil.isNullOrEmpty(email) || ValidationUtil.isNullOrEmpty(password)) {
            request.setAttribute("error", "Email and password are required.");
            request.setAttribute("cartCount", CartUtil.getCartCount(request));
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            User user = userDAO.findByEmail(email.trim().toLowerCase());
            if (user == null) {
                request.setAttribute("error", "You need to Register First to login ");
                request.setAttribute("cartCount", CartUtil.getCartCount(request));
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }
            if (!"active".equalsIgnoreCase(user.getStatus()) || !isValidPassword(user, password)) {
                request.setAttribute("error", "Invalid login credentials.");
                request.setAttribute("cartCount", CartUtil.getCartCount(request));
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            SessionUtil.setAttribute(request, SessionUtil.USER_SESSION_KEY, user);
            SessionUtil.setAttribute(request, "user", user);

            if (rememberMe != null) {
                Cookie userCookie = new Cookie("rememberUser", user.getEmail());
                userCookie.setMaxAge(60 * 60 * 24 * 7);
                userCookie.setHttpOnly(true);
                userCookie.setSecure(request.isSecure());
                userCookie.setPath("/");
                response.addCookie(userCookie);
            } else {
                Cookie userCookie = new Cookie("rememberUser", "");
                userCookie.setMaxAge(0);
                userCookie.setHttpOnly(true);
                userCookie.setSecure(request.isSecure());
                userCookie.setPath("/");
                response.addCookie(userCookie);
            }

            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, "Welcome back, " + user.getUsername() + ".");
            String destination = resolveRedirect(redirect, user.getRole());
            response.sendRedirect(request.getContextPath() + destination);
        } catch (SQLException | ClassNotFoundException exception) {
            throw new ServletException("Unable to login.", exception);
        }
    }

    private boolean isValidPassword(User user, String password) throws SQLException, ClassNotFoundException {
        String storedPassword = user.getPassword();
        if (storedPassword != null && storedPassword.startsWith("$2")) {
            return PasswordUtil.checkPassword(password, storedPassword);
        }
        boolean matched = storedPassword != null && storedPassword.equals(password);
        if (matched) {
            userDAO.updatePasswordHash(user.getUserId(), PasswordUtil.getHashPassword(password));
        }
        return matched;
    }

    private String resolveRedirect(String redirect, String role) {
        if (!ValidationUtil.isNullOrEmpty(redirect) && redirect.startsWith("/")) {
            return redirect;
        }
        return "admin".equalsIgnoreCase(role) ? "/admin/dashboard" : "/home";
    }
}
