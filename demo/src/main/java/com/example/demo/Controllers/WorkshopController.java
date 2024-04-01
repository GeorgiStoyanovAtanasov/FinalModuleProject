package com.example.demo.Controllers;

import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Repositories.ArcherWorkshopRepository;
import com.example.demo.Repositories.PlayerRepository;
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
            }
            archerWorkshopRepository.save(new ArcherWorkshop(player));
            return "buy-archer-workshop";
        }
        else if(workshopType.equals("swordsman")){
            return "buy-swordsman-workshop";
        }
        return "choose-workshop";
    }
}
