package com.example.demo.Services;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Cavalry;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.ArcherRepository;
import com.example.demo.Repositories.CavalryRepository;
import com.example.demo.Repositories.SwordsmanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionalService {
    @Autowired
    ArcherRepository archerRepository;
    @Autowired
    SwordsmanRepository swordsmanRepository;
    @Autowired
    CavalryRepository cavalryRepository;
    @Transactional
    public void deletePlayerEntities(Player player) {
        archerRepository.deleteAllByPlayer(player);
        swordsmanRepository.deleteAllByPlayer(player);
        cavalryRepository.deleteAllByPlayer(player);
    }

    @Transactional
    public void deleteOnlyTheEntitiesOfAnAttackThatHasBeenCountered(Iterable<Archer> archers, Iterable<Swordsman> swordsmen, Iterable<Cavalry> cavalries) {
        archerRepository.deleteAll(archers);
        swordsmanRepository.deleteAll(swordsmen);
        cavalryRepository.deleteAll(cavalries);
    }
}
