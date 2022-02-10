package edu.shmonin.university.service;

import edu.shmonin.university.model.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HolidayService extends EntityService<Holiday> {

    Page<Holiday> getAll(Pageable pageable);
}
