package com.example.demo.Repositories;

import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArcherWorkshopRepository extends CrudRepository<ArcherWorkshop, Long> {
    List<ArcherWorkshop> findAllByPlayer(Player player);
}
