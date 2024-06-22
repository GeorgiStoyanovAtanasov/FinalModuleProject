package com.example.demo.Services;

import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.GoldMineRepository;
import com.example.demo.Repositories.PlayerRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LastAccessedGoldMineService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GoldMineRepository goldMineRepository;

    public GoldMine getLastAccessedGoldMine(Player player) {
        Map<LocalDate, Long> lastAccessedGoldMines = player.getLastAccessedGoldMines();
        if (lastAccessedGoldMines != null && !lastAccessedGoldMines.isEmpty()) {
            LocalDate lastAccessDate = lastAccessedGoldMines.keySet().stream()
                    .max(LocalDate::compareTo)
                    .orElse(null);
            if (lastAccessDate != null) {
                Long goldMineId = lastAccessedGoldMines.get(lastAccessDate);
                return goldMineRepository.findById(goldMineId).orElse(null);
                // Fetch the gold mine from repository based on goldMineId
                // You need to implement this based on your repository structure
                // For example:
                // return goldMineRepository.findById(goldMineId).orElse(null);
            }
        }
        return null; // No last accessed gold mine found
    }

    public void setLastAccessedGoldMine(Player player, GoldMine goldMine) {
        Map<LocalDate, Long> lastAccessedGoldMines = player.getLastAccessedGoldMines();
        if (lastAccessedGoldMines == null) {
            lastAccessedGoldMines = new HashMap<>();
            player.setLastAccessedGoldMines(lastAccessedGoldMines);
        }
        lastAccessedGoldMines.put(LocalDate.now(), goldMine.getId());
        playerRepository.save(player);
    }
}
