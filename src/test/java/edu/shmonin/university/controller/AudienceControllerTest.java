package edu.shmonin.university.controller;

import edu.shmonin.university.config.ApplicationConfig;
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
class AudienceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AudienceService audienceService;

    @InjectMocks
    private AudienceController audienceController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(audienceController)
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
                .andExpect(view().name("audiences/all"))
                .andExpect(forwardedUrl("audiences/all"))
                .andExpect(model().attribute("page", page));
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
    }
}