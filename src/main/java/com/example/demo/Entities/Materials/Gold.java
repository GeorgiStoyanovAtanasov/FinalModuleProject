package com.example.demo.Entities.Materials;

import com.example.demo.Entities.Player;
import jakarta.persistence.*;

@Entity
public class Gold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Player player;
    private int amount;
    public Gold(){
    }
    public Gold(Player player){
        this.player = player;
        this.amount = 0;
    }

    public Gold(int amount, Player player) {
        this.amount = amount;
        this.player = player;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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
