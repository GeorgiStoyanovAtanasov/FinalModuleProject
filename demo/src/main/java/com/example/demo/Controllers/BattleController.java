package com.example.demo.Controllers;

import com.example.demo.Entities.Attack;
import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Cavalry;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.*;
import com.example.demo.Services.AttackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BattleController {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    AttackRepository attackRepository;
    @Autowired
    AttackService attackService;
    @Autowired
    ArcherRepository archerRepository;
    @Autowired
    SwordsmanRepository swordsmanRepository;
    @Autowired
    CavalryRepository cavalryRepository;

    @GetMapping("/attack")
    public String attackFrom(Model model) {
        return attackService.attackFrom(model);
    }

    @PostMapping("/attack")
    public String attack(@RequestParam(name = "playerToAttack", required = false) Long playerToAttackId,
                         @RequestParam(name = "numberOfArchers", required = false, defaultValue = "0") int numberOfArchers,
                         @RequestParam(name = "numberOfSwordsmen", required = false, defaultValue = "0") int numberOfSwordsmen,
                         @RequestParam(name = "numberOfCavalries", required = false, defaultValue = "0") int numberOfCavalries) {
        return attackService.attack(playerToAttackId, numberOfArchers, numberOfSwordsmen, numberOfCavalries);
    }

    @GetMapping("/defend")
    public String defendForm(Model model) {
        return attackService.defendForm(model);
    }

    @PostMapping("/defend")
    public String defend(@RequestParam(name = "numberOfArchers", required = false, defaultValue = "0") int numberOfArchers,
                         @RequestParam(name = "numberOfSwordsmen", required = false, defaultValue = "0") int numberOfSwordsmen,
                         @RequestParam(name = "numberOfCavalries", required = false, defaultValue = "0") int numberOfCavalries, Model model) {
        return attackService.defend(numberOfArchers, numberOfSwordsmen, numberOfCavalries, model);
    }
}
