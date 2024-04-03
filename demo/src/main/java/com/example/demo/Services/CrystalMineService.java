package com.example.demo.Services;

import com.example.demo.Entities.ChosenMines.ChosenCrystalMineEntity;
import com.example.demo.Entities.Mines.CrystalMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.ChosenCrystalMineEntityRepository;
import com.example.demo.Repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
@Service
public class CrystalMineService {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    ChosenCrystalMineEntityRepository chosenCrystalMineEntityRepository;
    public String mineCrystal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        ChosenCrystalMineEntity chosenCrystalMineEntity = chosenCrystalMineEntityRepository.findById(1L).orElse(null);
        CrystalMine currentCrystalMine = (chosenCrystalMineEntity != null) ? chosenCrystalMineEntity.getCurrentCrystalMine() : null;
        if (currentCrystalMine != null && getMaterials(currentCrystalMine, player)) {
            playerRepository.save(player);
            return "redirect:/crystal/mine/success";
        } else {
            return "redirect:/mine";
        }
    }

    public boolean getMaterials(CrystalMine crystalMine, Player player) {
        if (canPlayerGetMaterials(crystalMine, player)) {
            int value = crystalMine.getValue();
            player.getCrystal().setAmount(player.getCrystal().getAmount() + value);
            return true;
        }
        return false;
    }

    public synchronized boolean canPlayerGetMaterials(CrystalMine crystalMine, Player player) {
        LocalDate currentDate = LocalDate.now();
        Map<Player, LocalDate> lastAccessDates = crystalMine.getLastAccessDates();
        LocalDate lastAccessDate = lastAccessDates.getOrDefault(player, null);

        if (lastAccessDate == null || !lastAccessDate.equals(currentDate)) {
            lastAccessDates.put(player, currentDate);
            return true;
        } else {
            return false;
        }
    }
}
