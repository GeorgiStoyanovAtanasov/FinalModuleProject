package com.example.demo.Entities.Mines;

import com.example.demo.Entities.Materials.Silver;
import com.example.demo.Entities.Player;
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

    public boolean getMaterials(Player player) {
        if (canPlayerGetMaterials(player)) {
            player.getSilver().setAmount(player.getSilver().getAmount() + value);
            return true;
        }
        return false;
    }

    public synchronized boolean canPlayerGetMaterials(Player player) {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastAccessDate = lastAccessDates.getOrDefault(player, null);

        if (lastAccessDate == null || !lastAccessDate.equals(currentDate)) {
            lastAccessDates.put(player, currentDate);
            return true;
        } else {
            return false;
        }
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

    public void setLastAccessDates(Map<Player, LocalDate> lastAccessDates) {
        this.lastAccessDates = lastAccessDates;
    }
}

