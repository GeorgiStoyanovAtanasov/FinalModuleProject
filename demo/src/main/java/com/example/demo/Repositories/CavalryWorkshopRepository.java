package com.example.demo.Repositories;

import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.CavalryWorkshop;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CavalryWorkshopRepository extends CrudRepository<CavalryWorkshop, Long> {
    List<CavalryWorkshop> findAllByPlayer(Player player);
}
