package com.example.demo.Entities.Workshops;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Mines.GoldMine;
import com.example.demo.Entities.Player;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
public class ArcherWorkshop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Player player;
    private LocalDate lastAccessDate;

    public ArcherWorkshop() {
    }

    public ArcherWorkshop(Player player) {
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

//    public boolean addArcher(Player player, ArcherWorkshop archerWorkshop) {
//        if (canPlayerGetMaterials(archerWorkshop, player)) {
//            player.getArchers().add(new Archer(player));
//            return true;
//        }
//        return false;
//    }
//
//    public synchronized boolean canPlayerGetMaterials(ArcherWorkshop archerWorkshop, Player player) {
//        LocalDate currentDate = LocalDate.now();
//        LocalDate lastAccessDates = archerWorkshop.getLastAccessDate();
//        if (lastAccessDate == null || !lastAccessDate.equals(currentDate)) {
//           archerWorkshop.setLastAccessDate(currentDate);
//            return true;
//        } else {
//            return false;
//        }
//    }
}
