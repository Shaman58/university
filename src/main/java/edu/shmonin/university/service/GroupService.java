package edu.shmonin.university.service;

import edu.shmonin.university.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupService extends EntityService<Group> {

    Page<Group> getAll(Pageable pageable);
}
