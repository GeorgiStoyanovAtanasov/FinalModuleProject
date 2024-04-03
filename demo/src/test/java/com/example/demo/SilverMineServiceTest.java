package com.example.demo;

import com.example.demo.Entities.ChosenMines.ChosenSilverMineEntity;
import com.example.demo.Entities.Materials.Silver;
import com.example.demo.Entities.Mines.SilverMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.ChosenSilverMineRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Services.SilverMineService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SilverMineServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ChosenSilverMineRepository chosenSilverMineRepository;

    @InjectMocks
    private SilverMineService silverMineService;

    @Test
    void testMineSilverSuccess() {
        MockitoAnnotations.openMocks(this);

        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "testPassword");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Player player = new Player();
        player.setSilver(new Silver());
        player.setUsername("testUser");

        when(playerRepository.findByUsername(anyString())).thenReturn(player);

        ChosenSilverMineEntity chosenSilverMineEntity = new ChosenSilverMineEntity();
        SilverMine silverMine = new SilverMine();
        chosenSilverMineEntity.setCurrentSilverMine(silverMine);

        when(chosenSilverMineRepository.findById(1L)).thenReturn(Optional.of(chosenSilverMineEntity));

        String result = silverMineService.mineSilver();

        verify(playerRepository, times(1)).findByUsername("testUser");
        verify(chosenSilverMineRepository, times(1)).findById(1L);
        verify(playerRepository, times(1)).save(player);

        assertEquals("redirect:/silver/mine/success", result);
    }
}

