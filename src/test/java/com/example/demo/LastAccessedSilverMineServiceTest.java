package com.example.demo;

import com.example.demo.Entities.Mines.SilverMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Repositories.SilverMineRepository;
import com.example.demo.Services.LastAccessedSilverMineService;
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
public class LastAccessedSilverMineServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private SilverMineRepository silverMineRepository;

    @InjectMocks
    private LastAccessedSilverMineService silverMineService;

    @Test
    public void testGetLastAccessedSilverMine_NoLastAccessedSilverMine() {

        Player player = new Player();
        player.setLastAccessedSilverMines(null);


        SilverMine result = silverMineService.getLastAccessedSilverMine(player);


        assertNull(result);
    }

    @Test
    public void testGetLastAccessedSilverMine_WithLastAccessedSilverMine() {

        Player player = new Player();
        Map<LocalDate, Long> lastAccessedSilverMines = new HashMap<>();
        lastAccessedSilverMines.put(LocalDate.now(), 1L);
        player.setLastAccessedSilverMines(lastAccessedSilverMines);


        SilverMine silverMine = new SilverMine();
        when(silverMineRepository.findById(1L)).thenReturn(Optional.of(silverMine));


        SilverMine result = silverMineService.getLastAccessedSilverMine(player);


        assertNotNull(result);
        assertEquals(silverMine, result);
    }

    @Test
    public void testSetLastAccessedSilverMine() {

        Player player = new Player();


        SilverMine silverMine = new SilverMine();
        silverMine.setId(1L);


        silverMineService.setLastAccessedSilverMine(player, silverMine);


        assertNotNull(player.getLastAccessedSilverMines());
        assertTrue(player.getLastAccessedSilverMines().containsKey(LocalDate.now()));
        assertEquals(Long.valueOf(1L), player.getLastAccessedSilverMines().get(LocalDate.now()));


        verify(playerRepository, times(1)).save(player);
    }
}
