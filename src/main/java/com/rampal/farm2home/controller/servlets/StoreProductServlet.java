package com.rampal.farm2home.controller.servlets;

import com.rampal.farm2home.dao.ProductDAO;
import com.rampal.farm2home.model.Product;
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

@WebServlet(name = "StoreProductServlet", urlPatterns = "/product")
public class StoreProductServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productIdValue = request.getParameter("productId");
        if (!ValidationUtil.isPositiveWholeNumber(productIdValue)) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdValue);
            Product product = productDAO.findById(productId);
            if (product == null) {
                SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Requested product could not be found.");
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }

            request.setAttribute("product", product);
            request.setAttribute("cartCount", CartUtil.getCartCount(request));
            request.setAttribute("flashSuccess", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_SUCCESS));
            request.setAttribute("flashError", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_ERROR));
            request.getRequestDispatcher("/product.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException exception) {
            throw new ServletException("Unable to load the product view.", exception);
        }
    }
}
