package com.rampal.farm2home.controller.servlets;

import com.rampal.farm2home.utilities.CartUtil;
import com.rampal.farm2home.utilities.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "CartServlet", urlPatterns = "/cart")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cartItems", CartUtil.getCart(request));
        request.setAttribute("cartTotal", CartUtil.getCartTotal(request));
        request.setAttribute("cartCount", CartUtil.getCartCount(request));
        request.setAttribute("flashSuccess", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_SUCCESS));
        request.setAttribute("flashError", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_ERROR));
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }
}
