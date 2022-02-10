package edu.shmonin.university.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(int id);

    List<T> getAll();

    void create(T entity);

    void update(T entity);

    void delete(int id);

    Page<T> getAll(Pageable pageable);
}