package edu.shmonin.university.dao;

import edu.shmonin.university.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeacherDao extends Dao<Teacher> {

    List<Teacher> getByCourseId(int courseId);

    Page<Teacher> getAllSortedPaginated(Pageable pageable);
}