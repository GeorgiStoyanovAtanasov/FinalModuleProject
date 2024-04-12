package com.example.demo;

import com.example.demo.Entities.Materials.Crystal;
import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Mines.CrystalMine;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.ChosenCrystalMineEntityRepository;
import com.example.demo.Repositories.CrystalMineRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Services.CrystalMineService;
import com.example.demo.Services.LastAccessedCrystalMineService;
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
public class CrystalMineServiceTests {
    @Mock
    RedirectAttributes redirectAttributes;
    @Mock
    CrystalMineRepository crystalMineRepository;
    @Mock
    PlayerRepository playerRepository;
    @Mock
    LastAccessedCrystalMineService lastAccessedCrystalMineService;
    @Mock
    ChosenCrystalMineEntityRepository chosenCrystalMineEntityRepository;
    @InjectMocks
    CrystalMineService crystalMineService;

    @Test
    public void testMineCrystal_Success() throws Exception {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testUser");


        Player player = new Player();
        player.setCrystal(new Crystal(1000, player));




        CrystalMine crystalMine = new CrystalMine(200);
        crystalMine.setId(1L);
        when(crystalMineRepository.findById(1L)).thenReturn(Optional.of(crystalMine));
        when(playerRepository.findByUsername(anyString())).thenReturn(player);
        when(chosenCrystalMineEntityRepository.findById(anyLong())).thenReturn(Optional.empty());

        String viewName = crystalMineService.mineCrystal(redirectAttributes);


        assertEquals(1200, player.getCrystal().getAmount());

        assertEquals("redirect:/crystal/mine/success", viewName);
    }

    @Test
    public void testMineCrystal_TimeLimitExceeded() throws Exception {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testUser");


        Player player = new Player();
        HashMap<LocalDate, Long> testHashMap = new HashMap<>();
        testHashMap.put(LocalDate.now(), 1L);

        CrystalMine crystalMine = new CrystalMine();
        Map<Player, LocalDate> lastAccessDates = new HashMap<>();
        lastAccessDates.put(player, LocalDate.now());
        crystalMine.setLastAccessDates(lastAccessDates);
        player.setLastAccessedCrystalMines(testHashMap);
        when(playerRepository.findByUsername(anyString())).thenReturn(player);


        when(crystalMineService.getCurrentCrystalMineForPlayer(any(Player.class))).thenReturn(crystalMine);
        String viewName = crystalMineService.mineCrystal(redirectAttributes);


        assertEquals("redirect:/mine", viewName);
        verify(redirectAttributes).addFlashAttribute("timeLimitForCrystal", "you can't mine crystal for today anymore, come back tomorrow and see what exciting crystal mine is awaiting you.");
    }
}
