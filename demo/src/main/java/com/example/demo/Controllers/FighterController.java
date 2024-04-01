package com.example.demo.Controllers;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Entities.Workshops.SwordsmanWorkshop;
import com.example.demo.Repositories.*;
import com.example.demo.Services.ArcherWorkshopService;
import com.example.demo.Services.SwordsmanWorkshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FighterController {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    ArcherWorkshopRepository archerWorkshopRepository;
    @Autowired
    ArcherRepository archerRepository;
    @Autowired
    ArcherWorkshopService archerWorkshopService;
    @Autowired
    SwordsmanRepository swordsmanRepository;
    @Autowired
    SwordsmanWorkshopRepository swordsmanWorkshopRepository;
    @Autowired
    SwordsmanWorkshopService swordsmanWorkshopService;

    @GetMapping("/add/archer")
    public String chooseArcherWorkshopForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        List<ArcherWorkshop> archerWorkshopsToSelectFrom = archerWorkshopRepository.findAllByPlayer(player);
        model.addAttribute("archerWorkshops", archerWorkshopsToSelectFrom);
        return "choose-archerWorkshop-to-add-archer";
    }

    @PostMapping("/add/archer")
    public String buyArcher(@RequestParam("archerWorkshop") Long archerWorkshopId, Model model) {
        ArcherWorkshop archerWorkshop = archerWorkshopRepository.findById(archerWorkshopId).orElse(null);
        if (archerWorkshop == null) {
            return "redirect:/add/archer";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        boolean doesPlayerGetArcher = archerWorkshopService.addArcher(player, archerWorkshop);
        if (!doesPlayerGetArcher) {
            return "redirect:/add/archer";
        }
        model.addAttribute("archers", player.getArchers());
        return "redirect:/home";
        //return "add-archer-success";
    }
    @GetMapping("/add/swordsman")
    public String chooseSwordsmanWorkshopForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        List<SwordsmanWorkshop> swordsmanWorkshopsToSelectFrom = swordsmanWorkshopRepository.findAllByPlayer(player);
        model.addAttribute("swordsmanWorkshops", swordsmanWorkshopsToSelectFrom);
        return "choose-swordsmanWorkshop-to-add-swordsman";
    }
    @PostMapping("/add/swordsman")
    public String buySwordsman(@RequestParam("swordsmanWorkshop") Long swordsmanWorkshopId, Model model) {
        SwordsmanWorkshop swordsmanWorkshop = swordsmanWorkshopRepository.findById(swordsmanWorkshopId).orElse(null);
        if (swordsmanWorkshop == null) {
            return "redirect:/add/swordsman";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        boolean doesPlayerGetSwordsman = swordsmanWorkshopService.addSwordsman(player, swordsmanWorkshop);
        if (!doesPlayerGetSwordsman) {
            return "redirect:/add/swordsman";
        }
        model.addAttribute("swordsmen", player.getSwordsmen());
        return "redirect:/home";
        //return "add-swordsman-success";
    }
}
