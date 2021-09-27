package edu.shmonin.university.dao;

import edu.shmonin.university.model.Group;

import java.util.List;

public interface GroupDao extends Dao<Group> {
    List<Group> getLectureGroups(int lectureId);
}