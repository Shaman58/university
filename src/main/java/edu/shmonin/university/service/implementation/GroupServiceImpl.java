package edu.shmonin.university.service.implementation;

import edu.shmonin.university.dao.GroupDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ForeignReferenceException;
import edu.shmonin.university.model.Group;
import edu.shmonin.university.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private static final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupDao groupDao;
    private final LectureDao lectureDao;

    public GroupServiceImpl(GroupDao groupDao, LectureDao lectureDao) {
        this.groupDao = groupDao;
        this.lectureDao = lectureDao;
    }

    @Override
    public Group get(int groupId) {
        log.debug("Get group with id={}", groupId);
        return groupDao.get(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Can not find group by id=" + groupId));
    }

    @Override
    public List<Group> getAll() {
        log.debug("Get all groups");
        return groupDao.getAll();
    }

    @Override
    public Page<Group> getAll(Pageable pageable) {
        log.debug("Get all sorted groups");
        return groupDao.getAll(pageable);
    }

    @Override
    public void create(Group group) {
        log.debug("Create group {}", group);
        groupDao.create(group);
    }

    @Override
    public void update(Group group) {
        log.debug("Update audience {}", group);
        groupDao.update(group);
    }

    @Override
    public void delete(int groupId) {
        log.debug("Delete group by id={}", groupId);
        if (!this.get(groupId).getStudents().isEmpty()) {
            throw new ForeignReferenceException("There are students with this group");
        }
        if (!lectureDao.getByGroupId(groupId).isEmpty()) {
            throw new ForeignReferenceException("There are lectures with this group");
        }
        groupDao.delete(groupId);
    }
}