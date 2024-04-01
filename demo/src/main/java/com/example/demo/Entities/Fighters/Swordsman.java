package com.example.demo.Entities.Fighters;

import com.example.demo.Entities.Player;
import jakarta.persistence.*;

@Entity
public class Swordsman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Player player;

    private int value;

    public Swordsman() {
    }

    public Swordsman(Player player) {
        this.player = player;
        this.value = 3;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

