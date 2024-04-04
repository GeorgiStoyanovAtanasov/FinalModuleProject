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
import jakarta.validation.Valid;
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
