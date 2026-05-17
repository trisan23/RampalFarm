package com.rampal.farm2home.dao.interfaces;

import com.rampal.farm2home.model.Order;
import java.sql.SQLException;
import java.util.List;

public interface OrderDAOInterface {
    int create(Order order) throws SQLException, ClassNotFoundException;
    List<Order> findByUserId(int userId) throws SQLException, ClassNotFoundException;
    List<Order> findAll() throws SQLException, ClassNotFoundException;
    List<Order> findRecent(int limit) throws SQLException, ClassNotFoundException;
    int countOrders() throws SQLException, ClassNotFoundException;
    int countPendingOrders() throws SQLException, ClassNotFoundException;
    boolean updateStatus(int orderId, String newStatus) throws SQLException, ClassNotFoundException;
}
