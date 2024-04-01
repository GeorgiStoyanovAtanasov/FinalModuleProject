package com.example.demo.Entities.Mines;

import com.example.demo.Entities.Player;
import com.example.demo.Services.CrystalMineService;
import com.example.demo.Services.GoldMineService;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
public class GoldMine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int value;

    @ElementCollection
    @CollectionTable(name = "goldmine_last_access_dates", joinColumns = @JoinColumn(name = "goldmine_id"))
    @MapKeyJoinColumn(name = "player_id")
    @Column(name = "last_access_date")
    private Map<Player, LocalDate> lastAccessDates = new HashMap<>();
    public GoldMine() {
    }
    public Map<Player, LocalDate> getLastAccessDates() {
        return lastAccessDates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean getMaterials(Player player, GoldMineService goldMineService) {
        return goldMineService.getMaterials(this, player);
    }

    public boolean canPlayerGetMaterials(Player player, GoldMineService goldMineService) {
        return goldMineService.canPlayerGetMaterials(this, player);
    }

    public void setLastAccessDates(Map<Player, LocalDate> lastAccessDates) {
        this.lastAccessDates = lastAccessDates;
    }
}

