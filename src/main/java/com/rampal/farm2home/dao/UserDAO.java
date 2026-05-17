package com.rampal.farm2home.dao;

import com.rampal.farm2home.model.User;
import com.rampal.farm2home.utilities.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rampal.farm2home.dao.interfaces.UserDAOInterface;

public class UserDAO implements UserDAOInterface {

    public List<User> findAll() throws SQLException, ClassNotFoundException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC, user_id DESC";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }
        }
        return users;
    }

    public User findById(int userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
        }
        return null;
    }

    public User findByEmail(String email) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
        }
        return null;
    }

    public boolean emailExists(String email) throws SQLException, ClassNotFoundException {
        return exists("SELECT 1 FROM users WHERE email = ?", email);
    }

    public boolean phoneExists(String phone) throws SQLException, ClassNotFoundException {
        return exists("SELECT 1 FROM users WHERE phone_number = ?", phone);
    }

    public boolean emailExistsForOtherUser(String email, int userId) throws SQLException, ClassNotFoundException {
        return exists("SELECT 1 FROM users WHERE email = ? AND user_id <> ?", email, userId);
    }

    public boolean phoneExistsForOtherUser(String phone, int userId) throws SQLException, ClassNotFoundException {
        return exists("SELECT 1 FROM users WHERE phone_number = ? AND user_id <> ?", phone, userId);
    }

    public boolean create(User user) throws SQLException, ClassNotFoundException {
        String sql = """
                INSERT INTO users (username, email, password, phone_number, role, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getPhoneNumber());
            statement.setString(5, user.getRole());
            statement.setString(6, user.getStatus());
            return statement.executeUpdate() == 1;
        }
    }

    public boolean update(User user) throws SQLException, ClassNotFoundException {
        String sql = """
                UPDATE users
                SET username = ?, email = ?, password = ?, phone_number = ?, role = ?, status = ?
                WHERE user_id = ?
                """;
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getPhoneNumber());
            statement.setString(5, user.getRole());
            statement.setString(6, user.getStatus());
            statement.setInt(7, user.getUserId());
            return statement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException exception) {
            return false;
        }
    }

    public boolean delete(int userId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException exception) {
            return false;
        }
    }

    public boolean updatePasswordHash(int userId, String hashedPassword) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hashedPassword);
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException exception) {
            return false;
        }
    }

    public int countUsers() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    private boolean exists(String sql, String value) throws SQLException, ClassNotFoundException {
        return exists(sql, value, null);
    }

    private boolean exists(String sql, String value, Integer userId) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, value);
            if (userId != null) {
                statement.setInt(2, userId);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getInt("user_id"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setRole(resultSet.getString("role"));
        user.setStatus(resultSet.getString("status"));
        user.setCreatedAt(resultSet.getTimestamp("created_at"));
        return user;
    }
}
