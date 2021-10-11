package edu.shmonin.university.dao;

import edu.shmonin.university.model.Vacation;

import java.util.List;

public interface VacationDao extends Dao<Vacation> {
    List<Vacation> getByTeacherId(int teacherId);
}