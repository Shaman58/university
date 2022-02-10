package edu.shmonin.university.service;

import edu.shmonin.university.model.Course;
import edu.shmonin.university.model.Duration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DurationService extends EntityService<Duration> {

    Page<Duration> getAll(Pageable pageable);
}
