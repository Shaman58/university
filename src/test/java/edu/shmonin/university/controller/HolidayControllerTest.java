package edu.shmonin.university.controller;

import config.ApplicationConfig;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@WebAppConfiguration
class HolidayControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HolidayService holidayService;

    @InjectMocks
    private HolidaysController holidaysController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(holidaysController)
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
                .andExpect(view().name("holidays/index"))
                .andExpect(forwardedUrl("holidays/index"))
                .andExpect(model().attribute("page", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("name", is("holiday1")),
                                hasProperty("date", is(LocalDate.now().plus(1, ChronoUnit.DAYS)))
                        )))).
                andExpect(model().attribute("page", hasItem(
                        allOf(
                                hasProperty("id", is(2)),
                                hasProperty("name", is("holiday2")),
                                hasProperty("date", is(LocalDate.now().plus(1, ChronoUnit.DAYS)))
                        ))));

        verify(holidayService, times(1)).getAll(pageRequest);
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

        verify(holidayService, times(1)).get(1);
    }
}