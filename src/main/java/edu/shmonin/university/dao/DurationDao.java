package edu.shmonin.university.dao;

import edu.shmonin.university.model.Duration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DurationDao extends Dao<Duration> {

    Page<Duration> getAllSortedPaginated(Pageable pageable);
}