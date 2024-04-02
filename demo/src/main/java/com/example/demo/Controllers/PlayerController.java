package com.example.demo.Controllers;

import com.example.demo.Constants.Role;
import com.example.demo.Entities.ChosenMines.ChosenGoldMineEntity;
import com.example.demo.Entities.Materials.Crystal;
import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Materials.Silver;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    @Autowired
    GoldRepository goldRepository;
    @Autowired
    SilverRepository silverRepository;
    @Autowired
    CrystalRepository crystalRepository;

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
        model.addAttribute("cavalryWorkshops", player.getCavalryWorkshops().size());
        model.addAttribute("archers", player.getArchers().size());
        model.addAttribute("swordsman", player.getSwordsmen().size());
        model.addAttribute("cavalries", player.getCavalries().size());
        return "home";
    }
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("player", new Player());
        return "register";
    }

    @PostMapping("/register")
    public String registerPlayer(@Valid Player player, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors
            return "register"; // Return to registration form
        }

        // Check if player already exists by username
        if (playerRepository.findByUsername(player.getUsername()) != null) {
            //here I changed the check because before it was playerRepository.findByUsername(player.getUsername()) == null
            // Handle case where player already exists
            return "register"; // Return to registration form
        }
        Player playerToSave = new Player();
        playerToSave.setUsername(player.getUsername());
        playerToSave.setPassword(passwordEncoder.encode(player.getPassword()));
        // Create instances of resources for the player
        Gold gold = new Gold();
        Silver silver = new Silver();
        Crystal crystal = new Crystal();

        // Saved the resources to the database
        crystalRepository.save(crystal);
        goldRepository.save(gold);
        silverRepository.save(silver);

        // Assign resources to the player
        playerToSave.setGold(gold);
        playerToSave.setSilver(silver);
        playerToSave.setCrystal(crystal);

        // Set default role, the player musn't choose their role, ADMIN role must only ba assigned through the database
        playerToSave.setRole(Role.USER);

        // Save the player to database
        playerRepository.save(playerToSave);
        crystal.setPlayer(playerToSave);
        gold.setPlayer(playerToSave);
        silver.setPlayer(playerToSave);
        crystalRepository.save(crystal);
        goldRepository.save(gold);
        silverRepository.save(silver);
        return "redirect:/login";
    }
}
