package com.example.demo.Services;

import com.example.demo.Entities.Attack;
import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Cavalry;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttackService {
    @Autowired
    ArcherRepository archerRepository;
    @Autowired
    SwordsmanRepository swordsmanRepository;
    @Autowired
    CavalryRepository cavalryRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    AttackRepository attackRepository;
    @Autowired
    TransactionalService transactionalService;

    //    public String calculateArmyStrength(Player player){
//
//    }
    public String attackFrom(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player player = playerRepository.findByUsername(authentication.getName());
        List<Player> players = (List<Player>) playerRepository.findAll();
        List<Player> selectedPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (!players.get(i).getUsername().equals(player.getUsername()) && !players.get(i).isAttacked()) {
                selectedPlayers.add(players.get(i));
            }
        }
        model.addAttribute("players", selectedPlayers);
        return "choose-player-to-attack";
    }

    public String attack(Long playerToAttackId,
                         int numberOfArchers,
                         int numberOfSwordsmen,
                         int numberOfCavalries) {
        Player playerToBeAttack = playerRepository.findById(playerToAttackId).orElse(null);
        if (playerToBeAttack == null) {
            return "choose-player-to-attack";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player player = playerRepository.findByUsername(authentication.getName());
        if (numberOfArchers > archerRepository.findAllByPlayerAndInBattle(player, false).size()) {
            return "choose-player-to-attack";
        }
        if (numberOfSwordsmen > swordsmanRepository.findAllByPlayerAndInBattle(player, false).size()) {
            return "choose-player-to-attack";
        }
        if (numberOfCavalries > cavalryRepository.findAllByPlayerAndInBattle(player, false).size()) {
            return "choose-player-to-attack";
        }
        return attackPart2(player, playerToBeAttack, numberOfArchers, numberOfSwordsmen, numberOfCavalries);
//        List<Archer> archerToAttackWith = new ArrayList<>();
//        for (int i = 0; i < numberOfArchers; i++) {
//            archerToAttackWith.add(archerRepository.findAllByPlayerAndInBattle(player, false).get(i));
//            archerRepository.findAllByPlayerAndInBattle(player, false).get(i).setInBattle(true);
//        }
//        List<Swordsman> swordsmenToAttackWith = new ArrayList<>();
//        for (int i = 0; i < numberOfSwordsmen; i++) {
//            swordsmenToAttackWith.add(swordsmanRepository.findAllByPlayerAndInBattle(player, false).get(i));
//            swordsmanRepository.findAllByPlayerAndInBattle(player, false).get(i).setInBattle(true);
//        }
//        List<Cavalry> cavalriesToAttackWith = new ArrayList<>();
//        for (int i = 0; i < numberOfCavalries; i++) {
//            cavalriesToAttackWith.add(cavalryRepository.findAllByPlayerAndInBattle(player, false).get(i));
//            cavalryRepository.findAllByPlayerAndInBattle(player, false).get(i).setInBattle(true);
//        }
//        playerToBeAttack.setAttacked(true);
//        playerRepository.save(playerToBeAttack);
//        playerRepository.save(player);
//        attackRepository.save(new Attack(player, archerToAttackWith, swordsmenToAttackWith, cavalriesToAttackWith, playerToBeAttack));
//        return "attack-success";
    }

    public String attackPart2(Player player, Player playerToBeAttack, int numberOfArchers, int numberOfSwordsmen, int numberOfCavalries) {
        List<Archer> archerToAttackWith = new ArrayList<>();
        for (int i = 0; i < numberOfArchers; i++) {
            archerToAttackWith.add(archerRepository.findAllByPlayerAndInBattle(player, false).get(i));
            archerRepository.findAllByPlayerAndInBattle(player, false).get(i).setInBattle(true);
        }
        List<Swordsman> swordsmenToAttackWith = new ArrayList<>();
        for (int i = 0; i < numberOfSwordsmen; i++) {
            swordsmenToAttackWith.add(swordsmanRepository.findAllByPlayerAndInBattle(player, false).get(i));
            swordsmanRepository.findAllByPlayerAndInBattle(player, false).get(i).setInBattle(true);
        }
        List<Cavalry> cavalriesToAttackWith = new ArrayList<>();
        for (int i = 0; i < numberOfCavalries; i++) {
            cavalriesToAttackWith.add(cavalryRepository.findAllByPlayerAndInBattle(player, false).get(i));
            cavalryRepository.findAllByPlayerAndInBattle(player, false).get(i).setInBattle(true);
        }
        playerToBeAttack.setAttacked(true);
        playerRepository.save(playerToBeAttack);
        playerRepository.save(player);
        attackRepository.save(new Attack(player, archerToAttackWith, swordsmenToAttackWith, cavalriesToAttackWith, playerToBeAttack));
        return "attack-success";
    }

    public int calculateAttackStrength(Attack attack) {
        int archersStrength = attack.getArchersUsed().size() * Archer.value;
        int swordsmenStrength = attack.getSwordsmenUsed().size() * Swordsman.value;
        int cavalriesStrength = attack.getCavalriesUsed().size() * Cavalry.value;
        return archersStrength + swordsmenStrength + cavalriesStrength;
    }

    public int[] adjustNumbers(int num1, int num2, int num3, int value1, int value2, int value3) {
        int archers = num1 * value1;
        int swordsmen = num2 * value2;
        int cavalries = num3 * value3;
        if (num1 < 0 && num2 >= 0 && num3 >= 0) {
            if (cavalries + archers >= 0) {
                cavalries = cavalries + archers;
                num1 = 0;
                num2 = swordsmen / value2;
                num3 = cavalries / value3;
            } else {
                archers = archers + cavalries;
                num3 = 0;
                swordsmen = swordsmen + archers;
                num1 = 0;
                num2 = swordsmen / value2;
            }
        } else if(num1 >= 0 && num2 < 0 && num3 >= 0){
            if(archers + swordsmen >= 0){
                archers = archers + swordsmen;
                num2 = 0;
                num1 = archers/value1;
                num3 = cavalries/value3;
            } else {
                swordsmen = swordsmen + archers;
                num1 = 0;
                cavalries = cavalries + swordsmen;
                num2 = 0;
                num3 = cavalries/value3;
            }
        } else if(num1 >= 0 && num2 >= 0 && num3 < 0){
            if(swordsmen + cavalries >= 0){
                num3 = 0;
                swordsmen = swordsmen + cavalries;
                num2 = swordsmen/value2;
                num1 = archers/value1;
            } else {
                cavalries = cavalries + swordsmen;
                num2 = 0;
                archers = archers + cavalries;
                num3 = 0;
                num1 = archers/value1;
            }
        } else if(num1 < 0 && num2 < 0 && num3 > 0){
            cavalries = cavalries + archers;
            num1 = 0;
            cavalries = cavalries + swordsmen;
            num2 = 0;
            num3 = cavalries/value3;
        } else if(num1 < 0 && num2 > 0 && num3 < 0){
            swordsmen = swordsmen + archers;
            num1 = 0;
            swordsmen = swordsmen + cavalries;
            num3 = 0;
            num2 = swordsmen/value2;
        } else if(num1 > 0 && num2 < 0 && num3 < 0){
            archers = archers + cavalries;
            num3 = 0;
            archers = archers + swordsmen;
            num2 = 0;
            num1 = archers/value1;
        }
        return new int[]{num1, num2, num3};
    }


    public int calculateTotalDefenderValue(Attack attack) {
        int defenderArcherStrength = archerRepository.findAllByPlayerAndInBattle(attack.getDefender(), false).size() * Archer.value;
        int defenderSwordsmanStrength = swordsmanRepository.findAllByPlayerAndInBattle(attack.getDefender(), false).size() * Swordsman.value;
        int defenderCavalryStrength = cavalryRepository.findAllByPlayerAndInBattle(attack.getDefender(), false).size() * Cavalry.value;
        return defenderArcherStrength + defenderSwordsmanStrength + defenderCavalryStrength;
    }

    public String defendForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player defenderPlayer = playerRepository.findByUsername(authentication.getName());
        Attack attack = attackRepository.findByDefender(defenderPlayer);
        if (attack == null) {
            defenderPlayer.setAttacked(false);
            return "redirect:/home";
        }
//        int defenderArcherStrength = archerRepository.findAllByPlayerAndInBattle(attack.getDefender(), false).size() * Archer.value;
//        int defenderSwordsmanStrength = swordsmanRepository.findAllByPlayerAndInBattle(attack.getDefender(), false).size() * Swordsman.value;
//        int defenderCavalryStrength = cavalryRepository.findAllByPlayerAndInBattle(attack.getDefender(), false).size() * Cavalry.value;
//        int totalDefenderValue = defenderArcherStrength + defenderSwordsmanStrength + defenderCavalryStrength;
        int totalDefenderValue = calculateTotalDefenderValue(attack);
        if (totalDefenderValue < calculateAttackStrength(attack)) {
            int archersLeft = attack.getArchersUsed().size() - attack.getDefender().getArchers().size();
            int swordsmenLeft = attack.getSwordsmenUsed().size() - attack.getDefender().getSwordsmen().size();
            int cavalriesLeft = attack.getCavalriesUsed().size() - attack.getDefender().getCavalries().size();
            int[] adjustedValues = adjustNumbers(archersLeft, swordsmenLeft, cavalriesLeft, Archer.value, Swordsman.value, Cavalry.value);

            // Assigning the values from the array back to the original variables
            archersLeft = adjustedValues[0];
            swordsmenLeft = adjustedValues[1];
            cavalriesLeft = adjustedValues[2];
            return transferResources(attack, archersLeft, swordsmenLeft, cavalriesLeft, model);
//            attack.getAttacker().getGold().setAmount(attack.getAttacker().getGold().getAmount() + attack.getDefender().getGold().getAmount());
//            attack.getDefender().getGold().setAmount(0);
//            attack.getAttacker().getSilver().setAmount(attack.getAttacker().getSilver().getAmount() + attack.getDefender().getSilver().getAmount());
//            attack.getDefender().getSilver().setAmount(0);
//            attack.getAttacker().getCrystal().setAmount(attack.getAttacker().getCrystal().getAmount() + attack.getDefender().getCrystal().getAmount());
//            attack.getDefender().getCrystal().setAmount(0);
//            for (int i = 0; i < attack.getDefender().getArcherWorkshops().size(); i++) {
//                attack.getDefender().getArcherWorkshops().get(i).setPlayer(attack.getAttacker());
//                //attack.getAttacker().getArcherWorkshops().add(attack.getDefender().getArcherWorkshops().get(i));
//            }
//            //attack.getDefender().getArcherWorkshops().clear();
//            for (int i = 0; i < attack.getDefender().getSwordsmanWorkshops().size(); i++) {
//                //attack.getAttacker().getSwordsmanWorkshops().add(attack.getDefender().getSwordsmanWorkshops().get(i));
//                attack.getDefender().getSwordsmanWorkshops().get(i).setPlayer(attack.getAttacker());
//            }
//            //attack.getDefender().getSwordsmanWorkshops().clear();
//            for (int i = 0; i < attack.getDefender().getCavalryWorkshops().size(); i++) {
//                attack.getDefender().getCavalryWorkshops().get(i).setPlayer(attack.getAttacker());
//            }
//            for (int i = 0; i < archersLeft; i++) {
//                attack.getArchersUsed().get(i).setInBattle(false);
//                //attack.getAttacker().getArchers().add(attack.getArchersUsed().get(i));
//            }
//            for (int i = 0; i < swordsmenLeft; i++) {
//                attack.getSwordsmenUsed().get(i).setInBattle(false);
//                //attack.getAttacker().getSwordsmen().add(attack.getSwordsmenUsed().get(i));
//            }
//            for (int i = 0; i < cavalriesLeft; i++) {
//                attack.getCavalriesUsed().get(i).setInBattle(false);
//                //attack.getAttacker().getCavalries().add(attack.getCavalriesUsed().get(i));
//            }
//            deletePlayerEntities(attack.getDefender());
//            //attack.getDefender().getCavalryWorkshops().clear();
//            attack.getDefender().setAttacked(false);
//            playerRepository.save(attack.getAttacker());
//            playerRepository.save(attack.getDefender());
//            model.addAttribute("player", attack.getAttacker().getUsername());
//            attackRepository.delete(attack);
//            return "defeat";
        }


        playerRepository.save(attack.getAttacker());
        playerRepository.save(attack.getDefender());
        model.addAttribute("attack", attack);
        model.addAttribute("attackerUsername", attack.getAttacker().getUsername());
        model.addAttribute("archers", attack.getArchersUsed().size());
        model.addAttribute("swordsmen", attack.getSwordsmenUsed().size());
        model.addAttribute("cavalries", attack.getCavalriesUsed().size());
//        model.addAttribute("archersToChooseFrom", archerRepository.findAllByPlayerAndInBattle(attack.getDefender(), false));
//        model.addAttribute("swordsmenToChooseFrom", swordsmanRepository.findAllByPlayerAndInBattle(attack.getDefender(), false));
//        model.addAttribute("cavalriesToChooseFrom", cavalryRepository.findAllByPlayerAndInBattle(attack.getAttacker(), false));
        return "defend";
    }

    public String transferResources(Attack attack, int archersLeft, int swordsmenLeft, int cavalriesLeft, Model model) {
        attack.getAttacker().getGold().setAmount(attack.getAttacker().getGold().getAmount() + attack.getDefender().getGold().getAmount());
        attack.getDefender().getGold().setAmount(0);
        attack.getAttacker().getSilver().setAmount(attack.getAttacker().getSilver().getAmount() + attack.getDefender().getSilver().getAmount());
        attack.getDefender().getSilver().setAmount(0);
        attack.getAttacker().getCrystal().setAmount(attack.getAttacker().getCrystal().getAmount() + attack.getDefender().getCrystal().getAmount());
        attack.getDefender().getCrystal().setAmount(0);
        for (int i = 0; i < attack.getDefender().getArcherWorkshops().size(); i++) {
            attack.getDefender().getArcherWorkshops().get(i).setPlayer(attack.getAttacker());
            //attack.getAttacker().getArcherWorkshops().add(attack.getDefender().getArcherWorkshops().get(i));
        }
        //attack.getDefender().getArcherWorkshops().clear();
        for (int i = 0; i < attack.getDefender().getSwordsmanWorkshops().size(); i++) {
            //attack.getAttacker().getSwordsmanWorkshops().add(attack.getDefender().getSwordsmanWorkshops().get(i));
            attack.getDefender().getSwordsmanWorkshops().get(i).setPlayer(attack.getAttacker());
        }
        //attack.getDefender().getSwordsmanWorkshops().clear();
        for (int i = 0; i < attack.getDefender().getCavalryWorkshops().size(); i++) {
            attack.getDefender().getCavalryWorkshops().get(i).setPlayer(attack.getAttacker());
        }
        for (int i = 0; i < archersLeft; i++) {
            attack.getArchersUsed().get(i).setInBattle(false);
            //attack.getAttacker().getArchers().add(attack.getArchersUsed().get(i));
        }
        for (int i = 0; i < swordsmenLeft; i++) {
            attack.getSwordsmenUsed().get(i).setInBattle(false);
            //attack.getAttacker().getSwordsmen().add(attack.getSwordsmenUsed().get(i));
        }
        for (int i = 0; i < cavalriesLeft; i++) {
            attack.getCavalriesUsed().get(i).setInBattle(false);
            //attack.getAttacker().getCavalries().add(attack.getCavalriesUsed().get(i));
        }
        //attack.getDefender().getCavalryWorkshops().clear();
        attack.getDefender().setAttacked(false);
        playerRepository.save(attack.getAttacker());
        playerRepository.save(attack.getDefender());
        Iterable<Archer> archersToDelete = archerRepository.findAllByPlayer(attack.getDefender());
        Iterable<Swordsman> swordsmenToDelete = swordsmanRepository.findAllByPlayer(attack.getDefender());
        Iterable<Cavalry> cavalriesToDelete = cavalryRepository.findAllByPlayer(attack.getDefender());
        attackRepository.delete(attack);
        transactionalService.deleteOnlyTheEntitiesOfAnAttackThatHasBeenCountered(archersToDelete, swordsmenToDelete, cavalriesToDelete);
        model.addAttribute("player", attack.getAttacker().getUsername());
        return "defeat";
    }

    public String defend(int numberOfArchers,
                         int numberOfSwordsmen,
                         int numberOfCavalries, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player defenderPlayer = playerRepository.findByUsername(authentication.getName());
        Attack attack = attackRepository.findByDefender(defenderPlayer);
        if (attack == null) {
            defenderPlayer.setAttacked(false);
            playerRepository.save(defenderPlayer);
            return "redirect:/home";
        }
        if (numberOfArchers > archerRepository.findAllByPlayerAndInBattle(defenderPlayer, false).size()) {
            return "redirect:/defend";
            //return "defend";
        }
        if (numberOfSwordsmen > swordsmanRepository.findAllByPlayerAndInBattle(defenderPlayer, false).size()) {
            return "redirect:/defend";
            //return "defend";
        }
        if (numberOfCavalries > cavalryRepository.findAllByPlayerAndInBattle(defenderPlayer, false).size()) {
            return "redirect:/defend";
            //return "defend";
        }
        List<Archer> archersToDefendWith = new ArrayList<>();
        for (int i = 0; i < numberOfArchers; i++) {
            archersToDefendWith.add(archerRepository.findAllByPlayerAndInBattle(defenderPlayer, false).get(i));
            archerRepository.findAllByPlayerAndInBattle(defenderPlayer, false).get(i).setInBattle(true);
        }
        List<Swordsman> swordsmenToDefendWith = new ArrayList<>();
        for (int i = 0; i < numberOfSwordsmen; i++) {
            swordsmenToDefendWith.add(swordsmanRepository.findAllByPlayerAndInBattle(defenderPlayer, false).get(i));
            swordsmanRepository.findAllByPlayerAndInBattle(defenderPlayer, false).get(i).setInBattle(true);
        }
        List<Cavalry> cavalriesToDefendWith = new ArrayList<>();
        for (int i = 0; i < numberOfCavalries; i++) {
            cavalriesToDefendWith.add(cavalryRepository.findAllByPlayerAndInBattle(defenderPlayer, false).get(i));
            cavalryRepository.findAllByPlayerAndInBattle(defenderPlayer, false).get(i).setInBattle(true);
        }
        return battle(archersToDefendWith, swordsmenToDefendWith, cavalriesToDefendWith, attack, defenderPlayer);
//        int defenderArcherStrength = archersToDefendWith.size() * Archer.value;
//        int defenderSwordsmanStrength = swordsmenToDefendWith.size() * Swordsman.value;
//        int defenderCavalryStrength = cavalriesToDefendWith.size() * Cavalry.value;
//        int totalDefenderValue = defenderArcherStrength + defenderSwordsmanStrength + defenderCavalryStrength;
//        if (totalDefenderValue < calculateAttackStrength(attack)) {
//            return "redirect:/defend";
//            //return "defend";
//        }
//        int archersLeft = archersToDefendWith.size() - attack.getArchersUsed().size();
//        int swordsmenLeft = swordsmenToDefendWith.size() - attack.getSwordsmenUsed().size();
//        int cavalriesLeft = cavalriesToDefendWith.size() - attack.getCavalriesUsed().size();
//        int[] adjustedValues = adjustNumbers(archersLeft, swordsmenLeft, cavalriesLeft, Archer.value, Swordsman.value, Cavalry.value);
//        archersLeft = adjustedValues[0];
//        swordsmenLeft = adjustedValues[1];
//        cavalriesLeft = adjustedValues[2];
//        for (int i = 0; i < archersLeft; i++) {
//            archersToDefendWith.get(i).setInBattle(false);
//            //defenderPlayer.getArchers().add(archersToDefendWith.get(i));
//        }
//        for (int i = 0; i < swordsmenLeft; i++) {
//            swordsmenToDefendWith.get(i).setInBattle(false);
//            //defenderPlayer.getSwordsmen().add(swordsmenToDefendWith.get(i));
//        }
//        for (int i = 0; i < cavalriesLeft; i++) {
//            cavalriesToDefendWith.get(i).setInBattle(false);
//            //attack.getAttacker().getCavalries().add(attack.getCavalriesUsed().get(i));
//        }
//        Iterable<Archer> archersToDelete = attack.getArchersUsed();
//        Iterable<Swordsman> swordsmenToDelete = attack.getSwordsmenUsed();
//        Iterable<Cavalry> cavalriesToDelete = attack.getCavalriesUsed();
//        attackRepository.delete(attack);
//        deleteOnlyTheEntitiesOfAnAttackThatHasBeenCountered(archersToDelete, swordsmenToDelete, cavalriesToDelete);
//        defenderPlayer.setAttacked(false);
//        playerRepository.save(defenderPlayer);
//        playerRepository.save(attack.getAttacker());
//        return "redirect:/home";
    }

    public String battle(List<Archer> archersToDefendWith, List<Swordsman> swordsmenToDefendWith, List<Cavalry> cavalriesToDefendWith, Attack attack, Player defenderPlayer) {
        int defenderArcherStrength = archersToDefendWith.size() * Archer.value;
        int defenderSwordsmanStrength = swordsmenToDefendWith.size() * Swordsman.value;
        int defenderCavalryStrength = cavalriesToDefendWith.size() * Cavalry.value;
        int totalDefenderValue = defenderArcherStrength + defenderSwordsmanStrength + defenderCavalryStrength;
        if (totalDefenderValue < calculateAttackStrength(attack)) {
            return "redirect:/defend";
            //return "defend";
        }
        int archersLeft = archersToDefendWith.size() - attack.getArchersUsed().size();
        int swordsmenLeft = swordsmenToDefendWith.size() - attack.getSwordsmenUsed().size();
        int cavalriesLeft = cavalriesToDefendWith.size() - attack.getCavalriesUsed().size();
        int[] adjustedValues = adjustNumbers(archersLeft, swordsmenLeft, cavalriesLeft, Archer.value, Swordsman.value, Cavalry.value);
        archersLeft = adjustedValues[0];
        swordsmenLeft = adjustedValues[1];
        cavalriesLeft = adjustedValues[2];
        for (int i = 0; i < archersLeft; i++) {
            archersToDefendWith.get(i).setInBattle(false);
            //defenderPlayer.getArchers().add(archersToDefendWith.get(i));
        }
        for (int i = 0; i < swordsmenLeft; i++) {
            swordsmenToDefendWith.get(i).setInBattle(false);
            //defenderPlayer.getSwordsmen().add(swordsmenToDefendWith.get(i));
        }
        for (int i = 0; i < cavalriesLeft; i++) {
            cavalriesToDefendWith.get(i).setInBattle(false);
            //attack.getAttacker().getCavalries().add(attack.getCavalriesUsed().get(i));
        }
        Iterable<Archer> archersToDelete = attack.getArchersUsed();
        Iterable<Swordsman> swordsmenToDelete = attack.getSwordsmenUsed();
        Iterable<Cavalry> cavalriesToDelete = attack.getCavalriesUsed();
        attackRepository.delete(attack);
        transactionalService.deleteOnlyTheEntitiesOfAnAttackThatHasBeenCountered(archersToDelete, swordsmenToDelete, cavalriesToDelete);
        defenderPlayer.setAttacked(false);
        playerRepository.save(defenderPlayer);
        playerRepository.save(attack.getAttacker());
        return "redirect:/home";
    }
}
