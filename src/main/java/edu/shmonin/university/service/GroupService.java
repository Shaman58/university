package edu.shmonin.university.service;

import edu.shmonin.university.dao.GroupDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ChainedEntityException;
import edu.shmonin.university.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService implements EntityService<Group> {

    private static final Logger log = LoggerFactory.getLogger(GroupService.class);

    private final GroupDao groupDao;
    private final LectureDao lectureDao;

    public GroupService(GroupDao groupDao, LectureDao lectureDao) {
        this.groupDao = groupDao;
        this.lectureDao = lectureDao;
    }

    @Override
    public Group get(int groupId) {
        var group = groupDao.get(groupId);
        if (group.isEmpty()) {
            throw new EntityNotFoundException("Can not find the group. There are no group with id=" + groupId);
        }
        log.debug("Get group with id={}", groupId);
        return group.get();
    }

    @Override
    public List<Group> getAll() {
        log.debug("Get all groups");
        return groupDao.getAll();
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
        var group = groupDao.get(groupId);
        if (group.isEmpty()) {
            throw new EntityNotFoundException("Can not delete the group. There is no group with id=" + groupId);
        }
        if (!group.get().getStudents().isEmpty()) {
            throw new ChainedEntityException("Can not delete group with id=" + groupId + ", there are students with this group in database");
        }
        if (!lectureDao.getByGroupId(groupId).isEmpty()) {
            throw new ChainedEntityException("Can not delete group with id=" + groupId + ", there are lectures with this group in database");
        }
        log.debug("Delete group by id={}", groupId);
        groupDao.delete(groupId);
    }
}