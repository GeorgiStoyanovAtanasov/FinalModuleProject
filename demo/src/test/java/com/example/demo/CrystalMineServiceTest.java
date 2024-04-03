package com.example.demo;

import com.example.demo.Entities.ChosenMines.ChosenCrystalMineEntity;
import com.example.demo.Entities.Mines.CrystalMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.ChosenCrystalMineEntityRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Services.CrystalMineService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.springframework.security.core.Authentication;

public class CrystalMineServiceTest {
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private ChosenCrystalMineEntityRepository chosenCrystalMineEntityRepository;

    @InjectMocks
    private CrystalMineService crystalMineService;

    @Test
    public void testMineCrystalSuccess() {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
        Player player = new Player();
        ChosenCrystalMineEntity chosenCrystalMineEntity = new ChosenCrystalMineEntity();
        CrystalMine currentCrystalMine = new CrystalMine();
        chosenCrystalMineEntity.setCurrentCrystalMine(currentCrystalMine);

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("testuser");
        when(playerRepository.findByUsername(anyString())).thenReturn(new Player());
        when(chosenCrystalMineEntityRepository.findById(1L)).thenReturn(Optional.of(chosenCrystalMineEntity));

        String result = crystalMineService.mineCrystal();

        verify(playerRepository, times(1)).save(player);
        assertEquals("redirect:/crystal/mine/success", result);
        assertEquals("redirect:/crystal/mine/success", result);
    }
    }
