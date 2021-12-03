package edu.shmonin.university.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(int id);

    List<T> getAll();

    void create(T entity);

    void update(T entity);

    void delete(int id);
}