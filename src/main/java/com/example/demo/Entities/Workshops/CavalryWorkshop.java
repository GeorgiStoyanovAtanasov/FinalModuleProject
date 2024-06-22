package com.example.demo.Entities.Workshops;
import com.example.demo.Entities.Player;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class CavalryWorkshop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Player player;
    private LocalDate lastAccessDate;

    public CavalryWorkshop() {
    }

    public CavalryWorkshop(Player player) {
        this.player = player;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(LocalDate lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}