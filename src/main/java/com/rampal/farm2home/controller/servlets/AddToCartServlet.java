package com.rampal.farm2home.controller.servlets;

import com.rampal.farm2home.dao.ProductDAO;
import com.rampal.farm2home.model.Product;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet(name = "AddToCartServlet", urlPatterns = "/add-to-cart")
public class AddToCartServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productIdValue = request.getParameter("productId");
        String quantityValue = request.getParameter("quantity");
        String redirect = request.getParameter("redirect");
        User user = (User) SessionUtil.getAttribute(request, SessionUtil.USER_SESSION_KEY);

        if (user == null) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Please login or register before adding products to the cart.");
            response.sendRedirect(request.getContextPath() + "/login?redirect="
                    + URLEncoder.encode(resolveRedirectQuery(redirect), StandardCharsets.UTF_8));
            return;
        }

        if (!ValidationUtil.isPositiveWholeNumber(productIdValue)) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Invalid product selected.");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        int quantity = ValidationUtil.isPositiveWholeNumber(quantityValue) ? Integer.parseInt(quantityValue) : 1;

        try {
            Product product = productDAO.findById(Integer.parseInt(productIdValue));
            if (product == null) {
                SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Product not found.");
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
            if (product.getStockQuantity() < quantity) {
                SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Sorry we are out of stock");
                response.sendRedirect(request.getContextPath() + (ValidationUtil.isNullOrEmpty(redirect) ? "/home" : redirect));
                return;
            }

            CartUtil.addToCart(request, product, quantity);
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, product.getProductName() + " added to cart.");
            response.sendRedirect(request.getContextPath() + (ValidationUtil.isNullOrEmpty(redirect) ? "/cart" : redirect));
        } catch (SQLException | ClassNotFoundException exception) {
            throw new ServletException("Unable to add item to cart.", exception);
        }
    }

    private String resolveRedirectQuery(String redirect) {
        if (ValidationUtil.isNullOrEmpty(redirect) || !redirect.startsWith("/")) {
            return "/home";
        }
        return redirect;
    }
}
