package com.example.demo.Controllers;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Repositories.ArcherRepository;
import com.example.demo.Repositories.ArcherWorkshopRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Services.ArcherWorkshopService;
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
    public String addArcher(@RequestParam("archerWorkshop") Long archerWorkshopId, Model model) {
        ArcherWorkshop archerWorkshop = archerWorkshopRepository.findById(archerWorkshopId).orElse(null);
        if (archerWorkshop == null) {
            return "redirect:/add/archer";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        boolean doesPlayerGetArcher = archerWorkshopService.addArcher(player, archerWorkshop);
        model.addAttribute("archers", player.getArchers());
        if (!doesPlayerGetArcher) {
            return "redirect:/add/archer";
        }
        return "add-archer-success";
    }

}
