package com.example.demo.Entities.Workshops;

import com.example.demo.Entities.Player;
import com.example.demo.Entities.Fighters.Swordsman;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class SwordsmanWorkshop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Player player;

    private LocalDate lastAccessDate;

    public SwordsmanWorkshop() {
    }

    public SwordsmanWorkshop(LocalDate lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
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

    public void makeArcher(List<Swordsman> swordsmanList) {
        LocalDate currentDate = LocalDate.now();
        if (lastAccessDate == null || !lastAccessDate.equals(currentDate)) {
            swordsmanList.add(new Swordsman());
            lastAccessDate = currentDate;
        }
    }
}