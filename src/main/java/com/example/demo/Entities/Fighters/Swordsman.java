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
    @Column(columnDefinition = "integer default 3")
    public static final int value = 3;
    private boolean inBattle;

    public Swordsman() {
    }

    public Swordsman(Player player) {
        this.player = player;
        this.inBattle = false;
    }

    public int getValue() {
        return value;
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

    public boolean isInBattle() {
        return inBattle;
    }

    public void setInBattle(boolean inBattle) {
        this.inBattle = inBattle;
    }
}

