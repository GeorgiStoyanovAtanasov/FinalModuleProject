package com.example.demo.Repositories;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Cavalry;
import com.example.demo.Entities.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CavalryRepository extends CrudRepository<Cavalry, Long> {
    List<Cavalry> findAllByPlayer(Player player);
    List<Cavalry> findAllByPlayerAndInBattle(Player player, Boolean inBattle);
    void deleteAllByPlayer(Player player);

}
