package com.example.demo;

import com.example.demo.Entities.Materials.Crystal;
import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Materials.Silver;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Entities.Workshops.CavalryWorkshop;
import com.example.demo.Entities.Workshops.SwordsmanWorkshop;
import com.example.demo.Repositories.ArcherWorkshopRepository;
import com.example.demo.Repositories.CavalryWorkshopRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Repositories.SwordsmanWorkshopRepository;
import com.example.demo.Services.WorkshopService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class WorkshopTests {
    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ArcherWorkshopRepository archerWorkshopRepository;

    @Mock
    private SwordsmanWorkshopRepository swordsmanWorkshopRepository;

    @Mock
    private CavalryWorkshopRepository cavalryWorkshopRepository;
    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private WorkshopService workshopService;
    @Test
    public void testBuyArcherWorkshop_EnoughGold() {

        Player player = new Player();
        player.setGold(new Gold(200, player));


        String result = workshopService.buyArcherWorkshop(player, redirectAttributes);


        assertEquals(0, player.getGold().getAmount());
        verify(archerWorkshopRepository, times(1)).save(any(ArcherWorkshop.class));


        assertEquals("redirect:/home", result);
    }

    @Test
    public void testBuyArcherWorkshop_NotEnoughGold() {

        Player player = new Player();
        player.setGold(new Gold(100, player));


        String result = workshopService.buyArcherWorkshop(player, redirectAttributes);


        assertEquals(100, player.getGold().getAmount());
        verify(archerWorkshopRepository, never()).save(any(ArcherWorkshop.class));


        verify(redirectAttributes).addFlashAttribute("notEnoughGold", "not enough gold to buy an archer workshop.");
        assertEquals("redirect:/buy/workshop", result);
    }
    @Test
    public void testBuySwordsmanWorkshop_EnoughSilver() throws Exception {

        Player player = new Player();
        player.setSilver(new Silver(500, player));


        String result = workshopService.buySwordsmanWorkshop(player, redirectAttributes);


        assertEquals(0, player.getSilver().getAmount());
        verify(swordsmanWorkshopRepository, times(1)).save(any(SwordsmanWorkshop.class));


        assertEquals("redirect:/home", result);
    }

    @Test
    public void testBuySwordsmanWorkshop_NotEnoughSilver() throws Exception {

        Player player = new Player();
        player.setSilver(new Silver(100, player));


        String result = workshopService.buySwordsmanWorkshop(player, redirectAttributes);


        assertEquals(100, player.getSilver().getAmount());
        verify(swordsmanWorkshopRepository, never()).save(any(SwordsmanWorkshop.class));


        verify(redirectAttributes).addFlashAttribute("notEnoughSilver", "not enough silver to buy a swordsman workshop.");
        assertEquals("redirect:/buy/workshop", result);
    }
    @Test
    public void testBuyCavalryWorkshop_EnoughCrystal() throws Exception {

        Player player = new Player();
        player.setCrystal(new Crystal(50, player));


        String result = workshopService.buyCavalryWorkshop(player, redirectAttributes);


        assertEquals(0, player.getCrystal().getAmount());
        verify(cavalryWorkshopRepository, times(1)).save(any(CavalryWorkshop.class));


        assertEquals("redirect:/home", result);
    }
    @Test
    public void testBuyCavalryWorkshop_NotEnoughCrystal() {

        Player player = new Player();
        player.setCrystal(new Crystal(25, player));


        String result = workshopService.buyCavalryWorkshop(player, redirectAttributes);


        assertEquals(25, player.getCrystal().getAmount());
        verify(cavalryWorkshopRepository, never()).save(any(CavalryWorkshop.class));


        verify(redirectAttributes).addFlashAttribute("notEnoughCrystal", "not enough crystal to buy a cavalry workshop.");
        assertEquals("redirect:/buy/workshop", result);
    }
}
