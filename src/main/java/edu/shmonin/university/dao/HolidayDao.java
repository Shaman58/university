package edu.shmonin.university.dao;

import edu.shmonin.university.model.Holiday;

import java.time.LocalDate;
import java.util.Optional;

public interface HolidayDao extends Dao<Holiday> {
    Optional<Holiday> getByDate(LocalDate localDate);
}