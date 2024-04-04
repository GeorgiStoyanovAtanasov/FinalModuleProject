package com.example.demo;
import com.example.demo.Entities.ChosenMines.ChosenGoldMineEntity;
import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Repositories.ChosenGoldMineEntityRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Services.GoldMineService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class GoldMineServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ChosenGoldMineEntityRepository chosenGoldMineEntityRepository;

    @InjectMocks
    private GoldMineService goldMineService;

    @Test
    void testMineGoldSuccess() {
        // Arrange
        GoldMine goldMine = new GoldMine();
        goldMine.setValue(10);

        Player player = new Player();
        player.setGold(new Gold());

        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        // Create a ChosenGoldMineEntity with the provided GoldMine
        ChosenGoldMineEntity chosenGoldMineEntity = new ChosenGoldMineEntity();
        chosenGoldMineEntity.setCurrentGoldMine(goldMine);

        when(playerRepository.findByUsername("username")).thenReturn(player);
        when(chosenGoldMineEntityRepository.findById(1L)).thenReturn(Optional.of(chosenGoldMineEntity));

        // Act
        String result = goldMineService.mineGold();

        // Assert
        assertEquals("redirect:/gold/mine/success", result);
        assertEquals(10, player.getGold().getAmount());
    }
}
