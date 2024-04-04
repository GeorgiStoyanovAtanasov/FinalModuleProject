package com.example.demo;

import com.example.demo.Controllers.MiningController;
import com.example.demo.Repositories.CrystalMineRepository;
import com.example.demo.Repositories.GoldMineRepository;
import com.example.demo.Repositories.SilverMineRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Services.CrystalMineService;
import com.example.demo.Services.GoldMineService;
import com.example.demo.Services.SilverMineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(roles = "USER")
@WebMvcTest(MiningController.class)
@AutoConfigureMockMvc
public class MiningControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoldMineService goldMineService;

    @MockBean
    private SilverMineService silverMineService;

    @MockBean
    private CrystalMineService crystalMineService;
    @MockBean
    private GoldMineRepository goldMineRepository;
    @MockBean
    private SilverMineRepository silverMineRepository;

    @MockBean
    private CrystalMineRepository crystalMineRepository;

    @MockBean
    private PlayerRepository playerRepository;

    @Test
    public void testMineChoice() throws Exception {
        mockMvc.perform(get("/mine"))
                .andExpect(status().isOk())
                .andExpect(view().name("mine-choice"));
    }

    @Test
    public void testMineGold() throws Exception {
        mockMvc.perform(get("/mine/gold"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetGoldSuccessMessage() throws Exception {
        mockMvc.perform(get("/gold/mine/success"))
                .andExpect(status().isOk())
                .andExpect(view().name("gold-success-message"));
    }
}
