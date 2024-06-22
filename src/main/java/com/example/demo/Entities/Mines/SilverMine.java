package com.example.demo.Entities.Mines;

import com.example.demo.Entities.Materials.Silver;
import com.example.demo.Entities.Player;
import com.example.demo.Services.GoldMineService;
import com.example.demo.Services.SilverMineService;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
public class SilverMine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int value;

    @ElementCollection
    @CollectionTable(name = "silver_mine_last_access_dates", joinColumns = @JoinColumn(name = "silver_mine_id"))
    @MapKeyJoinColumn(name = "player_id")
    @Column(name = "last_access_date")
    private Map<Player, LocalDate> lastAccessDates = new HashMap<>();
    public SilverMine() {
    }

    public SilverMine(int value) {
        this.value = value;
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

    public void getMaterials(Player player, SilverMineService silverMineService) {
        silverMineService.getMaterials(this, player);
    }

    public boolean canPlayerGetMaterials(Player player, SilverMineService silverMineService) {
        return silverMineService.canPlayerGetMaterials(this, player);
    }

    public void setLastAccessDates(Map<Player, LocalDate> lastAccessDates) {
        this.lastAccessDates = lastAccessDates;
    }
}

