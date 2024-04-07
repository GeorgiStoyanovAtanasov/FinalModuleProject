package com.example.demo;

import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.SwordsmanWorkshop;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Repositories.SwordsmanRepository;
import com.example.demo.Repositories.SwordsmanWorkshopRepository;
import com.example.demo.Services.SwordsmanWorkshopService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class SwordsmanWorkshopServiceTest {

    @Mock
    private SwordsmanWorkshopRepository swordsmanWorkshopRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private SwordsmanRepository swordsmanRepository;

    @InjectMocks
    private SwordsmanWorkshopService swordsmanWorkshopService;

    @Test
    void testBuySwordsmanSuccess() {
        MockitoAnnotations.openMocks(this);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = new TestingAuthenticationToken("username", "password");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        PlayerRepository playerRepository = mock(PlayerRepository.class);
        SwordsmanRepository swordsmanRepository = mock(SwordsmanRepository.class);

        Player player = new Player();
        player.setUsername("username");
        player.setPassword("password");

        when(playerRepository.findByUsername(anyString())).thenReturn(player);

        SwordsmanWorkshopService swordsmanWorkshopService = new SwordsmanWorkshopService(swordsmanWorkshopRepository, playerRepository, swordsmanRepository);
        SwordsmanWorkshop swordsmanWorkshop = new SwordsmanWorkshop();
        swordsmanWorkshop.setId(1L);
        swordsmanWorkshopService.setPlayerRepository(playerRepository);
        swordsmanWorkshopService.setSwordsmanRepository(swordsmanRepository);



        when(swordsmanWorkshopRepository.findById(anyLong())).thenReturn(Optional.of(swordsmanWorkshop));

        String result = swordsmanWorkshopService.buySwordsman(1L, mock(Model.class));

        verify(playerRepository, times(1)).findByUsername("testUser");
        verify(swordsmanWorkshopRepository, times(1)).findById(1L);
        verify(swordsmanRepository, times(1)).save(any(Swordsman.class));
        verify(playerRepository, times(1)).save(player);

        assertEquals("redirect:/home", result);
    }

    @Test
    void testBuySwordsmanFailureSwordsmanWorkshopNotFound() {
        MockitoAnnotations.openMocks(this);

        when(swordsmanWorkshopRepository.findById(anyLong())).thenReturn(Optional.empty());

        String result = swordsmanWorkshopService.buySwordsman(1L, mock(Model.class));

        verify(swordsmanWorkshopRepository, times(1)).findById(1L);

        assertEquals("redirect:/add/swordsman", result);
    }
}
