package edu.shmonin.university.dao;

import edu.shmonin.university.model.Audience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AudienceDao extends Dao<Audience> {

    Page<Audience> getAllSortedPaginated(Pageable pageable);
}