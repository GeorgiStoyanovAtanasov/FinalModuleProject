package com.example.demo.Entities;

import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Cavalry;
import com.example.demo.Entities.Fighters.Swordsman;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Attack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Player attacker;

    @ManyToMany
    private List<Archer> archersUsed;

    @ManyToMany
    private List<Swordsman> swordsmenUsed;

    @ManyToMany
    private List<Cavalry> cavalriesUsed;

    @ManyToOne
    private Player defender;

    public Attack() {
    }

    public Attack(Player attacker, List<Archer> archersUsed, List<Swordsman> swordsmenUsed, List<Cavalry> cavalriesUsed, Player defender) {
        this.attacker = attacker;
        this.archersUsed = archersUsed;
        this.swordsmenUsed = swordsmenUsed;
        this.cavalriesUsed = cavalriesUsed;
        this.defender = defender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getAttacker() {
        return attacker;
    }

    public void setAttacker(Player attacker) {
        this.attacker = attacker;
    }

    public List<Archer> getArchersUsed() {
        return archersUsed;
    }

    public void setArchersUsed(List<Archer> archersUsed) {
        this.archersUsed = archersUsed;
    }

    public List<Swordsman> getSwordsmenUsed() {
        return swordsmenUsed;
    }

    public void setSwordsmenUsed(List<Swordsman> swordsmenUsed) {
        this.swordsmenUsed = swordsmenUsed;
    }

    public List<Cavalry> getCavalriesUsed() {
        return cavalriesUsed;
    }

    public void setCavalriesUsed(List<Cavalry> cavalriesUsed) {
        this.cavalriesUsed = cavalriesUsed;
    }

    public Player getDefender() {
        return defender;
    }

    public void setDefender(Player defender) {
        this.defender = defender;
    }
}
