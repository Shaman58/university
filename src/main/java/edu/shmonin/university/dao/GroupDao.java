package edu.shmonin.university.dao;

import edu.shmonin.university.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupDao extends Dao<Group> {

    List<Group> getByLectureId(int lectureId);

    Page<Group> getAllSortedPaginated(Pageable pageable);
}