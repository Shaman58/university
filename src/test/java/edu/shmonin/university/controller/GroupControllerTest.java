package edu.shmonin.university.controller;

import config.ApplicationConfig;
import edu.shmonin.university.model.Group;
import edu.shmonin.university.service.GroupService;
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
class GroupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(groupController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void whenGetPage_thenAddGroupPageToModel() throws Exception {
        var group1 = new Group("group1");
        var group2 = new Group("group2");
        group1.setId(1);
        group2.setId(2);
        var pageRequest = PageRequest.of(0, 20);
        var page = new PageImpl<>(List.of(group1, group2), pageRequest, 2);
        when(groupService.getAll(pageRequest)).thenReturn(page);

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/index"))
                .andExpect(forwardedUrl("groups/index"))
                .andExpect(model().attribute("page", page));
    }

    @Test
    void whenGet_thenAddGroupToModel() throws Exception {
        var group = new Group("group");
        group.setId(1);
        when(groupService.get(1)).thenReturn(group);

        mockMvc.perform(get("/groups/{id}/get", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/group"))
                .andExpect(forwardedUrl("groups/group"))
                .andExpect(model().attribute("group", group));
    }
}