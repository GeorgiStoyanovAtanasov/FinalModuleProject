package com.example.demo.Repositories;

import com.example.demo.Entities.ChosenMines.ChosenGoldMineEntity;
import com.example.demo.Entities.ChosenMines.ChosenSilverMineEntity;
import org.springframework.data.repository.CrudRepository;

public interface ChosenSilverMineRepository extends CrudRepository<ChosenSilverMineEntity, Long> {
}
