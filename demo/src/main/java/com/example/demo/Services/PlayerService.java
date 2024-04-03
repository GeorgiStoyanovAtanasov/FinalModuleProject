package com.example.demo.Services;

import com.example.demo.Constants.Role;
import com.example.demo.Entities.Materials.Crystal;
import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Materials.Silver;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.CrystalRepository;
import com.example.demo.Repositories.GoldRepository;
import com.example.demo.Repositories.PlayerRepository;
import com.example.demo.Repositories.SilverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class PlayerService {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    GoldRepository goldRepository;
    @Autowired
    SilverRepository silverRepository;
    @Autowired
    CrystalRepository crystalRepository;
    public String registerPlayer(Player player, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register"; // Return to registration form
        }

        if (playerRepository.findByUsername(player.getUsername()) != null) {

            return "register";
        }
        Player playerToSave = new Player();
        playerToSave.setUsername(player.getUsername());
        playerToSave.setPassword(passwordEncoder.encode(player.getPassword()));

        Gold gold = new Gold();
        Silver silver = new Silver();
        Crystal crystal = new Crystal(10);

        crystalRepository.save(crystal);
        goldRepository.save(gold);
        silverRepository.save(silver);

               playerToSave.setGold(gold);
        playerToSave.setSilver(silver);
        playerToSave.setCrystal(crystal);

        playerToSave.setRole(Role.USER);

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
