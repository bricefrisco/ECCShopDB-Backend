package com.shopdb.ecocitycraft.shopdb.models.regions;

import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class RegionResponse {
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private Server server;

    @NotNull
    private Location iBounds;

    @NotNull
    private Location oBounds;

    @NotNull
    private boolean active;

    private List<String> mayors;

    private int numChestShops;

    private Timestamp lastUpdated;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Server getServer() {
        return server;
    }

    public Location getiBounds() {
        return iBounds;
    }

    public Location getoBounds() {
        return oBounds;
    }

    public boolean isActive() {
        return active;
    }

    public List<String> getMayors() {
        return mayors;
    }

    public int getNumChestShops() {
        return numChestShops;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setiBounds(Location iBounds) {
        this.iBounds = iBounds;
    }

    public void setoBounds(Location oBounds) {
        this.oBounds = oBounds;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setMayors(List<String> mayors) {
        this.mayors = mayors;
    }

    public void setNumChestShops(int numChestShops) {
        this.numChestShops = numChestShops;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "RegionResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", server=" + server +
                ", iBounds=" + iBounds +
                ", oBounds=" + oBounds +
                ", mayors=" + mayors +
                ", numChestShops=" + numChestShops +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionResponse that = (RegionResponse) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
