package com.rampal.farm2home.utilities;

import com.rampal.farm2home.model.CartItem;
import com.rampal.farm2home.model.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartUtil {
    public static final String CART_SESSION_KEY = "cartItems";

    private CartUtil() {
    }

    @SuppressWarnings("unchecked")
    public static List<CartItem> getCart(HttpServletRequest request) {
        Object cart = SessionUtil.getAttribute(request, CART_SESSION_KEY);
        if (cart instanceof List<?>) {
            return (List<CartItem>) cart;
        }
        List<CartItem> items = new ArrayList<>();
        SessionUtil.setAttribute(request, CART_SESSION_KEY, items);
        return items;
    }

    public static void addToCart(HttpServletRequest request, Product product, int quantity) {
        List<CartItem> cartItems = getCart(request);
        for (CartItem item : cartItems) {
            if (item.getProductId() == product.getProductId()) {
                item.setQuantity(item.getQuantity() + quantity);
                SessionUtil.setAttribute(request, CART_SESSION_KEY, cartItems);
                return;
            }
        }

        CartItem item = new CartItem();
        item.setProductId(product.getProductId());
        item.setProductName(product.getProductName());
        item.setImageUrl(product.getImageUrl());
        item.setPrice(product.getPrice());
        item.setQuantity(quantity);
        cartItems.add(item);
        SessionUtil.setAttribute(request, CART_SESSION_KEY, cartItems);
    }

    public static void updateQuantity(HttpServletRequest request, int productId, int quantity) {
        List<CartItem> cartItems = getCart(request);
        cartItems.removeIf(item -> {
            if (item.getProductId() == productId) {
                if (quantity <= 0) {
                    return true;
                }
                item.setQuantity(quantity);
            }
            return false;
        });
        SessionUtil.setAttribute(request, CART_SESSION_KEY, cartItems);
    }

    public static void removeFromCart(HttpServletRequest request, int productId) {
        List<CartItem> cartItems = getCart(request);
        cartItems.removeIf(item -> item.getProductId() == productId);
        SessionUtil.setAttribute(request, CART_SESSION_KEY, cartItems);
    }

    public static BigDecimal getCartTotal(HttpServletRequest request) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : getCart(request)) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    public static int getCartCount(HttpServletRequest request) {
        int totalQuantity = 0;
        for (CartItem item : getCart(request)) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }
}
