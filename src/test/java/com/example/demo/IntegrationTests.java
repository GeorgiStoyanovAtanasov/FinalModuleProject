package com.example.demo;

import com.example.demo.Entities.Materials.Crystal;
import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Materials.Silver;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.ArcherRepository;
import com.example.demo.Repositories.CavalryRepository;
import com.example.demo.Repositories.PlayerRepository;

import com.example.demo.Repositories.SwordsmanRepository;
import com.example.demo.Services.AttackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
//@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {
    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ArcherRepository archerRepository;

    @Mock
    private SwordsmanRepository swordsmanRepository;

    @Mock
    private CavalryRepository cavalryRepository;
    @InjectMocks
    private AttackService attackService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLoginForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("<title>Imperia Online: The Redemption</title>")))
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("<h2 class=\"text-center\">Imperia Online</h2>")))
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("<h2 class=\"text-center\">The Redemption</h2>")))
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("<button type=\"submit\" class=\"btn btn-block\">Sign in</button>")))
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("<label for=\"username\">Username:</label>")))
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("<label for=\"password\">Password:</label>")))
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("<button type=\"submit\" class=\"btn btn-block\">Sign in</button>")))
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("<a href=\"http://localhost:8080/register\">Sign up:</a>")));
    }
    @Test
    @WithMockUser
    public void testPerformLogout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/logout"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    }
    @Test
    @WithMockUser(username = "ggg", roles = "USER")
    public void testHome() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/home"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("home"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("gold"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("silver"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("crystal"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("archerWorkshops"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("swordsmanWorkshops"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("cavalryWorkshops"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("archers"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("swordsman"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("cavalries"));
    }
    @Test
    public void testRegisterForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("player"));

    }
    @Test
    @WithMockUser(username = "ggg")
    public void testAttackFrom() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/attack"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("choose-player-to-attack"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("players"));
    }
    @Test
    @WithMockUser(username = "ggg")
    public void testDefendForm_RedirectToHome_NoAttack() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/defend"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));
    }
    @Test
    @WithMockUser(username = "ggg")
    public void testChooseArcherWorkshopForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/add/archer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("archerWorkshops"))
                .andExpect(MockMvcResultMatchers.view().name("choose-archerWorkshop-to-add-archer"));
    }
    @Test
    @WithMockUser(username = "ggg", roles = "USER")
    public void testChooseSwordsmanWorkshopForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/add/swordsman"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("swordsmanWorkshops"))
                .andExpect(MockMvcResultMatchers.view().name("choose-swordsmanWorkshop-to-add-swordsman"));
    }
    @Test
    @WithMockUser(username = "ggg", roles = "USER")
    public void testChooseCavalryWorkshopForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/add/cavalry"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("cavalryWorkshops"))
                .andExpect(MockMvcResultMatchers.view().name("choose-cavalryWorkshop-to-add-cavalry"));
    }
    @Test
    @WithMockUser(username = "ggg")
    public void testBuyWorkshopForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/buy/workshop"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("choose-workshop"));
    }
}