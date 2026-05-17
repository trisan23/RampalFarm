package com.rampal.farm2home.dao.interfaces;

import com.rampal.farm2home.model.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDAOInterface {
    List<User> findAll() throws SQLException, ClassNotFoundException;
    User findById(int userId) throws SQLException, ClassNotFoundException;
    User findByEmail(String email) throws SQLException, ClassNotFoundException;
    boolean emailExists(String email) throws SQLException, ClassNotFoundException;
    boolean phoneExists(String phone) throws SQLException, ClassNotFoundException;
    boolean emailExistsForOtherUser(String email, int userId) throws SQLException, ClassNotFoundException;
    boolean phoneExistsForOtherUser(String phone, int userId) throws SQLException, ClassNotFoundException;
    boolean create(User user) throws SQLException, ClassNotFoundException;
    boolean update(User user) throws SQLException, ClassNotFoundException;
    boolean delete(int userId) throws SQLException, ClassNotFoundException;
    boolean updatePasswordHash(int userId, String hashedPassword) throws SQLException, ClassNotFoundException;
    int countUsers() throws SQLException, ClassNotFoundException;
}
