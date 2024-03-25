package com.example.demo.Services;

import com.example.demo.Entities.Mines.CrystalMine;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Mines.SilverMine;
import com.example.demo.Entities.Player;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
@Service
public class SilverMineService {

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
