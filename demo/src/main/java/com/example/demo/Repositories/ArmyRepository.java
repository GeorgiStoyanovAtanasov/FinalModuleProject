package com.example.demo.Repositories;

import com.example.demo.Entities.Army;
import org.springframework.data.repository.CrudRepository;

public interface ArmyRepository extends CrudRepository <Army, Long   > {
}
