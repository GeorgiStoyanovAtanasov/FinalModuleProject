package com.example.demo.Services;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Materials.Silver;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Entities.Workshops.SwordsmanWorkshop;
import com.example.demo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;

@Service
public class SwordsmanWorkshopService {


    @Autowired
    SwordsmanWorkshopRepository swordsmanWorkshopRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    SwordsmanRepository swordsmanRepository;
    public String buySwordsman(Long swordsmanWorkshopId, Model model){
        SwordsmanWorkshop swordsmanWorkshop = swordsmanWorkshopRepository.findById(swordsmanWorkshopId).orElse(null);
        if (swordsmanWorkshop == null) {
            return "redirect:/add/swordsman";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        boolean doesPlayerGetSwordsman = addSwordsman(player, swordsmanWorkshop);
        if (!doesPlayerGetSwordsman) {
            return "redirect:/add/swordsman";
        }
        model.addAttribute("swordsmen", player.getSwordsmen());
        return "redirect:/home";
        //return "add-swordsman-success";
    }

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

