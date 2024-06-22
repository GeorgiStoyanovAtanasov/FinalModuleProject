package com.example.demo.Repositories;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArcherRepository extends CrudRepository<Archer, Long> {
    List<Archer> findAllByPlayer(Player player);
    List<Archer> findAllByPlayerAndInBattle(Player player,Boolean inBattle);
    void deleteAllByPlayer(Player player);
}
