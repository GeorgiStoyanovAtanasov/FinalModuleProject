package com.example.demo.Entities;

import com.example.demo.Constants.Role;
import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Materials.Crystal;
import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Materials.Silver;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Entities.Workshops.SwordsmanWorkshop;
import jakarta.persistence.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @OneToMany(mappedBy = "player")
    private List<Archer> archers;

    @OneToMany(mappedBy = "player")
    private List<Swordsman> swordsmen;

    @OneToMany(mappedBy = "player")
    private List<ArcherWorkshop> archerWorkshops;

    @OneToMany(mappedBy = "player")
    private List<SwordsmanWorkshop> swordsmanWorkshops;

    @OneToOne(mappedBy = "player")
    private Gold gold;

    @OneToOne(mappedBy = "player")
    private Silver silver;

    @OneToOne(mappedBy = "player")
    private Crystal crystal;
    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isAttacked;
    public Player() {
    }

    public Player(String username, String password, Gold gold, Silver silver, Crystal crystal, Role role) {
        this.username = username;
        this.password = password;
        this.gold = gold;
        this.silver = silver;
        this.crystal = crystal;
        this.role = role;
    }
    //private Player attackUser(Player user1, Player user2){
    //    //method body
    //}

    public Player(String username, String password, List<ArcherWorkshop> archerWorkshops, List<SwordsmanWorkshop> swordsmanWorkshops, Gold gold, Silver silver, Crystal crystal, boolean isAttacked, List<Archer> archer, List<Swordsman> swordsmen, Role role) {
        this.username = username;
        this.password = password;
        this.archerWorkshops = archerWorkshops;
        this.swordsmanWorkshops = swordsmanWorkshops;
        this.gold = gold;
        this.silver = silver;
        this.crystal = crystal;
        this.isAttacked = isAttacked;
        this.swordsmen = swordsmen;
        this.archers = archer;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ArcherWorkshop> getArcherWorkshops() {
        return archerWorkshops;
    }

    public void setArcherWorkshops(List<ArcherWorkshop> archerWorkshop) {
        this.archerWorkshops = archerWorkshop;
    }

    public List<SwordsmanWorkshop> getSwordsmanWorkshops() {
        return swordsmanWorkshops;
    }

    public void setSwordsmanWorkshops(List<SwordsmanWorkshop> swordsmanWorkshop) {
        this.swordsmanWorkshops = swordsmanWorkshop;
    }

    public Gold getGold() {
        return gold;
    }

    public void setGold(Gold gold) {
        this.gold = gold;
    }

    public Silver getSilver() {
        return silver;
    }

    public void setSilver(Silver silver) {
        this.silver = silver;
    }

    public Crystal getCrystal() {
        return crystal;
    }

    public void setCrystal(Crystal crystal) {
        this.crystal = crystal;
    }

    public boolean isAttacked() {
        return isAttacked;
    }

    public void setAttacked(boolean attacked) {
        isAttacked = attacked;
    }

    public List<Archer> getArchers() {
        return archers;
    }

    public void setArchers(List<Archer> archers) {
        this.archers = archers;
    }

    public List<Swordsman> getSwordsmen() {
        return swordsmen;
    }

    public void setSwordsmen(List<Swordsman> swordsmen) {
        this.swordsmen = swordsmen;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
