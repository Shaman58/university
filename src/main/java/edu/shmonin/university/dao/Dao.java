package edu.shmonin.university.dao;

import java.util.List;

public interface Dao<T> {

    T get(int id);

    List<T> getAll();

    void create(T entity);

    void update(T entity);

    void delete(int id);
}