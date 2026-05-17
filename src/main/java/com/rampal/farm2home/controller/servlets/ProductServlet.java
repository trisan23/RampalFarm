package com.rampal.farm2home.controller.servlets;

import com.rampal.farm2home.dao.CategoryDAO;
import com.rampal.farm2home.dao.ProductDAO;
import com.rampal.farm2home.model.Product;
import com.rampal.farm2home.utilities.SessionUtil;
import com.rampal.farm2home.utilities.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet(name = "ProductServlet", urlPatterns = "/admin/products")
@MultipartConfig
public class ProductServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String editId = request.getParameter("edit");
            if (ValidationUtil.isPositiveWholeNumber(editId)) {
                request.setAttribute("editProduct", productDAO.findById(Integer.parseInt(editId)));
            }
            request.setAttribute("products", productDAO.findAll());
            request.setAttribute("categories", categoryDAO.findAll());
            request.setAttribute("flashSuccess", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_SUCCESS));
            request.setAttribute("flashError", SessionUtil.consumeFlashMessage(request, SessionUtil.FLASH_ERROR));
            request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException exception) {
            throw new ServletException("Unable to load products.", exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("delete".equalsIgnoreCase(action)) {
                deleteProduct(request);
                SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, "Product deleted successfully.");
            } else if ("update".equalsIgnoreCase(action)) {
                saveProduct(request, true);
                SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, "Product updated successfully.");
            } else {
                saveProduct(request, false);
                SessionUtil.setFlashMessage(request, SessionUtil.FLASH_SUCCESS, "Product created successfully.");
            }
        } catch (IllegalArgumentException exception) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, exception.getMessage());
        } catch (SQLException | ClassNotFoundException exception) {
            SessionUtil.setFlashMessage(request, SessionUtil.FLASH_ERROR, "Unable to save product.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }

    private void saveProduct(HttpServletRequest request, boolean updating)
            throws SQLException, ClassNotFoundException, IOException, ServletException {
        String productName = request.getParameter("productName");
        String description = request.getParameter("description");
        String stockQuantity = request.getParameter("stockQuantity");
        String price = request.getParameter("price");
        String categoryId = request.getParameter("categoryId");
        String existingImageUrl = request.getParameter("existingImageUrl");
        Part imagePart = request.getPart("image");

        validateProduct(productName, stockQuantity, price, categoryId, imagePart);

        Product product = new Product();
        product.setProductName(productName.trim());
        product.setDescription(description == null ? null : description.trim());
        product.setStockQuantity(Integer.parseInt(stockQuantity));
        product.setPrice(new BigDecimal(price));
        product.setCategoryId(ValidationUtil.isPositiveWholeNumber(categoryId) ? Integer.parseInt(categoryId) : null);
        product.setImageUrl(resolveImageUrl(request, imagePart, existingImageUrl));

        if (updating) {
            String productId = request.getParameter("productId");
            if (!ValidationUtil.isPositiveWholeNumber(productId)) {
                throw new IllegalArgumentException("Invalid product selected.");
            }
            product.setProductId(Integer.parseInt(productId));
            productDAO.update(product);
        } else {
            productDAO.create(product);
        }
    }

    private void deleteProduct(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        String productId = request.getParameter("productId");
        if (!ValidationUtil.isPositiveWholeNumber(productId)) {
            throw new IllegalArgumentException("Invalid product selected.");
        }
        productDAO.delete(Integer.parseInt(productId));
    }

    private void validateProduct(String productName, String stockQuantity, String price, String categoryId, Part imagePart) {
        if (ValidationUtil.isNullOrEmpty(productName) || ValidationUtil.isNullOrEmpty(stockQuantity)
                || ValidationUtil.isNullOrEmpty(price)) {
            throw new IllegalArgumentException("Product name, stock quantity, and price are required.");
        }
        if (!ValidationUtil.isPositiveWholeNumber(stockQuantity)) {
            throw new IllegalArgumentException("Stock quantity must be a whole positive number.");
        }
        if (!ValidationUtil.isPositiveDecimal(price)) {
            throw new IllegalArgumentException("Price must be a valid positive amount.");
        }
        if (!ValidationUtil.isNullOrEmpty(categoryId) && !ValidationUtil.isPositiveWholeNumber(categoryId)) {
            throw new IllegalArgumentException("Invalid category selected.");
        }
        if (imagePart != null && !ValidationUtil.isNullOrEmpty(imagePart.getSubmittedFileName())
                && !ValidationUtil.isValidImageExtension(imagePart)) {
            throw new IllegalArgumentException("Only JPG, JPEG, PNG, or GIF images are allowed.");
        }
    }

    private String resolveImageUrl(HttpServletRequest request, Part imagePart, String existingImageUrl)
            throws IOException {
        if (imagePart == null || ValidationUtil.isNullOrEmpty(imagePart.getSubmittedFileName())) {
            return existingImageUrl;
        }

        String cleanFileName = ValidationUtil.sanitizeFileName(imagePart.getSubmittedFileName());
        String uniqueName = UUID.randomUUID() + "_" + cleanFileName;
        String webRoot = request.getServletContext().getRealPath("");
        Path uploadDirectory = Paths.get(webRoot, "uploads", "products");
        Files.createDirectories(uploadDirectory);

        Path filePath = uploadDirectory.resolve(uniqueName);
        imagePart.write(filePath.toString());
        return "uploads/products/" + uniqueName;
    }
}
