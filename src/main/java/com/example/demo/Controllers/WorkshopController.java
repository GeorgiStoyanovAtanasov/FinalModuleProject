package com.example.demo.Controllers;

import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Entities.Workshops.CavalryWorkshop;
import com.example.demo.Entities.Workshops.SwordsmanWorkshop;
import com.example.demo.Repositories.*;
import com.example.demo.Services.WorkshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @Autowired
    WorkshopService workshopService;
    @GetMapping("/buy/workshop")
    public String chooseWorkshopToBuyForm(){
        return "choose-workshop";
    }
    @PostMapping("/buy/workshop")
    public String buyWorkshop(@RequestParam("workshopType") String workshopType, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        if (workshopType.equals("archer")) {
            return workshopService.buyArcherWorkshop(player, redirectAttributes);
        } else if (workshopType.equals("swordsman")) {
            return workshopService.buySwordsmanWorkshop(player, redirectAttributes);
        } else if (workshopType.equals("cavalry")) {
            return workshopService.buyCavalryWorkshop(player, redirectAttributes);
        }
        return "redirect:/buy/workshop";
    }
}
