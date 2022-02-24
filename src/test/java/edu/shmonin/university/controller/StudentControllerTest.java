package edu.shmonin.university.controller;

import config.ApplicationConfig;
import edu.shmonin.university.model.Gender;
import edu.shmonin.university.model.Student;
import edu.shmonin.university.service.StudentService;
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
class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void whenGetPage_thenAddStudentPageToModel() throws Exception {
        var student1 = new Student();
        student1.setFirstName("name-1");
        student1.setLastName("surname-1");
        student1.setEmail("email-1");
        student1.setCountry("country-1");
        student1.setGender(Gender.MALE);
        student1.setPhone("phone-1");
        student1.setAddress("address-1");
        student1.setBirthDate(LocalDate.of(1980, 1, 1));
        student1.setId(1);
        var student2 = new Student();
        student2.setFirstName("name-2");
        student2.setLastName("surname-2");
        student2.setEmail("email-2");
        student2.setCountry("country-2");
        student2.setGender(Gender.MALE);
        student2.setPhone("phone-2");
        student2.setAddress("address-2");
        student2.setBirthDate(LocalDate.of(1980, 2, 2));
        student2.setId(2);
        var pageRequest = PageRequest.of(0, 20);
        var page = new PageImpl<>(List.of(student1, student2), pageRequest, 2);
        when(studentService.getAll(pageRequest)).thenReturn(page);
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/all"))
                .andExpect(forwardedUrl("students/all"))
                .andExpect(model().attribute("page", page));
    }

    @Test
    void whenGet_thenAddStudentToModel() throws Exception {
        var student = new Student();
        student.setFirstName("name");
        student.setLastName("surname");
        student.setEmail("email");
        student.setCountry("country");
        student.setGender(Gender.MALE);
        student.setPhone("phone");
        student.setAddress("address");
        student.setBirthDate(LocalDate.of(1980, 1, 1));
        student.setId(1);
        when(studentService.get(1)).thenReturn(student);

        mockMvc.perform(get("/students/{id}/get", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("students/student"))
                .andExpect(forwardedUrl("students/student"))
                .andExpect(model().attribute("student", student));
    }
}