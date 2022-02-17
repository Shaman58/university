package edu.shmonin.university.controller;

import config.ApplicationConfig;
import edu.shmonin.university.model.Course;
import edu.shmonin.university.service.CourseService;
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

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@WebAppConfiguration
class CourseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void whenGetPage_thenAddCoursePageToModel() throws Exception {
        var course1 = new Course("course1");
        var course2 = new Course("course2");
        course1.setId(1);
        course2.setId(2);
        var pageRequest = PageRequest.of(0, 20);
        var page = new PageImpl<>(List.of(course1, course2), pageRequest, 2);
        when(courseService.getAll(pageRequest)).thenReturn(page);

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/index"))
                .andExpect(forwardedUrl("courses/index"))
                .andExpect(model().attribute("page", page));
    }

    @Test
    void whenGet_thenAddCourseToModel() throws Exception {
        var course = new Course("course");
        course.setId(1);
        when(courseService.get(1)).thenReturn(course);

        mockMvc.perform(get("/courses/{id}/get", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/course"))
                .andExpect(forwardedUrl("courses/course"))
                .andExpect(model().attribute("course", course));
    }
}