package edu.shmonin.university.service;

import java.util.List;

public interface EntityService<T> {

    T get(int id);

    List<T> getAll();

    void create(T entity);

    void update(T entity);

    void delete(int id);
}