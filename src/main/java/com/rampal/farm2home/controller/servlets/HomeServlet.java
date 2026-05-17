package com.rampal.farm2home.controller.servlets;

import com.rampal.farm2home.dao.CategoryDAO;
import com.rampal.farm2home.dao.ProductDAO;
import com.rampal.farm2home.model.Category;
import com.rampal.farm2home.model.Product;
import com.rampal.farm2home.utilities.CartUtil;
import com.rampal.farm2home.utilities.CookieUtil;
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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@WebServlet(name = "HomeServlet", urlPatterns = "/home")
public class HomeServlet extends HttpServlet {
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String categoryIdValue = request.getParameter("categoryId");
            List<Category> categories = categoryDAO.findAll();
            List<Product> products;

            request.setAttribute("categories", categories);
            if (ValidationUtil.isPositiveWholeNumber(categoryIdValue)) {
                int categoryId = Integer.parseInt(categoryIdValue);
                products = productDAO.findByCategoryId(categoryId);
                request.setAttribute("selectedCategoryId", categoryId);
                for (Category category : categories) {
                    if (category.getCategoryId() == categoryId) {
                        request.setAttribute("selectedCategoryName", category.getCategoryName());
                        break;
                    }
                }
            } else {
                products = productDAO.findAll();
            }
            products = removeDuplicateProductNames(products);
            request.setAttribute("products", products);
            request.setAttribute("categoryCount", categories.size());
            request.setAttribute("productCount", products.size());
            request.setAttribute("cartCount", CartUtil.getCartCount(request));
            request.setAttribute("lastLoginEmail", CookieUtil.getCookieValue(request, "rememberUser"));
            request.setAttribute("flashSuccess", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_SUCCESS));
            request.setAttribute("flashError", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_ERROR));
            request.getRequestDispatcher("/home.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException exception) {
            throw new ServletException("Unable to load home page.", exception);
        }
    }

    private List<Product> removeDuplicateProductNames(List<Product> products) {
        List<Product> uniqueProducts = new ArrayList<>();
        Set<String> productNames = new HashSet<>();

        for (Product product : products) {
            String productName = product.getProductName();
            String productKey = productName == null ? "" : productName.trim().toLowerCase(Locale.ROOT);
            if (productNames.add(productKey)) {
                uniqueProducts.add(product);
            }
        }

        return uniqueProducts;
    }
}
