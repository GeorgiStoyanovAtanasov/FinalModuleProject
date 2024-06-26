package com.example.demo.Controllers;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Entities.Workshops.CavalryWorkshop;
import com.example.demo.Entities.Workshops.SwordsmanWorkshop;
import com.example.demo.Repositories.*;
import com.example.demo.Services.ArcherWorkshopService;
import com.example.demo.Services.CavalryWorkshopService;
import com.example.demo.Services.SwordsmanWorkshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @Autowired
    CavalryWorkshopRepository cavalryWorkshopRepository;
    @Autowired
    CavalryWorkshopService cavalryWorkshopService;

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
    public String buyArcher(@RequestParam(name = "archerWorkshop", required = false) Long archerWorkshopId, Model model, RedirectAttributes redirectAttributes) {
        return archerWorkshopService.buyArcher(archerWorkshopId, model, redirectAttributes);
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
    public String buySwordsman(@RequestParam(name = "swordsmanWorkshop", required = false) Long swordsmanWorkshopId, Model model,  RedirectAttributes redirectAttributes) {
        return swordsmanWorkshopService.buySwordsman(swordsmanWorkshopId, model, redirectAttributes);
    }
    @GetMapping("/add/cavalry")
    public String chooseCavalryWorkshopForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        List<CavalryWorkshop> cavalryWorkshopsToSelectFrom = cavalryWorkshopRepository.findAllByPlayer(player);
        model.addAttribute("cavalryWorkshops", cavalryWorkshopsToSelectFrom);
        return "choose-cavalryWorkshop-to-add-cavalry";
    }
    @PostMapping("/add/cavalry")
    public String buyCavalry(@RequestParam(name = "cavalryWorkshop", required = false) Long cavalryWorkshopId, Model model, RedirectAttributes redirectAttributes) {
        return cavalryWorkshopService.buyCavalry(cavalryWorkshopId, model, redirectAttributes);
    }
}
