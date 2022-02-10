package edu.shmonin.university.service;

import edu.shmonin.university.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService extends EntityService<Course> {

    Page<Course> getAll(Pageable pageable);

    List<Course> getByTeacherId(int teacherId);
}