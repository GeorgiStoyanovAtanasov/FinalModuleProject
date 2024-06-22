package com.example.demo.Repositories;

import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.SwordsmanWorkshop;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SwordsmanWorkshopRepository extends CrudRepository<SwordsmanWorkshop, Long> {
    List<SwordsmanWorkshop> findAllByPlayer(Player player);
}
