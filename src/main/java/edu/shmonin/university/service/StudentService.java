package edu.shmonin.university.service;

import edu.shmonin.university.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService extends EntityService<Student> {

    Page<Student> getAll(Pageable pageable);

    List<Student> getByGroupId(int groupId);
}
