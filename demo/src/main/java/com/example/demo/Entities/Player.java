package com.example.demo.Entities;

import com.example.demo.Constants.Role;
import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Cavalry;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Materials.Crystal;
import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Materials.Silver;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Entities.Workshops.CavalryWorkshop;
import com.example.demo.Entities.Workshops.SwordsmanWorkshop;
import jakarta.persistence.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<Cavalry> cavalries;
    @OneToMany(mappedBy = "player")
    private List<ArcherWorkshop> archerWorkshops;

    @OneToMany(mappedBy = "player")
    private List<SwordsmanWorkshop> swordsmanWorkshops;
    @OneToMany(mappedBy = "player")
    private List<CavalryWorkshop> cavalryWorkshops;

    @OneToOne(mappedBy = "player")
    private Gold gold;

    @OneToOne(mappedBy = "player")
    private Silver silver;

    @OneToOne(mappedBy = "player")
    private Crystal crystal;
    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isAttacked;
    @ElementCollection
    @CollectionTable(name = "player_last_accessed_gold_mines",
            joinColumns = @JoinColumn(name = "player_id"))
    @MapKeyColumn(name = "date")
    @Column(name = "gold_mine_id")
    private Map<LocalDate, Long> lastAccessedGoldMines = new HashMap<>();
    @ElementCollection
    @CollectionTable(name = "player_last_accessed_silver_mines",
            joinColumns = @JoinColumn(name = "player_id"))
    @MapKeyColumn(name = "date")
    @Column(name = "silver_mine_id")
    private Map<LocalDate, Long> lastAccessedSilverMines = new HashMap<>();
    @ElementCollection
    @CollectionTable(name = "player_last_accessed_crystal_mines",
            joinColumns = @JoinColumn(name = "player_id"))
    @MapKeyColumn(name = "date")
    @Column(name = "crystal_mine_id")
    private Map<LocalDate, Long> lastAccessedCrystalMines = new HashMap<>();

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

    public List<CavalryWorkshop> getCavalryWorkshops() {
        return cavalryWorkshops;
    }

    public void setCavalryWorkshops(List<CavalryWorkshop> cavalryWorkshops) {
        this.cavalryWorkshops = cavalryWorkshops;
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

    public List<Cavalry> getCavalries() {
        return cavalries;
    }

    public void setCavalries(List<Cavalry> cavalries) {
        this.cavalries = cavalries;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Map<LocalDate, Long> getLastAccessedGoldMines() {
        return lastAccessedGoldMines;
    }

    public void setLastAccessedGoldMines(Map<LocalDate, Long> lastAccessedGoldMines) {
        this.lastAccessedGoldMines = lastAccessedGoldMines;
    }
    public Map<LocalDate, Long> getLastAccessedSilverMines() {
        return lastAccessedSilverMines;
    }

    public void setLastAccessedSilverMines(Map<LocalDate, Long> lastAccessedSilverMines) {
        this.lastAccessedSilverMines = lastAccessedSilverMines;
    }
    public Map<LocalDate, Long> getLastAccessedCrystalMines() {
        return lastAccessedCrystalMines;
    }

    public void setLastAccessedCrystalMines(Map<LocalDate, Long> lastAccessedCrystalMines) {
        this.lastAccessedCrystalMines = lastAccessedCrystalMines;
    }
}
