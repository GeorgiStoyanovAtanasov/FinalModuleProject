package com.example.demo;

import com.example.demo.Entities.Fighters.Cavalry;
import com.example.demo.Entities.Materials.Crystal;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.CavalryWorkshop;
import com.example.demo.Repositories.CavalryRepository;
import com.example.demo.Repositories.CavalryWorkshopRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Services.CavalryWorkshopService;
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
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CavalryWorkshopServiceTests {
    @Mock
    private CavalryRepository cavalryRepository;
    @Mock
    private CavalryWorkshopRepository cavalryWorkshopRepository;

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private Model model;
    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private CavalryWorkshopService cavalryWorkshopService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCanPlayerGetCavalry_WhenLastAccessDateIsNull() {

        CavalryWorkshop cavalryWorkshop = new CavalryWorkshop();
        cavalryWorkshop.setLastAccessDate(null);


        when(cavalryWorkshopRepository.save(cavalryWorkshop)).thenReturn(cavalryWorkshop);


        boolean result = cavalryWorkshopService.canPlayerGetCavalry(cavalryWorkshop);


        assertTrue(result);


        verify(cavalryWorkshopRepository, times(1)).save(cavalryWorkshop);
    }

    @Test
    public void testCanPlayerGetCavalry_WhenLastAccessDateIsToday() {

        LocalDate currentDate = LocalDate.now();
        CavalryWorkshop cavalryWorkshop = new CavalryWorkshop();
        cavalryWorkshop.setLastAccessDate(currentDate);


        boolean result = cavalryWorkshopService.canPlayerGetCavalry(cavalryWorkshop);


        assertFalse(result);


        verify(cavalryWorkshopRepository, never()).save(cavalryWorkshop);
    }
    @Test
    public void testBuyCavalry_WithNullCavalryWorkshopId() {
        String expectedRedirectUrl = "redirect:/add/cavalry";


        String result = cavalryWorkshopService.buyCavalry(null, model, redirectAttributes);


        verify(redirectAttributes).addFlashAttribute("null", "no workshop selected.");

        assertEquals(expectedRedirectUrl, result);
    }

    @Test
    public void testBuyCavalry_WithNonExistentCavalryWorkshop() {
        Long cavalryWorkshopId = 1L;
        String expectedRedirectUrl = "redirect:/add/cavalry";


        when(cavalryWorkshopRepository.findById(cavalryWorkshopId)).thenReturn(Optional.empty());


        String result = cavalryWorkshopService.buyCavalry(cavalryWorkshopId, model, redirectAttributes);


        assertEquals(expectedRedirectUrl, result);
    }
    @Test
    public void testBuyCavalry_WithValidInput() {
        Long cavalryWorkshopId = 1L;
        String expectedRedirectUrl = "redirect:/home";


        CavalryWorkshop cavalryWorkshop = mock(CavalryWorkshop.class);
        cavalryWorkshop.setLastAccessDate(null);


        when(cavalryWorkshopRepository.findById(cavalryWorkshopId)).thenReturn(Optional.of(cavalryWorkshop));


        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        Player player = new Player();
        player.setCavalries(new ArrayList<Cavalry>());
        player.setCrystal(new Crystal(5, player));


        when(playerRepository.findByUsername("testUser")).thenReturn(player);


        when(cavalryWorkshop.getLastAccessDate()).thenReturn(null);

        String result = cavalryWorkshopService.buyCavalry(cavalryWorkshopId, model, redirectAttributes);




        verify(redirectAttributes, never()).addFlashAttribute(anyString(), anyString());
        assertEquals(expectedRedirectUrl, result);


        verify(cavalryRepository).save(any(Cavalry.class));


        assertEquals(0, player.getCrystal().getAmount());


        verify(playerRepository).findByUsername("testUser");
    }
}
