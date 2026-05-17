package com.rampal.farm2home.dao;

import com.rampal.farm2home.model.Order;
import com.rampal.farm2home.model.OrderDetail;
import com.rampal.farm2home.utilities.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.rampal.farm2home.dao.interfaces.OrderDAOInterface;

public class OrderDAO implements OrderDAOInterface {

    public int create(Order order) throws SQLException, ClassNotFoundException {
        String orderSql = """
                INSERT INTO orders (user_id, total_amount, status, delivery_address)
                VALUES (?, ?, ?, ?)
                """;
        String detailSql = """
                INSERT INTO order_details (order_id, product_id, quantity, price_at_purchase)
                VALUES (?, ?, ?, ?)
                """;
        String stockSql = """
                UPDATE products
                SET stock_quantity = stock_quantity - ?
                WHERE product_id = ? AND stock_quantity >= ?
                """;

        try (Connection connection = DBConfig.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement orderStatement = connection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement detailStatement = connection.prepareStatement(detailSql);
                 PreparedStatement stockStatement = connection.prepareStatement(stockSql)) {
                orderStatement.setInt(1, order.getUserId());
                orderStatement.setBigDecimal(2, order.getTotalAmount());
                orderStatement.setString(3, order.getStatus());
                orderStatement.setString(4, order.getDeliveryAddress());
                orderStatement.executeUpdate();

                int orderId;
                try (ResultSet generatedKeys = orderStatement.getGeneratedKeys()) {
                    if (!generatedKeys.next()) {
                        throw new SQLException("Order id was not generated.");
                    }
                    orderId = generatedKeys.getInt(1);
                }

                for (OrderDetail detail : order.getOrderDetails()) {
                    detailStatement.setInt(1, orderId);
                    detailStatement.setInt(2, detail.getProductId());
                    detailStatement.setInt(3, detail.getQuantity());
                    detailStatement.setBigDecimal(4, detail.getPriceAtPurchase());
                    detailStatement.addBatch();

                    stockStatement.setInt(1, detail.getQuantity());
                    stockStatement.setInt(2, detail.getProductId());
                    stockStatement.setInt(3, detail.getQuantity());
                    stockStatement.addBatch();
                }

                detailStatement.executeBatch();
                int[] stockResults = stockStatement.executeBatch();
                for (int result : stockResults) {
                    if (result != 1) {
                        throw new SQLException("Insufficient stock for one of the selected products.");
                    }
                }

                connection.commit();
                return orderId;
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public List<Order> findByUserId(int userId) throws SQLException, ClassNotFoundException {
        return findOrders("""
                SELECT o.order_id, o.user_id, u.username, o.order_date, o.total_amount, o.status, o.delivery_address,
                       od.order_details_id, od.product_id, od.quantity, od.price_at_purchase,
                       p.product_name, p.image_url
                FROM orders o
                JOIN users u ON o.user_id = u.user_id
                LEFT JOIN order_details od ON o.order_id = od.order_id
                LEFT JOIN products p ON od.product_id = p.product_id
                WHERE o.user_id = ?
                ORDER BY o.order_date DESC, o.order_id DESC, od.order_details_id ASC
                """, userId);
    }

    public List<Order> findAll() throws SQLException, ClassNotFoundException {
        return findOrders("""
                SELECT o.order_id, o.user_id, u.username, o.order_date, o.total_amount, o.status, o.delivery_address,
                       od.order_details_id, od.product_id, od.quantity, od.price_at_purchase,
                       p.product_name, p.image_url
                FROM orders o
                JOIN users u ON o.user_id = u.user_id
                LEFT JOIN order_details od ON o.order_id = od.order_id
                LEFT JOIN products p ON od.product_id = p.product_id
                ORDER BY o.order_date DESC, o.order_id DESC, od.order_details_id ASC
                """, null);
    }

    public List<Order> findRecent(int limit) throws SQLException, ClassNotFoundException {
        List<Order> orders = new ArrayList<>();
        String sql = """
                SELECT o.order_id, o.user_id, u.username, o.order_date, o.total_amount, o.status, o.delivery_address
                FROM orders o
                JOIN users u ON o.user_id = u.user_id
                ORDER BY o.order_date DESC, o.order_id DESC
                LIMIT ?
                """;
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapOrder(resultSet));
                }
            }
        }
        return orders;
    }

    public int countOrders() throws SQLException, ClassNotFoundException {
        return countBySql("SELECT COUNT(*) FROM orders");
    }

    public int countPendingOrders() throws SQLException, ClassNotFoundException {
        return countBySql("SELECT COUNT(*) FROM orders WHERE status = 'pending'");
    }

    public boolean updateStatus(int orderId, String newStatus) throws SQLException, ClassNotFoundException {
        if (!"confirmed".equalsIgnoreCase(newStatus) && !"cancelled".equalsIgnoreCase(newStatus)) {
            throw new IllegalArgumentException("Unsupported order status.");
        }

        String readOrderSql = "SELECT status FROM orders WHERE order_id = ?";
        String updateOrderSql = "UPDATE orders SET status = ? WHERE order_id = ? AND status = 'pending'";
        String restoreStockSql = """
                UPDATE products p
                JOIN order_details od ON od.product_id = p.product_id
                SET p.stock_quantity = p.stock_quantity + od.quantity
                WHERE od.order_id = ?
                """;

        try (Connection connection = DBConfig.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement readStatement = connection.prepareStatement(readOrderSql);
                 PreparedStatement updateStatement = connection.prepareStatement(updateOrderSql);
                 PreparedStatement restoreStockStatement = connection.prepareStatement(restoreStockSql)) {
                readStatement.setInt(1, orderId);
                String currentStatus;
                try (ResultSet resultSet = readStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new IllegalArgumentException("Order not found.");
                    }
                    currentStatus = resultSet.getString("status");
                }

                if (!"pending".equalsIgnoreCase(currentStatus)) {
                    throw new IllegalArgumentException("Only pending orders can be changed from the admin panel.");
                }

                updateStatement.setString(1, newStatus.toLowerCase());
                updateStatement.setInt(2, orderId);
                boolean updated = updateStatement.executeUpdate() == 1;

                if (updated && "cancelled".equalsIgnoreCase(newStatus)) {
                    restoreStockStatement.setInt(1, orderId);
                    restoreStockStatement.executeUpdate();
                }

                connection.commit();
                return updated;
            } catch (SQLException | IllegalArgumentException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private int countBySql(String sql) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    private List<Order> findOrders(String sql, Integer userId) throws SQLException, ClassNotFoundException {
        Map<Integer, Order> orderMap = new LinkedHashMap<>();
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (userId != null && sql.contains("WHERE o.user_id = ?")) {
                statement.setInt(1, userId);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int orderId = resultSet.getInt("order_id");
                    Order order = orderMap.computeIfAbsent(orderId, ignored -> mapOrderSafely(resultSet));
                    int detailId = resultSet.getInt("order_details_id");
                    if (!resultSet.wasNull()) {
                        order.getOrderDetails().add(mapOrderDetail(resultSet));
                    }
                }
            }
        }
        return new ArrayList<>(orderMap.values());
    }

    private Order mapOrderSafely(ResultSet resultSet) {
        try {
            return mapOrder(resultSet);
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to map order.", exception);
        }
    }

    private Order mapOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setOrderId(resultSet.getInt("order_id"));
        order.setUserId(resultSet.getInt("user_id"));
        order.setUsername(resultSet.getString("username"));
        order.setOrderDate(resultSet.getTimestamp("order_date"));
        order.setTotalAmount(resultSet.getBigDecimal("total_amount"));
        order.setStatus(resultSet.getString("status"));
        order.setDeliveryAddress(resultSet.getString("delivery_address"));
        return order;
    }

    private OrderDetail mapOrderDetail(ResultSet resultSet) throws SQLException {
        OrderDetail detail = new OrderDetail();
        detail.setOrderDetailsId(resultSet.getInt("order_details_id"));
        detail.setOrderId(resultSet.getInt("order_id"));
        detail.setProductId(resultSet.getInt("product_id"));
        detail.setProductName(resultSet.getString("product_name"));
        detail.setImageUrl(resultSet.getString("image_url"));
        detail.setQuantity(resultSet.getInt("quantity"));
        detail.setPriceAtPurchase(resultSet.getBigDecimal("price_at_purchase"));
        return detail;
    }
}
