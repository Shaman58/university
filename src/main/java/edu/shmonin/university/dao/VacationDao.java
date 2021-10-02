package edu.shmonin.university.dao;

import edu.shmonin.university.model.Teacher;
import edu.shmonin.university.model.Vacation;

import java.util.List;

public interface VacationDao extends Dao<Vacation> {
    List<Vacation> getTeacherVacations(int teacherId);

    void setTeacherVacation(Vacation vacation, Teacher teacher);
}