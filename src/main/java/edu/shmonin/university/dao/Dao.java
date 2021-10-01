package edu.shmonin.university.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {

    T get(int id);

    List<T> getAll();

    void create(T entity) throws SQLException;

    void update(T entity);

    void delete(int id);
}