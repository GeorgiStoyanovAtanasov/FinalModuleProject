package com.example.demo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDate;

public class CavalryWorkshopServiceTest {

    @Mock
    CavalryWorkshopRepository cavalryWorkshopRepository;

    @Mock
    PlayerRepository playerRepository;

    @Mock
    CavalryRepository cavalryRepository;

    @InjectMocks
    CavalryWorkshopService cavalryWorkshopService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBuyCavalrySuccess() {
        Long cavalryWorkshopId = 1L;
        CavalryWorkshop cavalryWorkshop = new CavalryWorkshop();
        cavalryWorkshop.setId(cavalryWorkshopId);
        Player player = new Player();
        player.setUsername("testUser");
        player.setCrystal(new Crystal(10));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(cavalryWorkshopRepository.findById(cavalryWorkshopId)).thenReturn(Optional.of(cavalryWorkshop));
        when(playerRepository.findByUsername("testUser")).thenReturn(player);

        String result = cavalryWorkshopService.buyCavalry(cavalryWorkshopId, new Model() {
            @Override
            public Model addAttribute(String attributeName, Object attributeValue) {
                return null;
            }

            @Override
            public Model addAttribute(Object attributeValue) {
                return null;
            }

            @Override
            public Model addAllAttributes(Collection<?> attributeValues) {
                return null;
            }

            @Override
            public Model addAllAttributes(Map<String, ?> attributes) {
                return null;
            }

            @Override
            public Model mergeAttributes(Map<String, ?> attributes) {
                return null;
            }

            @Override
            public boolean containsAttribute(String attributeName) {
                return false;
            }

            @Override
            public Object getAttribute(String attributeName) {
                return null;
            }

            @Override
            public Map<String, Object> asMap() {
                return null;
            }
        });

        assertEquals("redirect:/home", result);
        verify(cavalryRepository, times(1)).save(any(Cavalry.class));
        assertEquals(1, player.getCavalries().size());
        assertEquals(5, player.getCrystal().getAmount());
    }



}