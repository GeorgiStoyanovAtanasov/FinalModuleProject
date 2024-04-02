package com.example.demo.Services;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Cavalry;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArmyService {
    private List<Player> player;
    private static List<Archer> archers;
    private static List<Swordsman> swordsmen;
    private static List<Cavalry> cavalries;
    @Autowired
    private PlayerRepository playerRepository;

    private static int applyAsInt(List<Archer> value) {
        int i = new Integer(String.valueOf(value));
        return i;
    }

    public static Object getArmySize() {
        List<List<Archer>> archerSizes = new ArrayList<>();
        archerSizes.add(archers);
        List<List<Swordsman>> swordsmenSizes = new ArrayList<>();
        swordsmenSizes.add(swordsmen);
        List<List<Cavalry>> cavalriesSizes = new ArrayList<>();
        cavalriesSizes.add(cavalries);

//        armySizes.add(swordsmen);
//        armySizes.add(cavalries);
        int totalArmySize = archerSizes.stream().mapToInt(ArmyService::applyAsInt).sum();
        return totalArmySize;
//        return null;
    }
    public int calculateTotalArmySize(List<Integer> archerSizes) {
        return archerSizes.stream().mapToInt(Integer::intValue).sum();
    }

    public Player getPlayer1Army() {
    }

    public Player getPlayer2Army() {
        return null;
    }
}
