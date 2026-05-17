package com.rampal.farm2home.controller.servlets;

import com.rampal.farm2home.utilities.CartUtil;
import com.rampal.farm2home.utilities.SessionUtil;
import com.rampal.farm2home.utilities.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "UpdateCartServlet", urlPatterns = "/update-cart")
public class UpdateCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productIdValue = request.getParameter("productId");
        String quantityValue = request.getParameter("quantity");

        if (!ValidationUtil.isPositiveWholeNumber(productIdValue) || !ValidationUtil.isPositiveWholeNumber(quantityValue)) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Invalid quantity.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        CartUtil.updateQuantity(request, Integer.parseInt(productIdValue), Integer.parseInt(quantityValue));
        SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, "Cart updated successfully.");
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
