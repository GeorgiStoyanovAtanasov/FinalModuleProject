package com.example.demo.Services;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Entities.Workshops.SwordsmanWorkshop;
import com.example.demo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SwordsmanWorkshopService {
    @Autowired
    SwordsmanWorkshopRepository swordsmanWorkshopRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    SwordsmanRepository swordsmanRepository;

    public boolean addSwordsman(Player player, SwordsmanWorkshop swordsmanWorkshop) {
        if (canPlayerGetSwordsman(swordsmanWorkshop)) {
            int silverAmount = player.getSilver().getAmount();
            if (silverAmount >= 20) {
                Swordsman swordsman = new Swordsman(player);
                swordsmanRepository.save(swordsman);
                player.getSwordsmen().add(swordsman);
                player.getSilver().setAmount(silverAmount - 20);
                playerRepository.save(player);
            }
            return true;
        }
        return false;
    }

    public synchronized boolean canPlayerGetSwordsman(SwordsmanWorkshop swordsmanWorkshop) {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastAccessDate = swordsmanWorkshop.getLastAccessDate();
        if (lastAccessDate == null || !lastAccessDate.equals(currentDate)) {
            swordsmanWorkshop.setLastAccessDate(currentDate);
            swordsmanWorkshopRepository.save(swordsmanWorkshop);
            return true;
        } else {
            return false;
        }
    }
}

