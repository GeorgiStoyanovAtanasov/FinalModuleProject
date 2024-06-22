package com.example.demo;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Repositories.ArcherRepository;
import com.example.demo.Repositories.ArcherWorkshopRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Services.ArcherWorkshopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ArcherWorkshopSeriveUnitTests {
    @Mock
    private ArcherRepository archerRepository;
    @Mock
    private ArcherWorkshopRepository archerWorkshopRepository;

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private Model model;
    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private ArcherWorkshopService archerWorkshopService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCanPlayerGetArcher_WhenLastAccessDateIsNull() {
        ArcherWorkshop archerWorkshop = new ArcherWorkshop();
        archerWorkshop.setLastAccessDate(null);

        when(archerWorkshopRepository.save(archerWorkshop)).thenReturn(archerWorkshop);

        boolean result = archerWorkshopService.canPlayerGetArcher(archerWorkshop);

        assertTrue(result);

        // Verify that setLastAccessDate is called and archerWorkshopRepository.save is called
        verify(archerWorkshopRepository, times(1)).save(archerWorkshop);
    }

    @Test
    public void testCanPlayerGetArcher_WhenLastAccessDateIsToday() {
        LocalDate currentDate = LocalDate.now();
        ArcherWorkshop archerWorkshop = new ArcherWorkshop();
        archerWorkshop.setLastAccessDate(currentDate);

        boolean result = archerWorkshopService.canPlayerGetArcher(archerWorkshop);

        assertFalse(result);

        verify(archerWorkshopRepository, never()).save(archerWorkshop);
    }
    @Test
    public void testBuyArcher_WithNullArcherWorkshopId() {
        String expectedRedirectUrl = "redirect:/add/archer";


        String result = archerWorkshopService.buyArcher(null, model, redirectAttributes);


        verify(redirectAttributes).addFlashAttribute("nulll", "no workshop selected.");

        assertEquals(expectedRedirectUrl, result);
    }

    @Test
    public void testBuyArcher_WithNonExistentArcherWorkshop() {
        Long archerWorkshopId = 1L;
        String expectedRedirectUrl = "redirect:/add/archer";


        when(archerWorkshopRepository.findById(archerWorkshopId)).thenReturn(Optional.empty());


        String result = archerWorkshopService.buyArcher(archerWorkshopId, model, redirectAttributes);

        assertEquals(expectedRedirectUrl, result);
    }
    @Test
    public void testBuyArcher_WithValidInput() {
        Long archerWorkshopId = 1L;
        String expectedRedirectUrl = "redirect:/home";


        ArcherWorkshop archerWorkshop = mock(ArcherWorkshop.class);
        archerWorkshop.setLastAccessDate(null);


        when(archerWorkshopRepository.findById(archerWorkshopId)).thenReturn(Optional.of(archerWorkshop));


        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        Player player = new Player();
        player.setArchers(new ArrayList<Archer>());
        player.setGold(new Gold(10, player));


        when(playerRepository.findByUsername("testUser")).thenReturn(player);


        when(archerWorkshop.getLastAccessDate()).thenReturn(null);

        String result = archerWorkshopService.buyArcher(archerWorkshopId, model, redirectAttributes);


        verify(redirectAttributes, never()).addFlashAttribute(anyString(), anyString());
        assertEquals(expectedRedirectUrl, result);


        verify(archerRepository).save(any(Archer.class));


        assertEquals(0, player.getGold().getAmount());


        verify(playerRepository).findByUsername("testUser");
    }
}
