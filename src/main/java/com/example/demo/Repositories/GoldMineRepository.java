package com.example.demo.Repositories;

import com.example.demo.Entities.Mines.GoldMine;
import org.springframework.data.repository.CrudRepository;

public interface GoldMineRepository extends CrudRepository<GoldMine, Long> {
}
