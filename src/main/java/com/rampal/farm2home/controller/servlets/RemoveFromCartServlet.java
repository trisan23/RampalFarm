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

@WebServlet(name = "RemoveFromCartServlet", urlPatterns = "/remove-from-cart")
public class RemoveFromCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productIdValue = request.getParameter("productId");
        if (!ValidationUtil.isPositiveWholeNumber(productIdValue)) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Invalid cart item.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        CartUtil.removeFromCart(request, Integer.parseInt(productIdValue));
        SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, "Item removed from cart.");
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
