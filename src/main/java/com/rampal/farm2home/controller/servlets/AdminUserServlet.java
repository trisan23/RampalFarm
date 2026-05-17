package com.rampal.farm2home.controller.servlets;

import com.rampal.farm2home.dao.UserDAO;
import com.rampal.farm2home.model.User;
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

@WebServlet(name = "AdminUserServlet", urlPatterns = "/admin/users")
public class AdminUserServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String editId = request.getParameter("edit");
            if (ValidationUtil.isPositiveWholeNumber(editId)) {
                request.setAttribute("editUser", userDAO.findById(Integer.parseInt(editId)));
            }
            request.setAttribute("users", userDAO.findAll());
            request.setAttribute("flashSuccess", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_SUCCESS));
            request.setAttribute("flashError", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_ERROR));
            request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException exception) {
            throw new ServletException("Unable to load users.", exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("delete".equalsIgnoreCase(action)) {
                deleteUser(request);
                SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, "User deleted successfully.");
            } else if ("update".equalsIgnoreCase(action)) {
                saveUser(request, true);
                SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, "User updated successfully.");
            } else {
                saveUser(request, false);
                SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, "User created successfully.");
            }
        } catch (IllegalArgumentException exception) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, exception.getMessage());
        } catch (SQLException | ClassNotFoundException exception) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Unable to save user.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }

    private void saveUser(HttpServletRequest request, boolean updating) throws SQLException, ClassNotFoundException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String status = request.getParameter("status");

        User existingUser = null;
        Integer userId = null;
        if (updating) {
            String userIdValue = request.getParameter("userId");
            if (!ValidationUtil.isPositiveWholeNumber(userIdValue)) {
                throw new IllegalArgumentException("Invalid user selected.");
            }
            userId = Integer.parseInt(userIdValue);
            existingUser = userDAO.findById(userId);
            if (existingUser == null) {
                throw new IllegalArgumentException("Selected user could not be found.");
            }
        }

        validateUser(username, email, phoneNumber, password, role, status, updating, userId);

        User user = new User();
        if (updating) {
            user.setUserId(userId);
        }
        user.setUsername(username.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPhoneNumber(phoneNumber.trim());
        user.setRole(role.trim().toLowerCase());
        user.setStatus(status.trim().toLowerCase());
        user.setPassword(resolvePassword(password, existingUser));

        if (updating) {
            userDAO.update(user);
        } else {
            userDAO.create(user);
        }
    }

    private void validateUser(String username, String email, String phoneNumber, String password,
                              String role, String status, boolean updating, Integer userId)
            throws SQLException, ClassNotFoundException {
        if (ValidationUtil.isNullOrEmpty(username) || ValidationUtil.isNullOrEmpty(email)
                || ValidationUtil.isNullOrEmpty(phoneNumber) || ValidationUtil.isNullOrEmpty(role)
                || ValidationUtil.isNullOrEmpty(status)) {
            throw new IllegalArgumentException("Name, email, phone, role, and status are required.");
        }
        if (!ValidationUtil.isValidName(username)) {
            throw new IllegalArgumentException("Name must start with a letter and contain only letters and spaces.");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }
        if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone number must be 10 digits and start with 98.");
        }
        if (!"admin".equalsIgnoreCase(role) && !"customer".equalsIgnoreCase(role)) {
            throw new IllegalArgumentException("Role must be either admin or customer.");
        }
        if (!"active".equalsIgnoreCase(status) && !"inactive".equalsIgnoreCase(status)) {
            throw new IllegalArgumentException("Status must be either active or inactive.");
        }
        if (updating) {
            if (userDAO.emailExistsForOtherUser(email.trim().toLowerCase(), userId)) {
                throw new IllegalArgumentException("This email address is already registered.");
            }
            if (userDAO.phoneExistsForOtherUser(phoneNumber.trim(), userId)) {
                throw new IllegalArgumentException("This phone number is already registered.");
            }
            if (!ValidationUtil.isNullOrEmpty(password) && !ValidationUtil.isValidPassword(password)) {
                throw new IllegalArgumentException("Password must be at least 8 characters and include an uppercase letter, a number, and a symbol.");
            }
        } else {
            if (ValidationUtil.isNullOrEmpty(password)) {
                throw new IllegalArgumentException("Password is required for a new user.");
            }
            if (!ValidationUtil.isValidPassword(password)) {
                throw new IllegalArgumentException("Password must be at least 8 characters and include an uppercase letter, a number, and a symbol.");
            }
            if (userDAO.emailExists(email.trim().toLowerCase())) {
                throw new IllegalArgumentException("This email address is already registered.");
            }
            if (userDAO.phoneExists(phoneNumber.trim())) {
                throw new IllegalArgumentException("This phone number is already registered.");
            }
        }
    }

    private String resolvePassword(String password, User existingUser) {
        if (ValidationUtil.isNullOrEmpty(password)) {
            return existingUser == null ? null : existingUser.getPassword();
        }
        return PasswordUtil.getHashPassword(password);
    }

    private void deleteUser(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        String userIdValue = request.getParameter("userId");
        if (!ValidationUtil.isPositiveWholeNumber(userIdValue)) {
            throw new IllegalArgumentException("Invalid user selected.");
        }

        User loggedInUser = (User) SessionUtil.getAttribute(request, SessionUtil.USER_SESSION_KEY);
        int userId = Integer.parseInt(userIdValue);
        if (loggedInUser != null && loggedInUser.getUserId() == userId) {
            throw new IllegalArgumentException("You cannot delete the account you are currently using.");
        }

        userDAO.delete(userId);
    }
}
