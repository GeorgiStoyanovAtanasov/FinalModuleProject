package com.example.demo;

import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Materials.Silver;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Mines.SilverMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.ChosenGoldMineEntityRepository;
import com.example.demo.Repositories.ChosenSilverMineRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Repositories.SilverMineRepository;
import com.example.demo.Services.GoldMineService;
import com.example.demo.Services.LastAccessedGoldMineService;
import com.example.demo.Services.LastAccessedSilverMineService;
import com.example.demo.Services.SilverMineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
public class SilverMineServiceTests {
    @Mock
    RedirectAttributes redirectAttributes;
    @Mock
    SilverMineRepository silverMineRepository;
    @Mock
    PlayerRepository playerRepository;
    @Mock
    LastAccessedSilverMineService lastAccessedSilverMineService;
    @Mock
    ChosenSilverMineRepository chosenSilverMineRepository;
    @InjectMocks
    SilverMineService silverMineService;

    @Test
    public void testMineSilver_Success() throws Exception {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testUser");


        Player player = new Player();
        player.setSilver(new Silver(1000, player));




        SilverMine silverMine = new SilverMine(200);
        silverMine.setId(1L);
        when(silverMineRepository.findById(1L)).thenReturn(Optional.of(silverMine));
        when(playerRepository.findByUsername(anyString())).thenReturn(player);
        when(chosenSilverMineRepository.findById(anyLong())).thenReturn(Optional.empty());

        String viewName = silverMineService.mineSilver(redirectAttributes);


        assertEquals(1200, player.getSilver().getAmount());

        assertEquals("redirect:/silver/mine/success", viewName);
    }

    @Test
    public void testMineSilver_TimeLimitExceeded() throws Exception {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testUser");


        Player player = new Player();
        HashMap<LocalDate, Long> testHashMap = new HashMap<>();
        testHashMap.put(LocalDate.now(), 1L);

        SilverMine silverMine = new SilverMine();
        Map<Player, LocalDate> lastAccessDates = new HashMap<>();
        lastAccessDates.put(player, LocalDate.now());
        silverMine.setLastAccessDates(lastAccessDates);
        player.setLastAccessedSilverMines(testHashMap);
        when(playerRepository.findByUsername(anyString())).thenReturn(player);


        when(silverMineService.getCurrentSilverMineForPlayer(any(Player.class))).thenReturn(silverMine);
        String viewName = silverMineService.mineSilver(redirectAttributes);


        assertEquals("redirect:/mine", viewName);
        verify(redirectAttributes).addFlashAttribute("timeLimitForSilver", "you can't mine silver for today anymore, come back tomorrow and see what exciting silver mine is awaiting you.");
    }
}
