package com.example.demo.Entities;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Cavalry;
import com.example.demo.Entities.Fighters.Swordsman;
import jakarta.persistence.*;

@Entity
public class Army {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "archer_id")
    Archer archer;
    @ManyToOne
    @JoinColumn(name = "swordsman_id")
    Swordsman swordsman;
    @ManyToOne
    @JoinColumn(name = "cavalry_id")
    Cavalry cavalry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cavalry getCavalry() {
        return cavalry;
    }

    public void setCavalry(Cavalry cavalry) {
        this.cavalry = cavalry;
    }

    public Swordsman getSwordsman() {
        return swordsman;
    }

    public void setSwordsman(Swordsman swordsman) {
        this.swordsman = swordsman;
    }

    public Archer getArcher() {
        return archer;
    }

    public void setArcher(Archer archer) {
        this.archer = archer;
    }

}
