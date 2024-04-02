package com.example.demo.Controllers;

import com.example.demo.Services.ArmyService;
import org.springframework.ui.Model;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Random;

@Controller
public class BattleController {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ArmyService armyService;

    @GetMapping("/battle")
    public String showBattleForm(Model model) {
        List<Player> players = (List<Player>) playerRepository.findAll();
        model.addAttribute("players", playerRepository.findAll());
        return "battle";
    }

    @PostMapping("/startBattle")
    public String startBattle(@RequestParam("player1") Long player1Id, @RequestParam("player2") Long player2Id, Model model) {
        Player player1 = playerRepository.findById(player1Id).orElseThrow(() -> new IllegalArgumentException("Invalid player1 id"));
        Player player2 = playerRepository.findById(player2Id).orElseThrow(() -> new IllegalArgumentException("Invalid player2 id"));

        // Примерна логика за битката - просто сравнение на броя на войниците
        Random random = new Random();
        int armySize1 = (int) ArmyService.getArmySize();
        int armySize2 = (int) ArmyService.getArmySize();
        int player1Strength = random.nextInt(armySize1) + 1;
        int player2Strength = random.nextInt(armySize2) + 1;
        String winner;
        if (player1Strength > player2Strength) {
            winner = player1.getUsername();
        } else if (player1Strength < player2Strength) {
            winner = player2.getUsername();
        } else {
            winner = "Equal";
        }

        model.addAttribute("player1", player1.getUsername());
        model.addAttribute("player2", player2.getUsername());
        model.addAttribute("player1Strength", player1Strength);
        model.addAttribute("player2Strength", player2Strength);
        model.addAttribute("winner", winner);

        return "battle_result";
    }
    @GetMapping("/player1/army")
    public Player getPlayer1Army() {
        return armyService.getPlayer1Army();
    }

    @GetMapping("/player2/army")
    public Player getPlayer2Army() {
        return armyService.getPlayer2Army();
    }
}
