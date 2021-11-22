package edu.shmonin.university.service;

import edu.shmonin.university.dao.GroupDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.LinkedEntityException;
import edu.shmonin.university.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService implements EntityService<Group> {

    private static final Logger log = LoggerFactory.getLogger(GroupService.class);

    private final GroupDao jdbcGroupDao;
    private final LectureDao jdbcLectureDao;

    public GroupService(GroupDao jdbcGroupDao, LectureDao jdbcLectureDao) {
        this.jdbcGroupDao = jdbcGroupDao;
        this.jdbcLectureDao = jdbcLectureDao;
    }

    @Override
    public Group get(int groupId) {
        try {
            log.debug("Get group with id={}", groupId);
            return jdbcGroupDao.get(groupId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not find the group. There are no group with id=" + groupId);
        }
    }

    @Override
    public List<Group> getAll() {
        log.debug("Get all groups");
        return jdbcGroupDao.getAll();
    }

    @Override
    public void create(Group group) {
        log.debug("Create group {}", group);
        jdbcGroupDao.create(group);
    }

    @Override
    public void update(Group group) {
        log.debug("Update audience {}", group);
        jdbcGroupDao.update(group);
    }

    @Override
    public void delete(int groupId) {
        try {
            jdbcGroupDao.get(groupId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not delete the group. There is no group with id=" + groupId);
        }
        if (jdbcLectureDao.getByGroupId(groupId).isEmpty()) {
            throw new LinkedEntityException("Can not delete group with id=" + groupId + ", there are lectures with this group in database");
        }
        if (jdbcGroupDao.get(groupId).getStudents().isEmpty()) {
            throw new LinkedEntityException("Can not delete group with id=" + groupId + ", there are students with this group in database");
        }
        log.debug("Delete group by id={}", groupId);
        jdbcGroupDao.delete(groupId);
    }
}