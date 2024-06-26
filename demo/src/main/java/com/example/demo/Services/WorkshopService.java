package com.example.demo.Services;

import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Entities.Workshops.CavalryWorkshop;
import com.example.demo.Entities.Workshops.SwordsmanWorkshop;
import com.example.demo.Repositories.ArcherWorkshopRepository;
import com.example.demo.Repositories.CavalryWorkshopRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Repositories.SwordsmanWorkshopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class WorkshopService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    ArcherWorkshopRepository archerWorkshopRepository;

    @Autowired
    SwordsmanWorkshopRepository swordsmanWorkshopRepository;

    @Autowired
    CavalryWorkshopRepository cavalryWorkshopRepository;

    public String buyArcherWorkshop(Player player, RedirectAttributes redirectAttributes) {
        int playerGold = player.getGold().getAmount();
        int price = 200;
        if (playerGold >= price) {
            player.getGold().setAmount(playerGold - price);
            archerWorkshopRepository.save(new ArcherWorkshop(player));
            return "redirect:/home";
        }
        redirectAttributes.addFlashAttribute("notEnoughGold", "not enough gold to buy an archer workshop.");
        return "redirect:/buy/workshop";
    }

    public String buySwordsmanWorkshop(Player player, RedirectAttributes redirectAttributes) {
        int playerSilver = player.getSilver().getAmount();
        int price = 500;
        if (playerSilver >= price) {
            player.getSilver().setAmount(playerSilver - price);
            swordsmanWorkshopRepository.save(new SwordsmanWorkshop(player));
            return "redirect:/home";
        }
        redirectAttributes.addFlashAttribute("notEnoughSilver", "not enough silver to buy a swordsman workshop.");
        return "redirect:/buy/workshop";
    }

    public String buyCavalryWorkshop(Player player, RedirectAttributes redirectAttributes) {
        int playerCrystal = player.getCrystal().getAmount();
        int price = 50;
        if (playerCrystal >= price) {
            player.getCrystal().setAmount(playerCrystal - price);
            cavalryWorkshopRepository.save(new CavalryWorkshop(player));
            return "redirect:/home";
        }
        redirectAttributes.addFlashAttribute("notEnoughCrystal", "not enough crystal to buy a cavalry workshop.");
        return "redirect:/buy/workshop";
    }
}
