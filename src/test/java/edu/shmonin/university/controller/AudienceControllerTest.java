package edu.shmonin.university.controller;

import config.ApplicationConfig;
import edu.shmonin.university.model.Audience;
import edu.shmonin.university.service.AudienceService;
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

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@WebAppConfiguration
class AudienceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AudienceService audienceService;

    @InjectMocks
    private AudiencesController audiencesController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(audiencesController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void whenGetPage_thenAddAudiencePageToModel() throws Exception {
        var audience1 = new Audience(1, 50);
        var audience2 = new Audience(2, 60);
        audience1.setId(1);
        audience2.setId(2);
        var pageRequest = PageRequest.of(0, 20);
        var page = new PageImpl<>(List.of(audience1, audience2), pageRequest, 2);
        when(audienceService.getAll(pageRequest)).thenReturn(page);

        mockMvc.perform(get("/audiences"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/index"))
                .andExpect(forwardedUrl("audiences/index"))
                .andExpect(model().attribute("page", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("roomNumber", is(1)),
                                hasProperty("capacity", is(50))
                        )))).
                andExpect(model().attribute("page", hasItem(
                        allOf(
                                hasProperty("id", is(2)),
                                hasProperty("roomNumber", is(2)),
                                hasProperty("capacity", is(60))
                        ))));

        verify(audienceService, times(1)).getAll(pageRequest);
    }

    @Test
    void whenGet_thenAddAudienceToModel() throws Exception {
        var audience = new Audience(1, 50);
        audience.setId(1);
        when(audienceService.get(1)).thenReturn(audience);

        mockMvc.perform(get("/audiences/{id}/get", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/audience"))
                .andExpect(forwardedUrl("audiences/audience"))
                .andExpect(model().attribute("audience", audience));

        verify(audienceService, times(1)).get(1);
    }
}