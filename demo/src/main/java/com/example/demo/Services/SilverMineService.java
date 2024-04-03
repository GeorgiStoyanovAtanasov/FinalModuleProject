package com.example.demo.Services;

import com.example.demo.Entities.ChosenMines.ChosenSilverMineEntity;
import com.example.demo.Entities.Mines.CrystalMine;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Mines.SilverMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.ChosenCrystalMineEntityRepository;
import com.example.demo.Repositories.ChosenSilverMineRepository;
import com.example.demo.Repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
@Service
public class SilverMineService {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    ChosenSilverMineRepository chosenSilverMineRepository;
    public String mineSilver(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        ChosenSilverMineEntity chosenSilverMineEntity = chosenSilverMineRepository.findById(1L).orElse(null);
        SilverMine currentSilverMine = (chosenSilverMineEntity != null) ? chosenSilverMineEntity.getCurrentSilverMine() : null;
        if (currentSilverMine != null && getMaterials(currentSilverMine, player)) {
            playerRepository.save(player);
            return "redirect:/silver/mine/success";
        } else {
            return "redirect:/mine";
        }
    }

    public boolean getMaterials(SilverMine silverMine, Player player) {
        if (canPlayerGetMaterials(silverMine, player)) {
            int value = silverMine.getValue();
            player.getSilver().setAmount(player.getSilver().getAmount() + value);
            return true;
        }
        return false;
    }

    public synchronized boolean canPlayerGetMaterials(SilverMine silverMine, Player player) {
        LocalDate currentDate = LocalDate.now();
        Map<Player, LocalDate> lastAccessDates = silverMine.getLastAccessDates();
        LocalDate lastAccessDate = lastAccessDates.getOrDefault(player, null);

        if (lastAccessDate == null || !lastAccessDate.equals(currentDate)) {
            lastAccessDates.put(player, currentDate);
            return true;
        } else {
            return false;
        }
    }
}
