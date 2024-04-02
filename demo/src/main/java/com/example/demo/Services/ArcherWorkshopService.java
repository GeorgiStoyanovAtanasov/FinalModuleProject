package com.example.demo.Services;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Repositories.ArcherRepository;
import com.example.demo.Repositories.ArcherWorkshopRepository;
import com.example.demo.Repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ArcherWorkshopService {
    @Autowired
    ArcherWorkshopRepository archerWorkshopRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    ArcherRepository archerRepository;

    public boolean addArcher(Player player, ArcherWorkshop archerWorkshop) {
        if (canPlayerGetArcher(archerWorkshop)) {
            int goldAmount = player.getGold().getAmount();
            if (goldAmount > 10) {
                Archer archer = new Archer(player);
                archerRepository.save(archer);
                player.getArchers().add(archer);
                player.getGold().setAmount(goldAmount - 10);
                playerRepository.save(player);
            }
            return true;
        }
        return false;
    }

    public synchronized boolean canPlayerGetArcher(ArcherWorkshop archerWorkshop) {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastAccessDate = archerWorkshop.getLastAccessDate();
        if (lastAccessDate == null || !lastAccessDate.equals(currentDate)) {
            archerWorkshop.setLastAccessDate(currentDate);
            archerWorkshopRepository.save(archerWorkshop);
            return true;
        } else {
            return false;
        }
    }
}
