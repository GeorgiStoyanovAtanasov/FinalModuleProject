package com.example.demo.Entities.ChosenMines;

import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Mines.SilverMine;
import jakarta.persistence.*;

@Entity
public class ChosenSilverMineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private SilverMine currentSilverMine;

    public ChosenSilverMineEntity() {
    }

    public ChosenSilverMineEntity(Long id, SilverMine currentSilverMine) {
        this.id = id;
        this.currentSilverMine = currentSilverMine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SilverMine getCurrentSilverMine() {
        return currentSilverMine;
    }

    public void setCurrentSilverMine(SilverMine currentSilverMine) {
        this.currentSilverMine = currentSilverMine;
    }
}
