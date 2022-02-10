package edu.shmonin.university.service;

import edu.shmonin.university.model.Vacation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VacationService extends EntityService<Vacation> {

    Page<Vacation> getByTeacherId(Pageable pageable, int teacherId);

    List<Vacation> getByTeacherId(int teacherId);
}
