package com.example.demo.Services;

import com.example.demo.Entities.ChosenMines.ChosenGoldMineEntity;
import com.example.demo.Entities.Mines.CrystalMine;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.ChosenCrystalMineEntityRepository;
import com.example.demo.Repositories.ChosenGoldMineEntityRepository;
import com.example.demo.Repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
@Service
public class GoldMineService {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    ChosenGoldMineEntityRepository chosenGoldMineEntityRepository;
    public String mineGold(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        ChosenGoldMineEntity chosenGoldMineEntity = chosenGoldMineEntityRepository.findById(1L).orElse(null);
        GoldMine currentGoldMine = (chosenGoldMineEntity != null) ? chosenGoldMineEntity.getCurrentGoldMine() : null;
        if (currentGoldMine != null && getMaterials(currentGoldMine, player)) {
            playerRepository.save(player);
            return "redirect:/gold/mine/success";
        } else {
            return "redirect:/mine";
        }
    }

    public boolean getMaterials(GoldMine goldMine, Player player) {
        if (canPlayerGetMaterials(goldMine, player)) {
            int value = goldMine.getValue();
            player.getGold().setAmount(player.getGold().getAmount() + value);
            return true;
        }
        return false;
    }

    public synchronized boolean canPlayerGetMaterials(GoldMine goldMine, Player player) {
        LocalDate currentDate = LocalDate.now();
        Map<Player, LocalDate> lastAccessDates = goldMine.getLastAccessDates();
        LocalDate lastAccessDate = lastAccessDates.getOrDefault(player, null);

        if (lastAccessDate == null || !lastAccessDate.equals(currentDate)) {
            lastAccessDates.put(player, currentDate);
            return true;
        } else {
            return false;
        }
    }
}
