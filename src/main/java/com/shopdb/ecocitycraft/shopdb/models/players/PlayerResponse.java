package com.shopdb.ecocitycraft.shopdb.models.players;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class PlayerResponse {
    @NotNull
    private long id;

    @NotNull
    private String name;

    private Timestamp lastSeen;

    private Timestamp lastUpdated;

    private boolean active;

    private int numChestShops;

    private List<String> towns;

    public long getId() {
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

    public boolean isActive() {
        return active;
    }

    public int getNumChestShops() {
        return numChestShops;
    }

    public List<String> getTowns() {
        return towns;
    }

    public void setId(long id) {
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

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setNumChestShops(int numChestShops) {
        this.numChestShops = numChestShops;
    }

    public void setTowns(List<String> towns) {
        this.towns = towns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerResponse that = (PlayerResponse) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
