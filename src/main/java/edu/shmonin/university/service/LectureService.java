package edu.shmonin.university.service;

import edu.shmonin.university.model.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LectureService extends EntityService<Lecture> {

    Page<Lecture> getAll(Pageable pageable);
}
