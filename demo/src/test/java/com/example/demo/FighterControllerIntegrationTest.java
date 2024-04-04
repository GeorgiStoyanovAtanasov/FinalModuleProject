package com.example.demo;

import com.example.demo.Controllers.FighterController;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.ArcherRepository;
import com.example.demo.Repositories.ArcherWorkshopRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Services.ArcherWorkshopService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.Mockito.when;

@WithMockUser(roles = "USER")
@ExtendWith(SpringExtension.class)
@WebMvcTest(FighterController.class)
@AutoConfigureMockMvc
public class FighterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private ArcherWorkshopRepository archerWorkshopRepository;

    @MockBean
    private ArcherWorkshopService archerWorkshopService;
    @MockBean
    private ArcherRepository archerRepository;

    @Test
    public void testChooseArcherWorkshopForm() throws Exception {
        Player player = new Player();
        player.setUsername("testUser");

        when(playerRepository.findByUsername("testUser")).thenReturn(player);
        when(archerWorkshopRepository.findAllByPlayer(player)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/add/archer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("choose-archerWorkshop-to-add-archer"));
    }
}
