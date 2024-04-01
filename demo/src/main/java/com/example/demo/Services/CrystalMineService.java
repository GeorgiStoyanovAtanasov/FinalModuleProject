package com.example.demo.Services;

import com.example.demo.Entities.Mines.CrystalMine;
import com.example.demo.Entities.Player;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
@Service
public class CrystalMineService {

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
