package edu.shmonin.university.dao;

import edu.shmonin.university.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseDao extends Dao<Course> {

    List<Course> getByTeacherId(int teacherId);

    Page<Course> getAllSortedPaginated(Pageable pageable);
}