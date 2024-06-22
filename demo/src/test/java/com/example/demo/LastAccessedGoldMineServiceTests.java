package com.example.demo;

import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.GoldMineRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Services.LastAccessedGoldMineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class LastAccessedGoldMineServiceTests {
    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private GoldMineRepository goldMineRepository;

    @InjectMocks
    private LastAccessedGoldMineService goldMineService;

    @Test
    public void testGetLastAccessedGoldMine_NoLastAccessedGoldMine() {

        Player player = new Player();
        player.setLastAccessedGoldMines(null);


        GoldMine result = goldMineService.getLastAccessedGoldMine(player);


        assertNull(result);
    }

    @Test
    public void testGetLastAccessedGoldMine_WithLastAccessedGoldMine() {

        Player player = new Player();
        Map<LocalDate, Long> lastAccessedGoldMines = new HashMap<>();
        lastAccessedGoldMines.put(LocalDate.now(), 1L);
        player.setLastAccessedGoldMines(lastAccessedGoldMines);


        GoldMine goldMine = new GoldMine();
        when(goldMineRepository.findById(1L)).thenReturn(Optional.of(goldMine));


        GoldMine result = goldMineService.getLastAccessedGoldMine(player);


        assertNotNull(result);
        assertEquals(goldMine, result);
    }

    @Test
    public void testSetLastAccessedGoldMine() {

        Player player = new Player();


        GoldMine goldMine = new GoldMine();
        goldMine.setId(1L);


        goldMineService.setLastAccessedGoldMine(player, goldMine);


        assertNotNull(player.getLastAccessedGoldMines());
        assertTrue(player.getLastAccessedGoldMines().containsKey(LocalDate.now()));
        assertEquals(Long.valueOf(1L), player.getLastAccessedGoldMines().get(LocalDate.now()));


        verify(playerRepository, times(1)).save(player);
    }
}
