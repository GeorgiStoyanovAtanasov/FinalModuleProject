package com.example.demo.Controllers;

import com.example.demo.Entities.ChosenMines.ChosenGoldMineEntity;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.ChosenGoldMineEntityRepository;
import com.example.demo.Repositories.GoldMineRepository;
import com.example.demo.Repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PlayerController {
    @Autowired
    GoldMineRepository goldMineRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ChosenGoldMineEntityRepository chosenGoldMineEntityRepository;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Player player = playerRepository.findByUsername(username);
        if (player == null || !passwordEncoder.matches(password, player.getPassword())) {
            return "login";
        }
        return "redirect:/home";
    }
    @GetMapping("/home")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        model.addAttribute("username", player.getUsername());
        model.addAttribute("gold", player.getGold().getAmount());
        model.addAttribute("silver", player.getSilver().getAmount());
        model.addAttribute("crystal", player.getCrystal().getAmount());
        model.addAttribute("archerWorkshops", player.getArcherWorkshops().size());
        model.addAttribute("swordsmanWorkshops", player.getSwordsmanWorkshops().size());
        model.addAttribute("archers", player.getArchers().size());
        model.addAttribute("swordsman", player.getSwordsmen().size());
        return "home";
    }
}
