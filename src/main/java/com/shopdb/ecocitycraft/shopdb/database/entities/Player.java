package com.shopdb.ecocitycraft.shopdb.database.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 3, max = 16)
    private String name;

    private Timestamp lastSeen;

    private Timestamp lastUpdated;

    @ManyToMany(mappedBy = "mayors")
    private List<Region> towns;

    @OneToMany(mappedBy = "owner")
    private List<ChestShopSign> chestShopSigns;

    private Boolean active;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getLastSeen() {
        return lastSeen;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public List<Region> getTowns() {
        return towns;
    }

    public List<ChestShopSign> getChestShopSigns() {
        return chestShopSigns;
    }

    public Boolean getActive() {
        return active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastSeen(Timestamp lastSeen) {
        this.lastSeen = lastSeen;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setTowns(List<Region> towns) {
        this.towns = towns;
    }

    public void setChestShopSigns(List<ChestShopSign> chestShopSigns) {
        this.chestShopSigns = chestShopSigns;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastSeen=" + lastSeen +
                ", lastUpdated=" + lastUpdated +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player that = (Player) o;
        return id.equals(that.id);
    }
}
