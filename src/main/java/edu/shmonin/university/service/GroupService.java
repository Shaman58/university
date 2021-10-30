package edu.shmonin.university.service;

import edu.shmonin.university.dao.GroupDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.model.Group;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService implements EntityService<Group> {

    @Value("${university.group.capacity.max}")
    private int maxCapacity;

    private final GroupDao jdbcGroupDao;
    private final LectureDao jdbcLectureDao;

    public GroupService(GroupDao jdbcGroupDao, LectureDao jdbcLectureDao) {
        this.jdbcGroupDao = jdbcGroupDao;
        this.jdbcLectureDao = jdbcLectureDao;
    }

    @Override
    public Group get(int groupId) {
        return jdbcGroupDao.get(groupId);
    }

    @Override
    public List<Group> getAll() {
        return jdbcGroupDao.getAll();
    }

    @Override
    public void create(Group group) {
        if (validateGroup(group)) {
            jdbcGroupDao.create(group);
        }
    }

    @Override
    public void update(Group group) {
        if (validateGroup(group)) {
            jdbcGroupDao.update(group);
        }
    }

    @Override
    public void delete(int groupId) {
        if (jdbcLectureDao.getByGroupId(groupId).isEmpty() && jdbcGroupDao.get(groupId).getStudents().isEmpty()) {
            jdbcGroupDao.delete(groupId);
        }
    }

    private boolean validateGroup(Group group) {
        return maxCapacity > group.getStudents().size();
    }
}