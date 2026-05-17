package com.rampal.farm2home.controller.servlets;

import com.rampal.farm2home.dao.CategoryDAO;
import com.rampal.farm2home.model.Category;
import com.rampal.farm2home.utilities.SessionUtil;
import com.rampal.farm2home.utilities.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CategoryServlet", urlPatterns = "/admin/categories")
public class CategoryServlet extends HttpServlet {
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String editId = request.getParameter("edit");
            if (ValidationUtil.isPositiveWholeNumber(editId)) {
                request.setAttribute("editCategory", categoryDAO.findById(Integer.parseInt(editId)));
            }
            request.setAttribute("categories", categoryDAO.findAll());
            request.setAttribute("flashSuccess", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_SUCCESS));
            request.setAttribute("flashError", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_ERROR));
            request.getRequestDispatcher("/admin/categories.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException exception) {
            throw new ServletException("Unable to load categories.", exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("delete".equalsIgnoreCase(action)) {
                deleteCategory(request);
                SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, "Category deleted successfully.");
            } else if ("update".equalsIgnoreCase(action)) {
                saveCategory(request, true);
                SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, "Category updated successfully.");
            } else {
                saveCategory(request, false);
                SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, "Category created successfully.");
            }
        } catch (IllegalArgumentException exception) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, exception.getMessage());
        } catch (SQLException | ClassNotFoundException exception) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Unable to save category.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/categories");
    }

    private void saveCategory(HttpServletRequest request, boolean updating) throws SQLException, ClassNotFoundException {
        String categoryName = request.getParameter("categoryName");
        String description = request.getParameter("description");

        if (ValidationUtil.isNullOrEmpty(categoryName)) {
            throw new IllegalArgumentException("Category name is required.");
        }

        Category category = new Category();
        category.setCategoryName(categoryName.trim());
        category.setDescription(description == null ? null : description.trim());

        if (updating) {
            String categoryId = request.getParameter("categoryId");
            if (!ValidationUtil.isPositiveWholeNumber(categoryId)) {
                throw new IllegalArgumentException("Invalid category selected.");
            }
            category.setCategoryId(Integer.parseInt(categoryId));
            categoryDAO.update(category);
        } else {
            categoryDAO.create(category);
        }
    }

    private void deleteCategory(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        String categoryId = request.getParameter("categoryId");
        if (!ValidationUtil.isPositiveWholeNumber(categoryId)) {
            throw new IllegalArgumentException("Invalid category selected.");
        }
        categoryDAO.delete(Integer.parseInt(categoryId));
    }
}
