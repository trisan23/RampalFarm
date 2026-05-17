package com.rampal.farm2home.controller.servlets;

import com.rampal.farm2home.dao.OrderDAO;
import com.rampal.farm2home.dao.ProductDAO;
import com.rampal.farm2home.model.CartItem;
import com.rampal.farm2home.model.Order;
import com.rampal.farm2home.model.OrderDetail;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CheckoutServlet", urlPatterns = "/checkout")
public class CheckoutServlet extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) SessionUtil.getAttribute(request, SessionUtil.USER_SESSION_KEY);
        if (user == null) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Please login or register before placing an order.");
            response.sendRedirect(request.getContextPath() + "/login?redirect=/cart");
            return;
        }

        String deliveryAddress = request.getParameter("deliveryAddress");
        List<CartItem> cartItems = CartUtil.getCart(request);
        if (cartItems.isEmpty()) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Your cart is empty.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        if (ValidationUtil.isNullOrEmpty(deliveryAddress)) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Delivery address is required to place the order.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        try {
            Order order = new Order();
            order.setUserId(user.getUserId());
            order.setStatus("pending");
            order.setDeliveryAddress(deliveryAddress.trim());
            order.setTotalAmount(CartUtil.getCartTotal(request));
            order.setOrderDetails(buildOrderDetails(cartItems));
            int orderId = orderDAO.create(order);
            SessionUtil.setAttribute(request, CartUtil.CART_SESSION_KEY, new ArrayList<CartItem>());
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, "Order #" + orderId + " has been placed successfully.");
            response.sendRedirect(request.getContextPath() + "/orders");
        } catch (IllegalArgumentException exception) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, exception.getMessage());
            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (SQLException | ClassNotFoundException exception) {
            throw new ServletException("Unable to place order.", exception);
        }
    }

    private List<OrderDetail> buildOrderDetails(List<CartItem> cartItems) throws SQLException, ClassNotFoundException {
        List<OrderDetail> details = new ArrayList<>();
        for (CartItem item : cartItems) {
            Product product = productDAO.findById(item.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("One of the selected products is no longer available.");
            }
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Sorry we are out of stock");
            }

            OrderDetail detail = new OrderDetail();
            detail.setProductId(item.getProductId());
            detail.setProductName(item.getProductName());
            detail.setImageUrl(item.getImageUrl());
            detail.setQuantity(item.getQuantity());
            detail.setPriceAtPurchase(item.getPrice());
            details.add(detail);
        }
        return details;
    }
}
