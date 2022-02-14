package edu.shmonin.university.service;

import edu.shmonin.university.dao.GroupDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ForeignReferenceException;
import edu.shmonin.university.model.Group;
import edu.shmonin.university.model.Lecture;
import edu.shmonin.university.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupDao groupDao;
    @Mock
    private LectureDao lectureDao;

    @InjectMocks
    private GroupService groupService;

    @Test
    void givenId_whenGet_thenReturnedGroup() {
        var expected = new Group("group");
        when(groupDao.get(1)).thenReturn(Optional.of(expected));

        var actual = groupService.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnedAllGroups() {
        var expected = List.of(new Group("group1"), new Group("group2"));
        when(groupDao.getAll()).thenReturn(expected);

        var actual = groupService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenGroup_whenCreate_thenStartedGroupDaoCreate() {
        var group = new Group("group1");

        groupService.create(group);

        verify(groupDao).create(group);
    }

    @Test
    void givenGroup_whenUpdate_thenStartedDaoUpdate() {
        var group = new Group("group1");

        groupService.update(group);

        verify(groupDao).update(group);
    }

    @Test
    void givenIdAndEmptyListOfGroupStudentsAndGroupLecturesAndGroupDaoGetReturnNotEmptyOptional_whenDelete_thenStartedGroupDaoDelete() {
        var group = new Group("group");
        group.setStudents(new ArrayList<>());
        when(lectureDao.getByGroupId(1)).thenReturn(new ArrayList<>());
        when(groupDao.get(1)).thenReturn(Optional.of(group));

        groupService.delete(1);

        verify(groupDao).delete(1);
    }

    @Test
    void givenIdAndNotEmptyListOfGroupStudentsAndGroupDaoGetReturnNotEmptyOptional_whenDelete_thenThrowForeignReferenceExceptionNotStartedGroupDaoDelete() {
        var group = new Group("group");
        group.setStudents(List.of(new Student()));
        when(groupDao.get(1)).thenReturn(Optional.of(group));

        var exception = assertThrows(ForeignReferenceException.class, () -> groupService.delete(1));

        verify(groupDao, never()).delete(1);
        assertEquals("There are students with this group", exception.getMessage());
    }

    @Test
    void givenIdAndNotEmptyListOfGroupLecturesAndGroupStudentsAndGroupDaoGetReturnNotEmptyOptional_whenDelete_thenThrowForeignReferenceExceptionAndNotStartedGroupDaoDelete() {
        var group = new Group("group");
        group.setStudents(new ArrayList<>());
        when(lectureDao.getByGroupId(1)).thenReturn(List.of(new Lecture()));
        when(groupDao.get(1)).thenReturn(Optional.of(group));

        var exception = assertThrows(ForeignReferenceException.class, () -> groupService.delete(1));

        verify(groupDao, never()).delete(1);
        assertEquals("There are lectures with this group", exception.getMessage());
    }

    @Test
    void givenIdAndGroupDaoGetReturnEmptyOptional_whenDelete_thenThrowEntityNotFoundExceptionAndNotStartedGroupDaoDelete() {
        when(groupDao.get(1)).thenReturn(Optional.empty());

        var exception = assertThrows(EntityNotFoundException.class, () -> groupService.delete(1));

        verify(groupDao, never()).delete(1);
        assertEquals("Can not find group by id=1", exception.getMessage());
    }
}