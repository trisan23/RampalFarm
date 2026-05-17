package com.rampal.farm2home.dao.interfaces;

import com.rampal.farm2home.model.Product;
import java.sql.SQLException;
import java.util.List;

public interface ProductDAOInterface {
    List<Product> findAll() throws SQLException, ClassNotFoundException;
    List<Product> findLatest(int limit) throws SQLException, ClassNotFoundException;
    List<Product> findByCategoryId(int categoryId) throws SQLException, ClassNotFoundException;
    Product findById(int productId) throws SQLException, ClassNotFoundException;
    List<Product> findByIdsPreserveOrder(List<Integer> productIds) throws SQLException, ClassNotFoundException;
    boolean create(Product product) throws SQLException, ClassNotFoundException;
    boolean update(Product product) throws SQLException, ClassNotFoundException;
    boolean delete(int productId) throws SQLException, ClassNotFoundException;
    int countProducts() throws SQLException, ClassNotFoundException;
}
