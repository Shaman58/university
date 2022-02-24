package edu.shmonin.university.controller;

import config.ApplicationConfig;
import edu.shmonin.university.model.Vacation;
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
class VacationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VacationService vacationService;

    @InjectMocks
    private VacationController vacationController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(vacationController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void whenGetPage_thenAddVacationPageToModel() throws Exception {
        var vacation1 = new Vacation(LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 2, 2));
        var vacation2 = new Vacation(LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 3, 3));
        var pageRequest = PageRequest.of(0, 20);
        var page = new PageImpl<>(List.of(vacation1, vacation2), pageRequest, 2);
        when(vacationService.getByTeacherId(pageRequest, 1)).thenReturn(page);
        mockMvc.perform(get("/teachers/{teacherId}/vacations", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("vacations/all"))
                .andExpect(forwardedUrl("vacations/all"))
                .andExpect(model().attribute("page", page))
                .andExpect(model().attribute("teacherId", 1));
    }

    @Test
    void whenGet_thenAddVacationToModel() throws Exception {
        var vacation = new Vacation(LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 2, 2));
        vacation.setId(1);
        when(vacationService.get(1)).thenReturn(vacation);

        mockMvc.perform(get("/teachers/{teacherId}/vacations/{id}/get", 1, 1))
                .andExpect(status().isOk())
                .andExpect(view().name("vacations/vacation"))
                .andExpect(forwardedUrl("vacations/vacation"))
                .andExpect(model().attribute("vacation", vacation));
    }
}