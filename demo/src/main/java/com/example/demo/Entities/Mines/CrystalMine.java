package com.example.demo.Entities.Mines;

import com.example.demo.Entities.Player;
import com.example.demo.Services.CrystalMineService;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
public class CrystalMine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int value;

    @ElementCollection
    @CollectionTable(name = "crystal_mine_last_access_dates", joinColumns = @JoinColumn(name = "crystal_mine_id"))
    @MapKeyJoinColumn(name = "player_id")
    @Column(name = "last_access_date")
    private Map<Player, LocalDate> lastAccessDates = new HashMap<>();
    public CrystalMine() {
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

    public void setLastAccessDates(Map<Player, LocalDate> lastAccessDates) {
        this.lastAccessDates = lastAccessDates;
    }
    public void getMaterials(Player player, CrystalMineService crystalMineService) {
        crystalMineService.getMaterials(this, player);
    }

    public boolean canPlayerGetMaterials(Player player, CrystalMineService crystalMineService) {
        return crystalMineService.canPlayerGetMaterials(this, player);
    }

}

