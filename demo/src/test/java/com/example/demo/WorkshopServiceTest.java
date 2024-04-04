package com.example.demo;

import com.example.demo.Entities.*;
import com.example.demo.Entities.Materials.Crystal;
import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Materials.Silver;
import com.example.demo.Entities.Workshops.*;
import com.example.demo.Repositories.*;
import com.example.demo.Services.WorkshopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class WorkshopServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ArcherWorkshopRepository archerWorkshopRepository;

    @Mock
    private SwordsmanWorkshopRepository swordsmanWorkshopRepository;

    @Mock
    private CavalryWorkshopRepository cavalryWorkshopRepository;

    @InjectMocks
    private WorkshopService workshopService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuyArcherWorkshopSuccess() {
        Player player = new Player();
        player.setGold(new Gold());
        player.getGold().setAmount(300);

        workshopService.buyArcherWorkshop(player);

        verify(playerRepository, times(1)).save(player);
        verify(archerWorkshopRepository, times(1)).save(any(ArcherWorkshop.class));
    }

    @Test
    void testBuySwordsmanWorkshopSuccess() {
        Player player = new Player();
        player.setSilver(new Silver());
        player.getSilver().setAmount(600);

        workshopService.buySwordsmanWorkshop(player);

        verify(playerRepository, times(1)).save(player);
        verify(swordsmanWorkshopRepository, times(1)).save(any(SwordsmanWorkshop.class));
    }

    @Test
    void testBuyCavalryWorkshopSuccess() {
        Player player = new Player();
        player.setCrystal(new Crystal(300));
        player.getCrystal().setAmount(100);

        workshopService.buyCavalryWorkshop(player);

        verify(playerRepository, times(1)).save(player);
        verify(cavalryWorkshopRepository, times(1)).save(any(CavalryWorkshop.class));
    }
}

