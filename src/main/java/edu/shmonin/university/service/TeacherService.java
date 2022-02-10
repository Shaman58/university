package edu.shmonin.university.service;

import edu.shmonin.university.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeacherService extends EntityService<Teacher> {

    Page<Teacher> getAll(Pageable pageable);
}
