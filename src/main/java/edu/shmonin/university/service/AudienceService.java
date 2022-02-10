package edu.shmonin.university.service;

import edu.shmonin.university.model.Audience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AudienceService extends EntityService<Audience> {

    Page<Audience> getAll(Pageable pageable);
}
