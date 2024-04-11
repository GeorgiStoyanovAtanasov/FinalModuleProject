package com.example.demo.Services;

import com.example.demo.Entities.ChosenMines.ChosenGoldMineEntity;
import com.example.demo.Entities.Mines.CrystalMine;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.ChosenCrystalMineEntityRepository;
import com.example.demo.Repositories.ChosenGoldMineEntityRepository;
import com.example.demo.Repositories.GoldMineRepository;
import com.example.demo.Repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Map;

@Service
public class GoldMineService {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    ChosenGoldMineEntityRepository chosenGoldMineEntityRepository;
    @Autowired
    GoldMineRepository goldMineRepository;
    @Autowired
    LastAccessedGoldMineService lastAccessedGoldMineService;

    public String mineGold(RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        // Retrieve the current gold mine for the player
        GoldMine currentGoldMine = getCurrentGoldMineForPlayer(player);
        if(currentGoldMine == null){
            GoldMine goldMine = getNextGoldMine(null);
            canPlayerGetMaterials(goldMine, player);
            getMaterials(goldMine, player);
            playerRepository.save(player);
            goldMineRepository.save(goldMine);
            return "redirect:/gold/mine/success";
        }
        if (canPlayerGetMaterials(currentGoldMine, player)) {
            GoldMine goldMine = getNextGoldMine(currentGoldMine);
            getMaterials(goldMine, player);
            playerRepository.save(player);
            goldMineRepository.save(goldMine);
            return "redirect:/gold/mine/success";
        } else {
            redirectAttributes.addFlashAttribute("timeLimitForGold", "you can't mine gold for today anymore, come back tomorrow and see what exciting gold mine is awaiting you.");
            return "redirect:/mine";
        }
//        if (currentGoldMine != null && getMaterials(currentGoldMine, player)) {
//            playerRepository.save(player);
//            return "redirect:/gold/mine/success";
//        } else {
//            return "redirect:/mine";
//        }
    }

    public GoldMine getCurrentGoldMineForPlayer(Player player) {
        // Get today's date
        LocalDate currentDate = LocalDate.now();

        // Retrieve the last accessed gold mine for the player on the current date
        GoldMine lastAccessedGoldMine = lastAccessedGoldMineService.getLastAccessedGoldMine(player);

        // If the player has already accessed a gold mine today, return the same gold mine
        if (lastAccessedGoldMine != null) {
            return lastAccessedGoldMine;
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

    public GoldMine getNextGoldMine(GoldMine goldMine) {
        // Retrieve the current chosen gold mine entity
        ChosenGoldMineEntity chosenGoldMineEntity = chosenGoldMineEntityRepository.findById(1L).orElse(null);
        if (goldMine != null) {
            // Retrieve the next gold mine in the sequence
            Long nextGoldMineId = goldMine.getId() + 1;
            GoldMine nextGoldMine = goldMineRepository.findById(nextGoldMineId).orElse(null);

            // If the next gold mine doesn't exist, cycle back to the first gold mine
            if (nextGoldMine == null) {
                nextGoldMine = goldMineRepository.findById(1L).orElse(null);
            }

            return nextGoldMine;
        }
        return goldMineRepository.findById(1L).orElse(null);
    }

    public void getMaterials(GoldMine goldMine, Player player) {
        LocalDate currentDate = LocalDate.now();
        Map<Player, LocalDate> lastAccessDates = goldMine.getLastAccessDates();
        lastAccessDates.put(player, currentDate);
        player.getLastAccessedGoldMines().put(currentDate, goldMine.getId());
        int value = goldMine.getValue();
        player.getGold().setAmount(player.getGold().getAmount() + value);
    }

    public synchronized boolean canPlayerGetMaterials(GoldMine goldMine, Player player) {
        LocalDate currentDate = LocalDate.now();
        Map<Player, LocalDate> lastAccessDates = goldMine.getLastAccessDates();
        LocalDate lastAccessDate = lastAccessDates.getOrDefault(player, null);

        if (lastAccessDate == null || !lastAccessDate.equals(currentDate)) {
            lastAccessDates.put(player, currentDate);
            return true;
        } else {
            return false;
        }
    }
}
