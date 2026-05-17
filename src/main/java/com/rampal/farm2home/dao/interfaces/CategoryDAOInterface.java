package com.rampal.farm2home.dao.interfaces;

import com.rampal.farm2home.model.Category;
import java.sql.SQLException;
import java.util.List;

public interface CategoryDAOInterface {
    List<Category> findAll() throws SQLException, ClassNotFoundException;
    Category findById(int categoryId) throws SQLException, ClassNotFoundException;
    boolean create(Category category) throws SQLException, ClassNotFoundException;
    boolean update(Category category) throws SQLException, ClassNotFoundException;
    boolean delete(int categoryId) throws SQLException, ClassNotFoundException;
    int countCategories() throws SQLException, ClassNotFoundException;
}
