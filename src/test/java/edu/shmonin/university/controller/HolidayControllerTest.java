package edu.shmonin.university.controller;

import edu.shmonin.university.config.ApplicationConfig;
import edu.shmonin.university.model.Holiday;
import edu.shmonin.university.service.HolidayService;
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
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@WebAppConfiguration
class HolidayControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HolidayService holidayService;

    @InjectMocks
    private HolidayController holidayController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(holidayController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void whenGetPage_thenAddHolidayPageToModel() throws Exception {
        var holiday1 = new Holiday("holiday1", LocalDate.now().plus(1, ChronoUnit.DAYS));
        var holiday2 = new Holiday("holiday2", LocalDate.now().plus(1, ChronoUnit.DAYS));
        holiday1.setId(1);
        holiday2.setId(2);
        var pageRequest = PageRequest.of(0, 20);
        var page = new PageImpl<>(List.of(holiday1, holiday2), pageRequest, 2);
        when(holidayService.getAll(pageRequest)).thenReturn(page);

        mockMvc.perform(get("/holidays"))
                .andExpect(status().isOk())
                .andExpect(view().name("holidays/all"))
                .andExpect(forwardedUrl("holidays/all"))
                .andExpect(model().attribute("page", page));
    }

    @Test
    void whenGet_thenAddHolidayToModel() throws Exception {
        var holiday = new Holiday("holiday1", LocalDate.now().plus(1, ChronoUnit.DAYS));
        holiday.setId(1);
        when(holidayService.get(1)).thenReturn(holiday);

        mockMvc.perform(get("/holidays/{id}/get", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("holidays/holiday"))
                .andExpect(forwardedUrl("holidays/holiday"))
                .andExpect(model().attribute("holiday", holiday));
    }
}