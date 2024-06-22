package com.example.demo;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Materials.Silver;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Entities.Workshops.SwordsmanWorkshop;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Repositories.SwordsmanRepository;
import com.example.demo.Repositories.SwordsmanWorkshopRepository;
import com.example.demo.Services.SwordsmanWorkshopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class SwordsmanWorkshopServiceTests {
    @Mock
    private SwordsmanRepository swordsmanRepository;
    @Mock
    private SwordsmanWorkshopRepository swordsmanWorkshopRepository;

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private Model model;
    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private SwordsmanWorkshopService swordsmanWorkshopService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCanPlayerGetSwordsman_WhenLastAccessDateIsNull() {

        SwordsmanWorkshop swordsmanWorkshop = new SwordsmanWorkshop();
        swordsmanWorkshop.setLastAccessDate(null);


        when(swordsmanWorkshopRepository.save(swordsmanWorkshop)).thenReturn(swordsmanWorkshop);


        boolean result = swordsmanWorkshopService.canPlayerGetSwordsman(swordsmanWorkshop);


        assertTrue(result);


        verify(swordsmanWorkshopRepository, times(1)).save(swordsmanWorkshop);
    }

    @Test
    public void testCanPlayerGetSwordsman_WhenLastAccessDateIsToday() {

        LocalDate currentDate = LocalDate.now();
        SwordsmanWorkshop swordsmanWorkshop = new SwordsmanWorkshop();
        swordsmanWorkshop.setLastAccessDate(currentDate);


        boolean result = swordsmanWorkshopService.canPlayerGetSwordsman(swordsmanWorkshop);


        assertFalse(result);


        verify(swordsmanWorkshopRepository, never()).save(swordsmanWorkshop);
    }
    @Test
    public void testBuySwordsman_WithNullArcherWorkshopId() {
        String expectedRedirectUrl = "redirect:/add/swordsman";


        String result = swordsmanWorkshopService.buySwordsman(null, model, redirectAttributes);


        verify(redirectAttributes).addFlashAttribute("null", "no workshop selected.");

        assertEquals(expectedRedirectUrl, result);
    }

    @Test
    public void testBuySwordsman_WithNonExistentSwordsmanWorkshop() {
        Long swordsmanWorkshopId = 1L;
        String expectedRedirectUrl = "redirect:/add/swordsman";


        when(swordsmanWorkshopRepository.findById(swordsmanWorkshopId)).thenReturn(Optional.empty());


        String result = swordsmanWorkshopService.buySwordsman(swordsmanWorkshopId, model, redirectAttributes);


        assertEquals(expectedRedirectUrl, result);
    }
    @Test
    public void testBuySwordsman_WithValidInput() {
        Long swordsmanWorkshopId = 1L;
        String expectedRedirectUrl = "redirect:/home";


        SwordsmanWorkshop swordsmanWorkshop = mock(SwordsmanWorkshop.class);
        swordsmanWorkshop.setLastAccessDate(null);


        when(swordsmanWorkshopRepository.findById(swordsmanWorkshopId)).thenReturn(Optional.of(swordsmanWorkshop));


        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        Player player = new Player();
        player.setSwordsmen(new ArrayList<Swordsman>());
        player.setSilver(new Silver(20, player));


        when(playerRepository.findByUsername("testUser")).thenReturn(player);


        when(swordsmanWorkshop.getLastAccessDate()).thenReturn(null);

        String result = swordsmanWorkshopService.buySwordsman(swordsmanWorkshopId, model, redirectAttributes);




        verify(redirectAttributes, never()).addFlashAttribute(anyString(), anyString());
        assertEquals(expectedRedirectUrl, result);


        verify(swordsmanRepository).save(any(Swordsman.class));


        assertEquals(0, player.getSilver().getAmount());


        verify(playerRepository).findByUsername("testUser");
    }
}
