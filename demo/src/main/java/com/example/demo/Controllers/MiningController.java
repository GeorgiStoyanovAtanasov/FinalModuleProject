package com.example.demo.Controllers;

import com.example.demo.Entities.ChosenMines.ChosenCrystalMineEntity;
import com.example.demo.Entities.ChosenMines.ChosenGoldMineEntity;
import com.example.demo.Entities.ChosenMines.ChosenSilverMineEntity;
import com.example.demo.Entities.Mines.CrystalMine;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Mines.SilverMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.*;
import com.example.demo.Services.CrystalMineService;
import com.example.demo.Services.GoldMineService;
import com.example.demo.Services.SilverMineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class MiningController {
    @Autowired
    GoldMineRepository goldMineRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ChosenGoldMineEntityRepository chosenGoldMineEntityRepository;
    @Autowired
    ChosenSilverMineRepository chosenSilverMineRepository;
    @Autowired
    ChosenCrystalMineEntityRepository chosenCrystalMineEntityRepository;
    @Autowired
    CrystalMineService crystalMineService;
    @Autowired
    GoldMineService goldMineService;
    @Autowired
    SilverMineService silverMineService;
    @GetMapping("/mine")
    public String mineChoice(Model model) {
        return "mine-choice";
    }
    @GetMapping("/mine/gold")
    public String mineGod() {
        return goldMineService.mineGold();
    }
    @GetMapping("/gold/mine/success")
    public String getGoldSuccessMessage(){
        return "gold-success-message";
    }
    @GetMapping("/admin/selectGoldMine")
    public String selectGoldMine() {
        GoldMine selectedGoldMine = goldMineRepository.findById(2L).orElse(null);
        if (selectedGoldMine != null) {
            ChosenGoldMineEntity chosenGoldMineEntity = chosenGoldMineEntityRepository.findById(1L).orElse(null);
            if (chosenGoldMineEntity != null) {
                chosenGoldMineEntity.setCurrentGoldMine(selectedGoldMine);
                chosenGoldMineEntityRepository.save(chosenGoldMineEntity);
            }
        }
        return "redirect:/home";
    }
    @GetMapping("/mine/silver")
    public String mineSilver() {
        return silverMineService.mineSilver();
    }
    @GetMapping("/silver/mine/success")
    public String getSilverSuccessMessage(){
        return "silver-success-message";
    }
    @GetMapping("/mine/crystal")
    public String mineCrystal() {
        return crystalMineService.mineCrystal();
    }
    @GetMapping("/crystal/mine/success")
    public String getCrystalSuccessMessage(){
        return "crystal-success-message";
    }
}
