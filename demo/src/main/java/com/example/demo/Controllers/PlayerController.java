package com.example.demo.Controllers;

import com.example.demo.Constants.Role;
//import com.example.demo.Entities.ChosenMines.ChosenGoldMineEntity;
import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Cavalry;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Materials.Crystal;
import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Materials.Silver;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.*;
import com.example.demo.Services.AttackService;
import com.example.demo.Services.PlayerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PlayerController {
    @Autowired
    GoldMineRepository goldMineRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
//    @Autowired
//    ChosenGoldMineEntityRepository chosenGoldMineEntityRepository;
    @Autowired
    GoldRepository goldRepository;
    @Autowired
    SilverRepository silverRepository;
    @Autowired
    CrystalRepository crystalRepository;
    @Autowired
    PlayerService playerService;
    @Autowired
    ArcherRepository archerRepository;
    @Autowired
    SwordsmanRepository swordsmanRepository;
    @Autowired
    CavalryRepository cavalryRepository;
    @Autowired
    AttackService attackService;
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model, RedirectAttributes redirectAttributes) {
        Player player = playerRepository.findByUsername(username);
        if (player == null || !passwordEncoder.matches(password, player.getPassword())) {
            redirectAttributes.addFlashAttribute("wrongCredentials", "wrong credentials");
            return "/login";
        }
        return "redirect:/home";
    }
//    @GetMapping("/login?error")
//    public String loginError(Model model) {
//        model.addAttribute("wrongCredentials", true);
//        return "login";
//    }

    @GetMapping("/logout")
    public String performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        this.logoutHandler.logout(request, response, authentication);//.doLogout(request, response, authentication);
        return "redirect:/login";
    }
    @GetMapping("/home")
    public String home(Model model) {
        //int[] adjustedValues = attackService.adjustNumbers(-1, 1, 0, Archer.value, Swordsman.value, Cavalry.value);
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
        model.addAttribute("archers", archerRepository.findAllByPlayerAndInBattle(player,false).size());
        model.addAttribute("swordsman", swordsmanRepository.findAllByPlayerAndInBattle(player,false).size());
        model.addAttribute("cavalries", cavalryRepository.findAllByPlayerAndInBattle(player,false).size());
        return "home";
    }
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("player", new Player());
        return "register";
    }

    @PostMapping("/register")
    public String registerPlayer(@Valid Player player, BindingResult bindingResult, Model model) {
        return playerService.registerPlayer(player, bindingResult, model);
    }
}
