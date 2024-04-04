package com.example.demo;

import com.example.demo.Controllers.PlayerController;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Services.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//@WithMockUser(roles = "USER")
@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void testLoginForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testLoginSuccess() throws Exception {
        String username = "testUser";
        String password = "password";
        Player player = new Player();
        player.setUsername(username);
        player.setPassword(passwordEncoder.encode(password));
        when(playerRepository.findByUsername(username)).thenReturn(player);
        when(passwordEncoder.matches(password, player.getPassword())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }
}
