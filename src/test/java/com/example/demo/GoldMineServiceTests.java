package com.example.demo;

import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Player;
//import com.example.demo.Repositories.ChosenGoldMineEntityRepository;
import com.example.demo.Repositories.GoldMineRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Services.GoldMineService;
import com.example.demo.Services.LastAccessedGoldMineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class GoldMineServiceTests {
    @Mock
    RedirectAttributes redirectAttributes;
    @Mock
    GoldMineRepository goldMineRepository;
    @Mock
    PlayerRepository playerRepository;
    @Mock
    LastAccessedGoldMineService lastAccessedGoldMineService;
//    @Mock
//    ChosenGoldMineEntityRepository chosenGoldMineEntityRepository;
    @InjectMocks
    GoldMineService goldMineService;

    @Test
    public void testMineGold_Success() throws Exception {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testUser");


        Player player = new Player();
        player.setGold(new Gold(1000, player));




        GoldMine goldMine = new GoldMine(200);
        goldMine.setId(1L);
        when(goldMineRepository.findById(1L)).thenReturn(Optional.of(goldMine));
        when(playerRepository.findByUsername(anyString())).thenReturn(player);
        //when(chosenGoldMineEntityRepository.findById(anyLong())).thenReturn(Optional.empty());

        String viewName = goldMineService.mineGold(redirectAttributes);


        assertEquals(1200, player.getGold().getAmount());

        assertEquals("redirect:/gold/mine/success", viewName);
    }

    @Test
    public void testMineGold_TimeLimitExceeded() throws Exception {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testUser");


        Player player = new Player();
        HashMap<LocalDate, Long> testHashMap = new HashMap<>();
        testHashMap.put(LocalDate.now(), 1L);

        GoldMine goldMine = new GoldMine();
        Map<Player, LocalDate> lastAccessDates = new HashMap<>();
        lastAccessDates.put(player, LocalDate.now());
        goldMine.setLastAccessDates(lastAccessDates);
        player.setLastAccessedGoldMines(testHashMap);
        when(playerRepository.findByUsername(anyString())).thenReturn(player);


        when(goldMineService.getCurrentGoldMineForPlayer(any(Player.class))).thenReturn(goldMine);
        String viewName = goldMineService.mineGold(redirectAttributes);


        assertEquals("redirect:/mine", viewName);
        verify(redirectAttributes).addFlashAttribute("timeLimitForGold", "you can't mine gold for today anymore, come back tomorrow and see what exciting gold mine is awaiting you.");
    }
}
