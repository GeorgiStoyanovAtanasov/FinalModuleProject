package com.example.demo.Entities.ChosenMines;

import com.example.demo.Entities.Mines.GoldMine;
import jakarta.persistence.*;

@Entity
public class ChosenGoldMineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private GoldMine currentGoldMine;

    public ChosenGoldMineEntity() {
    }

    public ChosenGoldMineEntity(Long id, GoldMine currentGoldMine) {
        this.id = id;
        this.currentGoldMine = currentGoldMine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GoldMine getCurrentGoldMine() {
        return currentGoldMine;
    }

    public void setCurrentGoldMine(GoldMine goldMine) {
        this.currentGoldMine = goldMine;
    }
}
