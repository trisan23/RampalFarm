package com.rampal.farm2home.dao;

import com.rampal.farm2home.model.Product;
import com.rampal.farm2home.utilities.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rampal.farm2home.dao.interfaces.ProductDAOInterface;

public class ProductDAO implements ProductDAOInterface {

    public List<Product> findAll() throws SQLException, ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        String sql = """
                SELECT p.*, c.category_name
                FROM products p
                LEFT JOIN categories c ON p.category_id = c.category_id
                ORDER BY p.created_at DESC, p.product_name
                """;
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                products.add(mapProduct(resultSet));
            }
        }
        return products;
    }

    public List<Product> findLatest(int limit) throws SQLException, ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        String sql = """
                SELECT p.*, c.category_name
                FROM products p
                LEFT JOIN categories c ON p.category_id = c.category_id
                ORDER BY p.created_at DESC, p.product_name
                LIMIT ?
                """;
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapProduct(resultSet));
                }
            }
        }
        return products;
    }

    public List<Product> findByCategoryId(int categoryId) throws SQLException, ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        String sql = """
                SELECT p.*, c.category_name
                FROM products p
                LEFT JOIN categories c ON p.category_id = c.category_id
                WHERE p.category_id = ?
                ORDER BY p.created_at DESC, p.product_name
                """;
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapProduct(resultSet));
                }
            }
        }
        return products;
    }

    public Product findById(int productId) throws SQLException, ClassNotFoundException {
        String sql = """
                SELECT p.*, c.category_name
                FROM products p
                LEFT JOIN categories c ON p.category_id = c.category_id
                WHERE p.product_id = ?
                """;
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapProduct(resultSet);
                }
            }
        }
        return null;
    }

    public List<Product> findByIdsPreserveOrder(List<Integer> productIds) throws SQLException, ClassNotFoundException {
        if (productIds == null || productIds.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < productIds.size(); i++) {
            if (i > 0) {
                placeholders.append(",");
            }
            placeholders.append("?");
        }

        String sql = """
                SELECT p.*, c.category_name
                FROM products p
                LEFT JOIN categories c ON p.category_id = c.category_id
                WHERE p.product_id IN (""" + placeholders + ")";

        Map<Integer, Product> productMap = new HashMap<>();
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < productIds.size(); i++) {
                statement.setInt(i + 1, productIds.get(i));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = mapProduct(resultSet);
                    productMap.put(product.getProductId(), product);
                }
            }
        }

        List<Product> orderedProducts = new ArrayList<>();
        for (Integer productId : productIds) {
            Product product = productMap.get(productId);
            if (product != null) {
                orderedProducts.add(product);
            }
        }
        return orderedProducts;
    }

    public boolean create(Product product) throws SQLException, ClassNotFoundException {
        String sql = """
                INSERT INTO products
                (product_name, description, stock_quantity, price, image_url, category_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            return applyProduct(product, statement).executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException exception) {
            return false;
        }
    }

    public boolean update(Product product) throws SQLException, ClassNotFoundException {
        String sql = """
                UPDATE products
                SET product_name = ?, description = ?, stock_quantity = ?, price = ?, image_url = ?, category_id = ?
                WHERE product_id = ?
                """;
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            applyProduct(product, statement);
            statement.setInt(7, product.getProductId());
            return statement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException exception) {
            return false;
        }
    }

    public boolean delete(int productId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            return statement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException exception) {
            return false;
        }
    }

    public int countProducts() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    private PreparedStatement applyProduct(Product product, PreparedStatement statement) throws SQLException {
        statement.setString(1, product.getProductName());
        statement.setString(2, product.getDescription());
        statement.setInt(3, product.getStockQuantity());
        statement.setBigDecimal(4, product.getPrice());
        statement.setString(5, product.getImageUrl());
        if (product.getCategoryId() == null) {
            statement.setNull(6, java.sql.Types.INTEGER);
        } else {
            statement.setInt(6, product.getCategoryId());
        }
        return statement;
    }

    private Product mapProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setProductId(resultSet.getInt("product_id"));
        product.setProductName(resultSet.getString("product_name"));
        product.setDescription(resultSet.getString("description"));
        product.setStockQuantity(resultSet.getInt("stock_quantity"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setImageUrl(resultSet.getString("image_url"));
        int categoryId = resultSet.getInt("category_id");
        product.setCategoryId(resultSet.wasNull() ? null : categoryId);
        product.setCategoryName(resultSet.getString("category_name"));
        product.setCreatedAt(resultSet.getTimestamp("created_at"));
        return product;
    }
}
