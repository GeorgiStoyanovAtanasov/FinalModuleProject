package com.example.demo;

import com.example.demo.Entities.Mines.CrystalMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.CrystalMineRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Services.LastAccessedCrystalMineService;
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
public class LastAccessedCrystalMineServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private CrystalMineRepository crystalMineRepository;

    @InjectMocks
    private LastAccessedCrystalMineService crystalMineService;

    @Test
    public void testGetLastAccessedCrystalMine_NoLastAccessedCrystalMine() {

        Player player = new Player();
        player.setLastAccessedCrystalMines(null);


        CrystalMine result = crystalMineService.getLastAccessedCrystalMine(player);


        assertNull(result);
    }

    @Test
    public void testGetLastAccessedCrystalMine_WithLastAccessedCrystalMine() {

        Player player = new Player();
        Map<LocalDate, Long> lastAccessedCrystalMines = new HashMap<>();
        lastAccessedCrystalMines.put(LocalDate.now(), 1L);
        player.setLastAccessedCrystalMines(lastAccessedCrystalMines);


        CrystalMine crystalMine = new CrystalMine();
        when(crystalMineRepository.findById(1L)).thenReturn(Optional.of(crystalMine));


        CrystalMine result = crystalMineService.getLastAccessedCrystalMine(player);


        assertNotNull(result);
        assertEquals(crystalMine, result);
    }

    @Test
    public void testSetLastAccessedCrystalMine() {

        Player player = new Player();


        CrystalMine crystalMine = new CrystalMine();
        crystalMine.setId(1L);


        crystalMineService.setLastAccessedCrystalMine(player, crystalMine);


        assertNotNull(player.getLastAccessedCrystalMines());
        assertTrue(player.getLastAccessedCrystalMines().containsKey(LocalDate.now()));
        assertEquals(Long.valueOf(1L), player.getLastAccessedCrystalMines().get(LocalDate.now()));


        verify(playerRepository, times(1)).save(player);
    }
}
