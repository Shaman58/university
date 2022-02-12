package edu.shmonin.university.controller;

import config.ApplicationConfig;
import edu.shmonin.university.model.Duration;
import edu.shmonin.university.service.DurationService;
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

import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@WebAppConfiguration
class DurationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DurationService durationService;

    @InjectMocks
    private DurationController durationController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(durationController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void whenGetPage_thenAddDurationPageToModel() throws Exception {
        var duration1 = new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0));
        var duration2 = new Duration(LocalTime.of(15, 0), LocalTime.of(17, 0));
        duration1.setId(1);
        duration2.setId(2);
        var pageRequest = PageRequest.of(0, 20);
        var page = new PageImpl<>(List.of(duration1, duration2), pageRequest, 2);
        when(durationService.getAll(pageRequest)).thenReturn(page);

        mockMvc.perform(get("/durations"))
                .andExpect(status().isOk())
                .andExpect(view().name("durations/index"))
                .andExpect(forwardedUrl("durations/index"))
                .andExpect(model().attribute("page", hasItem(
                        allOf(
                                hasProperty("startTime", is(LocalTime.of(12, 0))),
                                hasProperty("endTime", is(LocalTime.of(14, 0)))
                        ))))
                .andExpect(model().attribute("page", hasItem(
                        allOf(
                                hasProperty("startTime", is(LocalTime.of(15, 0))),
                                hasProperty("endTime", is(LocalTime.of(17, 0)))
                        ))));

        verify(durationService, times(1)).getAll(pageRequest);
    }

    @Test
    void whenGet_thenAddDurationToModel() throws Exception {
        var duration = new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0));
        duration.setId(1);
        when(durationService.get(1)).thenReturn(duration);

        mockMvc.perform(get("/durations/{id}/get", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("durations/duration"))
                .andExpect(forwardedUrl("durations/duration"))
                .andExpect(model().attribute("duration", duration));

        verify(durationService, times(1)).get(1);
    }
}