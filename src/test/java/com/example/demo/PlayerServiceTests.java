package com.example.demo;

import com.example.demo.Entities.Player;
import com.example.demo.Repositories.CrystalRepository;
import com.example.demo.Repositories.GoldRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Repositories.SilverRepository;
import com.example.demo.Services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PlayerServiceTests {
    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private GoldRepository goldRepository;

    @Mock
    private SilverRepository silverRepository;

    @Mock
    private CrystalRepository crystalRepository;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private Model model;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PlayerService playerService;

    @Test
    public void testRegisterPlayer_SuccessfulRegistration() {

        Player player = new Player();
        player.setUsername("testUser");
        player.setPassword("testPassword");


        when(playerRepository.findByUsername(anyString())).thenReturn(null);



        String result = playerService.registerPlayer(player, bindingResult, model);


        verify(playerRepository, times(1)).save(any(Player.class));


        assertEquals("redirect:/login", result);
    }

    @Test
    public void testRegisterPlayer_UsernameAlreadyExists() {

        Player existingPlayer = new Player();
        existingPlayer.setUsername("existingUser");


        when(playerRepository.findByUsername(anyString())).thenReturn(existingPlayer);


        String result = playerService.registerPlayer(existingPlayer, bindingResult, model);


        verify(playerRepository, never()).save(any(Player.class));




        assertEquals("register", result);
    }
}