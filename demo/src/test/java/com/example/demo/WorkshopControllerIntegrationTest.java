package com.example.demo;

import com.example.demo.Controllers.WorkshopController;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.*;
import com.example.demo.Services.WorkshopService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@WebMvcTest(WorkshopController.class)
@AutoConfigureMockMvc
public class WorkshopControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerRepository playerRepository;
    @MockBean
    private ArcherRepository archerRepository;

    @MockBean
    private SwordsmanRepository swordsmanRepository;

    @MockBean
    private CavalryRepository cavalryRepository;
    @MockBean
    private ArcherWorkshopRepository archerWorkshopRepository;

    @MockBean
    private SwordsmanWorkshopRepository swordsmanWorkshopRepository;

    @MockBean
    private CavalryWorkshopRepository cavalryWorkshopRepository;

    @MockBean
    private WorkshopService workshopService;

    @Test
    @WithMockUser(roles = "USER")
    public void testChooseWorkshopToBuyForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/buy/workshop"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("choose-workshop"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testBuyWorkshopArcher() throws Exception {
        Player player = new Player();
        player.setUsername("testUser");

        when(playerRepository.findByUsername("testUser")).thenReturn(player);
        when(workshopService.buyArcherWorkshop(player)).thenReturn("redirect:/home");

        mockMvc.perform(MockMvcRequestBuilders.post("/buy/workshop")
                        .param("workshopType", "archer")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));
    }
}
