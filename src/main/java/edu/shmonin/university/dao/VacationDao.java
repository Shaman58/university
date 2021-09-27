package edu.shmonin.university.dao;

import edu.shmonin.university.model.Teacher;
import edu.shmonin.university.model.Vacation;

public interface VacationDao extends Dao<Vacation> {
    void setTeacherVacation(Vacation vacation, Teacher teacher);
}