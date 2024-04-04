package com.example.demo.Repositories;

import com.example.demo.Entities.Attack;
import com.example.demo.Entities.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttackRepository extends CrudRepository<Attack, Long> {
    Attack findByDefender(Player player);
    List<Attack> findAllByDefender(Player player);
}
