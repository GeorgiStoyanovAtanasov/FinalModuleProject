package com.example.demo.Services;

import com.example.demo.Entities.Fighters.Cavalry;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.CavalryWorkshop;
import com.example.demo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CavalryWorkshopService {
    @Autowired
    CavalryWorkshopRepository cavalryWorkshopRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    CavalryRepository cavalryRepository;

    public boolean addCavalry(Player player, CavalryWorkshop cavalryWorkshop) {
        if (canPlayerGetCavalry(cavalryWorkshop)) {
            Cavalry cavalry = new Cavalry(player);
            cavalryRepository.save(cavalry);
            player.getCavalry().add(cavalry);
            playerRepository.save(player);
            int goldAmount = player.getGold().getAmount();
            if (goldAmount > 10) {
                player.getGold().setAmount(goldAmount - 10);
                playerRepository.save(player);
            }
            return true;
        }
        return false;
    }

    public synchronized boolean canPlayerGetCavalry(CavalryWorkshop cavalryWorkshop) {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastAccessDate = cavalryWorkshop.getLastAccessDate();
        if (lastAccessDate == null || !lastAccessDate.equals(currentDate)) {
            cavalryWorkshop.setLastAccessDate(currentDate);
            cavalryWorkshopRepository.save(cavalryWorkshop);
            return true;
        } else {
            return false;
        }
    }
}
