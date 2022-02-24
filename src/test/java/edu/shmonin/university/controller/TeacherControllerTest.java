package edu.shmonin.university.controller;

import config.ApplicationConfig;
import edu.shmonin.university.model.Gender;
import edu.shmonin.university.model.ScientificDegree;
import edu.shmonin.university.model.Teacher;
import edu.shmonin.university.model.Vacation;
import edu.shmonin.university.service.TeacherService;
import edu.shmonin.university.service.VacationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@WebAppConfiguration
class TeacherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;
    @Mock
    VacationService vacationService;

    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(teacherController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void whenGetPage_thenAddTeacherPageToModel() throws Exception {
        var teacher1 = new Teacher();
        teacher1.setFirstName("name-1");
        teacher1.setLastName("surname-1");
        teacher1.setEmail("email-1");
        teacher1.setCountry("country-1");
        teacher1.setGender(Gender.MALE);
        teacher1.setPhone("phone-1");
        teacher1.setAddress("address-1");
        teacher1.setBirthDate(LocalDate.now().minusYears(24));
        teacher1.setScientificDegree(ScientificDegree.DOCTOR);
        teacher1.setId(1);
        var teacher2 = new Teacher();
        teacher2.setFirstName("name-2");
        teacher2.setLastName("surname-2");
        teacher2.setEmail("email-2");
        teacher2.setCountry("country-2");
        teacher2.setGender(Gender.MALE);
        teacher2.setPhone("phone-2");
        teacher2.setAddress("address-2");
        teacher2.setBirthDate(LocalDate.now().minusYears(24));
        teacher2.setScientificDegree(ScientificDegree.DOCTOR);
        teacher2.setId(2);
        var pageRequest = PageRequest.of(0, 20);
        var page = new PageImpl<>(List.of(teacher1, teacher2), pageRequest, 2);
        when(teacherService.getAll(pageRequest)).thenReturn(page);

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/all"))
                .andExpect(forwardedUrl("teachers/all"))
                .andExpect(model().attribute("page", page));
    }

    @Test
    void whenGet_thenAddTeacherToModel() throws Exception {
        var teacher = new Teacher();
        teacher.setFirstName("name-1");
        teacher.setLastName("surname-1");
        teacher.setEmail("email-1");
        teacher.setCountry("country-1");
        teacher.setGender(Gender.MALE);
        teacher.setPhone("phone-1");
        teacher.setAddress("address-1");
        teacher.setBirthDate(LocalDate.now().minusYears(24));
        teacher.setScientificDegree(ScientificDegree.DOCTOR);
        teacher.setId(1);
        var vacations = List.of(new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1)));
        when(teacherService.get(1)).thenReturn(teacher);
        when(vacationService.getByTeacherIdAndAcademicYear(1)).thenReturn(vacations);

        mockMvc.perform(get("/teachers/{id}/get", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/teacher"))
                .andExpect(forwardedUrl("teachers/teacher"))
                .andExpect(model().attribute("teacher", teacher))
                .andExpect(model().attribute("vacations", vacations));
    }
}