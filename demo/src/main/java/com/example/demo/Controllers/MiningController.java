package com.example.demo.Controllers;

import com.example.demo.Entities.ChosenMines.ChosenGoldMineEntity;
import com.example.demo.Entities.ChosenMines.ChosenSilverMineEntity;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Mines.SilverMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.ChosenGoldMineEntityRepository;
import com.example.demo.Repositories.ChosenSilverMineRepository;
import com.example.demo.Repositories.GoldMineRepository;
import com.example.demo.Repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    @GetMapping("/mine")
    public String mineChoice(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        Player player = playerRepository.findByUsername(username);
//        GoldMine goldMine = goldMineRepository.findById(1L).orElse(null);
//        if (goldMine != null && goldMine.getMaterials(player)) {
//            playerRepository.save(player);
//            model.addAttribute("gold", player.getGold().getAmount());
//            return "redirect:/home";
//        } else {
//            return "redirect:/login";
//        }
        return "mine-choice";
    }
    @GetMapping("/mine/gold")
    public String mineGod() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        ChosenGoldMineEntity chosenGoldMineEntity = chosenGoldMineEntityRepository.findById(1L).orElse(null);
        GoldMine currentGoldMine = (chosenGoldMineEntity != null) ? chosenGoldMineEntity.getCurrentGoldMine() : null;
        if (currentGoldMine != null && currentGoldMine.getMaterials(player)) {
            playerRepository.save(player);
            return "redirect:/gold/mine/success";
        } else {
            return "redirect:/mine";
        }
    }
    @GetMapping("/gold/mine/success")
    public String getSuccessMessage(){
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        ChosenSilverMineEntity chosenSilverMineEntity = chosenSilverMineRepository.findById(1L).orElse(null);
        SilverMine currentSilverMine = (chosenSilverMineEntity != null) ? chosenSilverMineEntity.getCurrentSilverMine() : null;
        if (currentSilverMine != null && currentSilverMine.getMaterials(player)) {
            playerRepository.save(player);
            return "redirect:/gold/mine/success";
        } else {
            return "redirect:/mine";
        }
    }
}
