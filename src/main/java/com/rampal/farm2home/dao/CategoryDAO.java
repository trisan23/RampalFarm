package com.rampal.farm2home.dao;

import com.rampal.farm2home.model.Category;
import com.rampal.farm2home.utilities.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rampal.farm2home.dao.interfaces.CategoryDAOInterface;

public class CategoryDAO implements CategoryDAOInterface {

    public List<Category> findAll() throws SQLException, ClassNotFoundException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY category_name";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                categories.add(mapCategory(resultSet));
            }
        }
        return categories;
    }

    public Category findById(int categoryId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapCategory(resultSet);
                }
            }
        }
        return null;
    }

    public boolean create(Category category) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO categories (category_name, description) VALUES (?, ?)";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getCategoryName());
            statement.setString(2, category.getDescription());
            return statement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException exception) {
            return false;
        }
    }

    public boolean update(Category category) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE categories SET category_name = ?, description = ? WHERE category_id = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getCategoryName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, category.getCategoryId());
            return statement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException exception) {
            return false;
        }
    }

    public boolean delete(int categoryId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            return statement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException exception) {
            return false;
        }
    }

    public int countCategories() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM categories";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    private Category mapCategory(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setCategoryId(resultSet.getInt("category_id"));
        category.setCategoryName(resultSet.getString("category_name"));
        category.setDescription(resultSet.getString("description"));
        return category;
    }
}
