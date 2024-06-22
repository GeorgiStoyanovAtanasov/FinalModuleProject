package com.example.demo.Services;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Entities.Workshops.SwordsmanWorkshop;
import com.example.demo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Service
public class SwordsmanWorkshopService {
    @Autowired
    SwordsmanWorkshopRepository swordsmanWorkshopRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    SwordsmanRepository swordsmanRepository;
    public String buySwordsman(Long swordsmanWorkshopId, Model model, RedirectAttributes redirectAttributes){
        if(swordsmanWorkshopId == null){
            redirectAttributes.addFlashAttribute("null", "no workshop selected.");
            return "redirect:/add/swordsman";
        }
        SwordsmanWorkshop swordsmanWorkshop = swordsmanWorkshopRepository.findById(swordsmanWorkshopId).orElse(null);
        if (swordsmanWorkshop == null) {
            return "redirect:/add/swordsman";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
//        boolean doesPlayerGetSwordsman = addSwordsman(player, swordsmanWorkshop);
//        if (!doesPlayerGetSwordsman) {
//            return "redirect:/add/swordsman";
//        }
//        model.addAttribute("swordsmen", player.getSwordsmen());
//        return "redirect:/home";
        //return "add-swordsman-success";
        return addSwordsman(player, swordsmanWorkshop, model, redirectAttributes);
    }

    public String addSwordsman(Player player, SwordsmanWorkshop swordsmanWorkshop, Model model, RedirectAttributes redirectAttributes) {
        int silverAmount = player.getSilver().getAmount();
        if (silverAmount >= 20) {
            if (canPlayerGetSwordsman(swordsmanWorkshop)) {
                Swordsman swordsman = new Swordsman(player);
                swordsmanRepository.save(swordsman);
                player.getSwordsmen().add(swordsman);
                player.getSilver().setAmount(silverAmount - 20);
                playerRepository.save(player);
                return "redirect:/home";
            }  else {
                redirectAttributes.addFlashAttribute("timeLimit", "you can't make any more swordsmen from this workshop, consider buying another one.");
                return "redirect:/add/swordsman";
            }
        }
        redirectAttributes.addFlashAttribute("notEnoughSilver", "not enough silver to buy a swordsman.");
        return "redirect:/add/swordsman";
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

