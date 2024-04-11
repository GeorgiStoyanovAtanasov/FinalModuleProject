package com.example.demo.Services;

import com.example.demo.Entities.ChosenMines.ChosenCrystalMineEntity;
import com.example.demo.Entities.ChosenMines.ChosenGoldMineEntity;
import com.example.demo.Entities.ChosenMines.ChosenSilverMineEntity;
import com.example.demo.Entities.Mines.CrystalMine;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Mines.SilverMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Map;

@Service
public class CrystalMineService {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    ChosenCrystalMineEntityRepository chosenCrystalMineEntityRepository;
    @Autowired
    CrystalMineRepository crystalMineRepository;
    @Autowired
    LastAccessedCrystalMineService lastAccessedCrystalMineService;

    public String mineCrystal(RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        // Retrieve the current gold mine for the player
        CrystalMine currentCrystalMine = getCurrentCrystalMineForPlayer(player);
        if(currentCrystalMine == null){
            CrystalMine crystalMine = getNextCrystalMine(null);
            canPlayerGetMaterials(crystalMine, player);
            getMaterials(crystalMine, player);
            playerRepository.save(player);
            crystalMineRepository.save(crystalMine);
            return "redirect:/crystal/mine/success";
        }
        if (canPlayerGetMaterials(currentCrystalMine, player)) {
            CrystalMine crystalMine = getNextCrystalMine(currentCrystalMine);
            getMaterials(crystalMine, player);
            playerRepository.save(player);
            crystalMineRepository.save(crystalMine);
            return "redirect:/crystal/mine/success";
        } else {
            redirectAttributes.addFlashAttribute("timeLimitForCrystal", "you can't mine crystal for today anymore, come back tomorrow and see what exciting crystal mine is awaiting you.");
            return "redirect:/mine";
        }
//        if (currentGoldMine != null && getMaterials(currentGoldMine, player)) {
//            playerRepository.save(player);
//            return "redirect:/gold/mine/success";
//        } else {
//            return "redirect:/mine";
//        }
    }

    public CrystalMine getCurrentCrystalMineForPlayer(Player player) {
        // Get today's date
        LocalDate currentDate = LocalDate.now();

        // Retrieve the last accessed gold mine for the player on the current date
        CrystalMine lastAccessedCrystalMine = lastAccessedCrystalMineService.getLastAccessedCrystalMine(player);

        // If the player has already accessed a gold mine today, return the same gold mine
        if (lastAccessedCrystalMine != null) {
            return lastAccessedCrystalMine;
        }
//
//        // Otherwise, get the next gold mine in the sequence
//        GoldMine nextGoldMine = getNextGoldMine();
//
//        // Update the player's last accessed gold mine mapping
//        lastAccessedGoldMineService.setLastAccessedGoldMine(player, nextGoldMine);
//
//        return nextGoldMine;


        //player.getLastAccessedGoldMines().put(currentDate, 1L);
        return null;
    }

    public CrystalMine getNextCrystalMine(CrystalMine crystalMine) {
        // Retrieve the current chosen gold mine entity
        ChosenCrystalMineEntity chosenCrystalMineEntity = chosenCrystalMineEntityRepository.findById(1L).orElse(null);
        if (crystalMine != null) {
            // Retrieve the next gold mine in the sequence
            Long nextCrystalMineId = crystalMine.getId() + 1;
            CrystalMine nextCrystalMine = crystalMineRepository.findById(nextCrystalMineId).orElse(null);

            // If the next gold mine doesn't exist, cycle back to the first gold mine
            if (nextCrystalMine == null) {
                nextCrystalMine = crystalMineRepository.findById(1L).orElse(null);
            }

            return nextCrystalMine;
        }
        return crystalMineRepository.findById(1L).orElse(null);
    }

    public void getMaterials(CrystalMine crystalMine, Player player) {
        LocalDate currentDate = LocalDate.now();
        Map<Player, LocalDate> lastAccessDates = crystalMine.getLastAccessDates();
        lastAccessDates.put(player, currentDate);
        player.getLastAccessedCrystalMines().put(currentDate, crystalMine.getId());
        int value = crystalMine.getValue();
        player.getCrystal().setAmount(player.getCrystal().getAmount() + value);
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
