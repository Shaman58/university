package edu.shmonin.university.dao;

import edu.shmonin.university.model.Vacation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VacationDao extends Dao<Vacation> {
    List<Vacation> getByTeacherId(int teacherId);

    Optional<Vacation> getByTeacherIdAndDate(int teacherId, LocalDate date);
}