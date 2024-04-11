package com.example.demo.Services;

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
public class SilverMineService {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    ChosenSilverMineRepository chosenSilverMineRepository;
    @Autowired
    SilverMineRepository silverMineRepository;
    @Autowired
    LastAccessedSilverMineService lastAccessedSilverMineService;

    public String mineSilver(RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Player player = playerRepository.findByUsername(username);
        // Retrieve the current gold mine for the player
        SilverMine currentSilverMine = getCurrentSilverMineForPlayer(player);
        if(currentSilverMine == null){
            SilverMine silverMine = getNextSilverMine(null);
            canPlayerGetMaterials(silverMine, player);
            getMaterials(silverMine, player);
            playerRepository.save(player);
            silverMineRepository.save(silverMine);
            return "redirect:/silver/mine/success";
        }
        if (canPlayerGetMaterials(currentSilverMine, player)) {
            SilverMine silverMine = getNextSilverMine(currentSilverMine);
            getMaterials(silverMine, player);
            playerRepository.save(player);
            silverMineRepository.save(silverMine);
            return "redirect:/silver/mine/success";
        } else {
            redirectAttributes.addFlashAttribute("timeLimitForSilver", "you can't mine silver for today anymore, come back tomorrow and see what exciting silver mine is awaiting you.");
            return "redirect:/mine";
        }
//        if (currentGoldMine != null && getMaterials(currentGoldMine, player)) {
//            playerRepository.save(player);
//            return "redirect:/gold/mine/success";
//        } else {
//            return "redirect:/mine";
//        }
    }

    public SilverMine getCurrentSilverMineForPlayer(Player player) {
        // Get today's date
        LocalDate currentDate = LocalDate.now();

        // Retrieve the last accessed gold mine for the player on the current date
        SilverMine lastAccessedSilverMine = lastAccessedSilverMineService.getLastAccessedSilverMine(player);

        // If the player has already accessed a gold mine today, return the same gold mine
        if (lastAccessedSilverMine != null) {
            return lastAccessedSilverMine;
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

    public SilverMine getNextSilverMine(SilverMine silverMine) {
        // Retrieve the current chosen gold mine entity
        ChosenSilverMineEntity chosenSilverMineEntity = chosenSilverMineRepository.findById(1L).orElse(null);
        if (silverMine != null) {
            // Retrieve the next gold mine in the sequence
            Long nextSilverMineId = silverMine.getId() + 1;
            SilverMine nextSilverMine = silverMineRepository.findById(nextSilverMineId).orElse(null);

            // If the next gold mine doesn't exist, cycle back to the first gold mine
            if (nextSilverMine == null) {
                nextSilverMine = silverMineRepository.findById(1L).orElse(null);
            }

            return nextSilverMine;
        }
        return silverMineRepository.findById(1L).orElse(null);
    }

    public void getMaterials(SilverMine silverMine, Player player) {
        LocalDate currentDate = LocalDate.now();
        Map<Player, LocalDate> lastAccessDates = silverMine.getLastAccessDates();
        lastAccessDates.put(player, currentDate);
        player.getLastAccessedSilverMines().put(currentDate, silverMine.getId());
        int value = silverMine.getValue();
        player.getSilver().setAmount(player.getSilver().getAmount() + value);
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
