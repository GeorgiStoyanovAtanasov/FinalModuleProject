package com.example.demo.Entities.ChosenMines;

import com.example.demo.Entities.Materials.Crystal;
import com.example.demo.Entities.Mines.CrystalMine;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Mines.SilverMine;
import jakarta.persistence.*;

@Entity
public class ChosenCrystalMineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private CrystalMine currentCrystalMine;

    public ChosenCrystalMineEntity() {
    }

    public ChosenCrystalMineEntity(Long id, CrystalMine currentCrystalMine) {
        this.id = id;
        this.currentCrystalMine = currentCrystalMine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CrystalMine getCurrentCrystalMine() {
        return currentCrystalMine;
    }

    public void setCurrentCrystalMine(CrystalMine currentCrystalMine) {
        this.currentCrystalMine = currentCrystalMine;
    }
}
