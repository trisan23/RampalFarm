package com.rampal.farm2home.controller.servlets;

import com.rampal.farm2home.dao.CategoryDAO;
import com.rampal.farm2home.dao.OrderDAO;
import com.rampal.farm2home.dao.ProductDAO;
import com.rampal.farm2home.dao.UserDAO;
import com.rampal.farm2home.utilities.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DashboardServlet", urlPatterns = "/admin/dashboard")
public class DashboardServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("userCount", userDAO.countUsers());
            request.setAttribute("categoryCount", categoryDAO.countCategories());
            request.setAttribute("productCount", productDAO.countProducts());
            request.setAttribute("orderCount", orderDAO.countOrders());
            request.setAttribute("pendingOrderCount", orderDAO.countPendingOrders());
            request.setAttribute("recentProducts", productDAO.findLatest(6));
            request.setAttribute("recentOrders", orderDAO.findRecent(6));
            request.setAttribute("flashSuccess", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_SUCCESS));
            request.setAttribute("flashError", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_ERROR));
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException exception) {
            throw new ServletException("Unable to load dashboard.", exception);
        }
    }
}
