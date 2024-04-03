package com.example.demo;

import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Repositories.ArcherRepository;
import com.example.demo.Repositories.ArcherWorkshopRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Services.ArcherWorkshopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ArcherWorkshopServiceTest {
    @Mock
    private ArcherRepository archerRepository;
    @Mock
    private ArcherWorkshopRepository archerWorkshopRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private ArcherWorkshopService archerWorkshopService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBuyArcherSuccess() {
        Long archerWorkshopId = 1L;
        ArcherWorkshop archerWorkshop = new ArcherWorkshop();
        archerWorkshop.setId(archerWorkshopId);
        Player player = new Player();
        player.setUsername("testUser");
        player.setArchers(new ArrayList<>());
        Gold gold = new Gold();
        gold.setAmount(20);
        player.setGold(gold);

        when(archerWorkshopRepository.findById(archerWorkshopId)).thenReturn(java.util.Optional.of(archerWorkshop));
        when(playerRepository.findByUsername("testUser")).thenReturn(player);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "password");
        securityContext.setAuthentication(authentication);

        when(archerWorkshopService.addArcher(player, archerWorkshop)).thenReturn(true);

        Model model = mock(Model.class);

        String result = archerWorkshopService.buyArcher(archerWorkshopId, model);

        assertEquals("redirect:/home", result);
        verify(model).addAttribute("archers", player.getArchers());
    }
    @Test
    public void testBuyArcherNotSuccess() {
        Long archerWorkshopId = 1L;
        ArcherWorkshop archerWorkshop = new ArcherWorkshop();
        archerWorkshop.setId(archerWorkshopId);
        Player player = new Player();
        player.setUsername("testUser");
        player.setArchers(new ArrayList<>());
        Gold gold = new Gold();
        gold.setAmount(20);
        player.setGold(gold);

        when(archerWorkshopRepository.findById(archerWorkshopId)).thenReturn(java.util.Optional.of(archerWorkshop));
        when(playerRepository.findByUsername("testUser")).thenReturn(player);
        when(archerWorkshopService.addArcher(player, archerWorkshop)).thenReturn(false);

        Model model = mock(Model.class);

        String result = archerWorkshopService.buyArcher(archerWorkshopId, model);

        assertEquals("redirect:/add/archer", result);
        verify(model, never()).addAttribute(eq("archers"), any());
    }
    @Test
    public void testBuyArcherArcherWorkshopNotFound() {
        Long archerWorkshopId = 1L;

        when(archerWorkshopRepository.findById(archerWorkshopId)).thenReturn(java.util.Optional.empty());

        Model model = mock(Model.class);

        String result = archerWorkshopService.buyArcher(archerWorkshopId, model);

        assertEquals("redirect:/add/archer", result);
    }
    @Test
    public void testBuyArcherArcherWorkshopExist() {
        Long archerWorkshopId = 1L;
        ArcherWorkshop archerWorkshop = new ArcherWorkshop();
        archerWorkshop.setId(archerWorkshopId);
        Player player = new Player();
        player.setUsername("testUser");

        when(archerWorkshopRepository.findById(archerWorkshopId)).thenReturn(java.util.Optional.of(archerWorkshop));
        when(playerRepository.findByUsername("testUser")).thenReturn(player);
        when(archerWorkshopService.addArcher(player, archerWorkshop)).thenReturn(true);

        Model model = mock(Model.class);

        String result = archerWorkshopService.buyArcher(archerWorkshopId, model);

        assertEquals("redirect:/home", result);
        verify(model).addAttribute("archers", player.getArchers());
    }
    @Test
    public void testBuyArcherPlayerNotFound() {
        Long archerWorkshopId = 1L;
        ArcherWorkshop archerWorkshop = new ArcherWorkshop();
        archerWorkshop.setId(archerWorkshopId);

        when(archerWorkshopRepository.findById(archerWorkshopId)).thenReturn(java.util.Optional.of(archerWorkshop));
        when(playerRepository.findByUsername(anyString())).thenReturn(null);

        Model model = mock(Model.class);

        String result = archerWorkshopService.buyArcher(archerWorkshopId, model);

        assertEquals("redirect:/add/archer", result);
    }
    @Test
    public void testBuyArcherPlayerExists() {
        Long archerWorkshopId = 1L;
        ArcherWorkshop archerWorkshop = new ArcherWorkshop();
        archerWorkshop.setId(archerWorkshopId);
        Player player = new Player();
        player.setUsername("testUser");

        when(archerWorkshopRepository.findById(archerWorkshopId)).thenReturn(java.util.Optional.of(archerWorkshop));
        when(playerRepository.findByUsername("testUser")).thenReturn(player);
        when(archerWorkshopService.addArcher(player, archerWorkshop)).thenReturn(true);

        Model model = mock(Model.class);

        String result = archerWorkshopService.buyArcher(archerWorkshopId, model);

        assertEquals("redirect:/home", result);
        verify(model).addAttribute("archers", player.getArchers());
    }
    @Test
    public void testBuyArcherPlayerDoesNotGetArcher() {
        Long archerWorkshopId = 1L;
        ArcherWorkshop archerWorkshop = new ArcherWorkshop();
        archerWorkshop.setId(archerWorkshopId);
        Player player = new Player();
        player.setUsername("testUser");

        when(archerWorkshopRepository.findById(archerWorkshopId)).thenReturn(java.util.Optional.of(archerWorkshop));
        when(playerRepository.findByUsername("testUser")).thenReturn(player);
        when(archerWorkshopService.addArcher(player, archerWorkshop)).thenReturn(false);

        Model model = mock(Model.class);

        String result = archerWorkshopService.buyArcher(archerWorkshopId, model);

        assertEquals("redirect:/add/archer", result);
    }
    @Test
    public void testBuyArcherPlayerGetArcher() {
        Long archerWorkshopId = 1L;
        ArcherWorkshop archerWorkshop = new ArcherWorkshop();
        archerWorkshop.setId(archerWorkshopId);
        Player player = new Player();
        player.setUsername("testUser");

        when(archerWorkshopRepository.findById(archerWorkshopId)).thenReturn(java.util.Optional.of(archerWorkshop));
        when(playerRepository.findByUsername("testUser")).thenReturn(player);
        when(archerWorkshopService.addArcher(player, archerWorkshop)).thenReturn(true);

        Model model = mock(Model.class);

        String result = archerWorkshopService.buyArcher(archerWorkshopId, model);

        assertEquals("redirect:/home", result);
        verify(model).addAttribute("archers", player.getArchers());
    }
}
