package com.example.demo.Repositories;

import com.example.demo.Entities.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {
    public Player findByUsername(String username);

    boolean existsByUsername(String username);
}
