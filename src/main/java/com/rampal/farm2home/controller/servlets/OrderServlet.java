package com.rampal.farm2home.controller.servlets;

import com.rampal.farm2home.dao.OrderDAO;
import com.rampal.farm2home.model.User;
import com.rampal.farm2home.utilities.CartUtil;
import com.rampal.farm2home.utilities.SessionUtil;
import com.rampal.farm2home.utilities.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "OrderServlet", urlPatterns = "/orders")
public class OrderServlet extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) SessionUtil.getAttribute(request, SessionUtil.USER_SESSION_KEY);
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Only admin users can update order status.");
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        String orderId = request.getParameter("orderId");
        String status = request.getParameter("status");

        try {
            if (!ValidationUtil.isPositiveWholeNumber(orderId)) {
                throw new IllegalArgumentException("Invalid order selected.");
            }
            orderDAO.updateStatus(Integer.parseInt(orderId), status);
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, "Order status updated successfully.");
        } catch (IllegalArgumentException exception) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, exception.getMessage());
        } catch (SQLException | ClassNotFoundException exception) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Unable to update order status.");
        }
        response.sendRedirect(request.getContextPath() + "/orders");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) SessionUtil.getAttribute(request, SessionUtil.USER_SESSION_KEY);
        if (user == null) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Please login to view your orders.");
            response.sendRedirect(request.getContextPath() + "/login?redirect=/orders");
            return;
        }

        try {
            boolean adminView = "admin".equalsIgnoreCase(user.getRole());
            request.setAttribute("orders", adminView ? orderDAO.findAll() : orderDAO.findByUserId(user.getUserId()));
            request.setAttribute("pageTitle", adminView ? "All Customer Orders" : "My Orders");
            request.setAttribute("pageSubtitle", adminView
                    ? "Track every order, delivery address, and item purchased across the system."
                    : "Review the orders you have placed from your Farm2Home cart.");
            request.setAttribute("cartCount", CartUtil.getCartCount(request));
            request.setAttribute("flashSuccess", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_SUCCESS));
            request.setAttribute("flashError", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_ERROR));
            request.getRequestDispatcher("/orders.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException exception) {
            throw new ServletException("Unable to load orders.", exception);
        }
    }
}
