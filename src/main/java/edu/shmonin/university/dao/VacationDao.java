package edu.shmonin.university.dao;

import edu.shmonin.university.model.Vacation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VacationDao extends Dao<Vacation> {

    List<Vacation> getByTeacherId(int teacherId);

    Optional<Vacation> getByTeacherIdAndDate(int teacherId, LocalDate date);

    Page<Vacation> getByTeacherId(Pageable pageable, int teacherId);
}