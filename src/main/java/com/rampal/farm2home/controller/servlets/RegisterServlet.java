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
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("flashSuccess", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_SUCCESS));
        request.setAttribute("flashError", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_ERROR));
        request.setAttribute("redirect", request.getParameter("redirect"));
        request.setAttribute("cartCount", CartUtil.getCartCount(request));
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String redirect = request.getParameter("redirect");

        request.setAttribute("username", username);
        request.setAttribute("email", email);
        request.setAttribute("phoneNumber", phoneNumber);
        request.setAttribute("redirect", redirect);

        try {
            String error = validateRegistration(username, email, phoneNumber, password, confirmPassword);
            if (error != null) {
                request.setAttribute("error", error);
                request.setAttribute("cartCount", CartUtil.getCartCount(request));
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            User user = new User();
            user.setUsername(username.trim());
            user.setEmail(email.trim().toLowerCase());
            user.setPhoneNumber(phoneNumber.trim());
            user.setPassword(PasswordUtil.getHashPassword(password));
            user.setRole("customer");
            user.setStatus("active");
            userDAO.create(user);

            User createdUser = userDAO.findByEmail(user.getEmail());
            SessionUtil.setAttribute(request, SessionUtil.USER_SESSION_KEY, createdUser);
            SessionUtil.setAttribute(request, "user", createdUser);
            CookieUtil.addCookie(response, "rememberUser", createdUser.getEmail(), 60 * 60 * 24 * 7);
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, "Registration completed successfully.");
            response.sendRedirect(request.getContextPath() + resolveRedirect(redirect));
        } catch (SQLException | ClassNotFoundException exception) {
            throw new ServletException("Unable to register the user.", exception);
        }
    }

    private String validateRegistration(String username, String email, String phoneNumber, String password,
                                        String confirmPassword) throws SQLException, ClassNotFoundException {
        if (ValidationUtil.isNullOrEmpty(username) || ValidationUtil.isNullOrEmpty(email)
                || ValidationUtil.isNullOrEmpty(phoneNumber) || ValidationUtil.isNullOrEmpty(password)
                || ValidationUtil.isNullOrEmpty(confirmPassword)) {
            return "Please fill in all required fields.";
        }
        if (!ValidationUtil.isValidName(username)) {
            return "Name must start with a letter and contain only letters and spaces.";
        }
        if (!ValidationUtil.isValidEmail(email)) {
            return "Please enter a valid email address.";
        }
        if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
            return "Phone number must be 10 digits and start with 98.";
        }
        if (!ValidationUtil.isValidPassword(password)) {
            return "Password must be at least 8 characters and include an uppercase letter, a number, and a symbol.";
        }
        if (!ValidationUtil.doPasswordsMatch(password, confirmPassword)) {
            return "Password and confirm password do not match.";
        }
        if (userDAO.emailExists(email.trim().toLowerCase())) {
            return "This email address is already registered.";
        }
        if (userDAO.phoneExists(phoneNumber.trim())) {
            return "This phone number is already registered.";
        }
        return null;
    }

    private String resolveRedirect(String redirect) {
        if (!ValidationUtil.isNullOrEmpty(redirect) && redirect.startsWith("/")) {
            return redirect;
        }
        return "/home";
    }
}
