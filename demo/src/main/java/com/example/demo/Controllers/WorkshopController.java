package com.example.demo.Controllers;

import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Entities.Workshops.CavalryWorkshop;
import com.example.demo.Entities.Workshops.SwordsmanWorkshop;
import com.example.demo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WorkshopController {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    ArcherWorkshopRepository archerWorkshopRepository;
    @Autowired
    SwordsmanWorkshopRepository swordsmanWorkshopRepository;
    @Autowired
    CavalryRepository cavalryRepository;
    @Autowired
    CavalryWorkshopRepository cavalryWorkshopRepository;
    @GetMapping("/buy/workshop")
    public String chooseWorkshopToBuyForm(){
        return "choose-workshop";
    }
    @PostMapping("/buy/workshop")
    public String buyWorkshop(@RequestParam("workshopType") String workshopType){
        if(workshopType.equals("archer")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Player player = playerRepository.findByUsername(username);
            int playerGold = player.getGold().getAmount();
            int price = 200;
            if(playerGold > price){
                player.getGold().setAmount(playerGold - price);
                archerWorkshopRepository.save(new ArcherWorkshop(player));
            }
            return "redirect:/home";
            //return "buy-archer-workshop";
        }
        else if(workshopType.equals("swordsman")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Player player = playerRepository.findByUsername(username);
            int playerSilver = player.getSilver().getAmount();
            int price = 500;
            if(playerSilver > price){
                player.getSilver().setAmount(playerSilver - price);
                swordsmanWorkshopRepository.save(new SwordsmanWorkshop(player));
            }
            return "redirect:/home";
            //return "buy-swordsman-workshop";
        } else if(workshopType.equals("cavalry")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Player player = playerRepository.findByUsername(username);
            int playerCrystal = player.getCrystal().getAmount();
            int price = 50;
            if(playerCrystal > price){
                player.getCrystal().setAmount(playerCrystal - price);
                cavalryWorkshopRepository.save(new CavalryWorkshop(player));
            }
            return "redirect:/home";
            //return "buy-swordsman-workshop";
        }
        return "choose-workshop";
    }
}
