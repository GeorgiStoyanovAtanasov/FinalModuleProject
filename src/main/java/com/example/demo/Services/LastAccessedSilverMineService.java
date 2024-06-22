package com.example.demo.Services;

import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Mines.SilverMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.GoldMineRepository;
import com.example.demo.Repositories.PlayerRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.Repositories.SilverMineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LastAccessedSilverMineService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private SilverMineRepository silverMineRepository;

    public SilverMine getLastAccessedSilverMine(Player player) {
        Map<LocalDate, Long> lastAccessedSilverMines = player.getLastAccessedSilverMines();
        if (lastAccessedSilverMines != null && !lastAccessedSilverMines.isEmpty()) {
            LocalDate lastAccessDate = lastAccessedSilverMines.keySet().stream()
                    .max(LocalDate::compareTo)
                    .orElse(null);
            if (lastAccessDate != null) {
                Long silverMineId = lastAccessedSilverMines.get(lastAccessDate);
                return silverMineRepository.findById(silverMineId).orElse(null);
                // Fetch the gold mine from repository based on goldMineId
                // You need to implement this based on your repository structure
                // For example:
                // return goldMineRepository.findById(goldMineId).orElse(null);
            }
        }
        return null; // No last accessed gold mine found
    }

    public void setLastAccessedSilverMine(Player player, SilverMine silverMine) {
        Map<LocalDate, Long> lastAccessedSilverMines = player.getLastAccessedSilverMines();
        if (lastAccessedSilverMines == null) {
            lastAccessedSilverMines = new HashMap<>();
            player.setLastAccessedSilverMines(lastAccessedSilverMines);
        }
        lastAccessedSilverMines.put(LocalDate.now(), silverMine.getId());
        playerRepository.save(player);
    }
}
