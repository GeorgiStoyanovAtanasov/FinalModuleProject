package com.example.demo.Repositories;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SwordsmanRepository extends CrudRepository<Swordsman, Long> {
    List<Swordsman> findAllByPlayer(Player player);
    List<Swordsman> findAllByPlayerAndInBattle(Player player, Boolean inBattle);
    void deleteAllByPlayer(Player player);
}
