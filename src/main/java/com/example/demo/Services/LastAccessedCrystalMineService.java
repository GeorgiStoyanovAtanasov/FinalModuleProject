package com.example.demo.Services;

import com.example.demo.Entities.Mines.CrystalMine;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Mines.SilverMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.CrystalMineRepository;
import com.example.demo.Repositories.GoldMineRepository;
import com.example.demo.Repositories.PlayerRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.Repositories.SilverMineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LastAccessedCrystalMineService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private CrystalMineRepository crystalMineRepository;

    public CrystalMine getLastAccessedCrystalMine(Player player) {
        Map<LocalDate, Long> lastAccessedCrystalMines = player.getLastAccessedCrystalMines();
        if (lastAccessedCrystalMines != null && !lastAccessedCrystalMines.isEmpty()) {
            LocalDate lastAccessDate = lastAccessedCrystalMines.keySet().stream()
                    .max(LocalDate::compareTo)
                    .orElse(null);
            if (lastAccessDate != null) {
                Long crystalMineId = lastAccessedCrystalMines.get(lastAccessDate);
                return crystalMineRepository.findById(crystalMineId).orElse(null);
                // Fetch the gold mine from repository based on goldMineId
                // You need to implement this based on your repository structure
                // For example:
                // return goldMineRepository.findById(goldMineId).orElse(null);
            }
        }
        return null; // No last accessed gold mine found
    }

    public void setLastAccessedCrystalMine(Player player, CrystalMine crystalMine) {
        Map<LocalDate, Long> lastAccessedCrystalMines = player.getLastAccessedCrystalMines();
        if (lastAccessedCrystalMines == null) {
            lastAccessedCrystalMines = new HashMap<>();
            player.setLastAccessedCrystalMines(lastAccessedCrystalMines);
        }
        lastAccessedCrystalMines.put(LocalDate.now(), crystalMine.getId());
        playerRepository.save(player);
    }
}
