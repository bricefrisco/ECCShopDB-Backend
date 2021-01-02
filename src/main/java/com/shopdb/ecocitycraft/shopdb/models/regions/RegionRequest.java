package com.shopdb.ecocitycraft.shopdb.models.regions;

import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class RegionRequest {

    @NotBlank
    private String name;

    @NotNull
    private Server server;

    @NotNull
    private Location iBounds;

    @NotNull
    private Location oBounds;

    @NotNull
    private List<String> mayorNames;

    private boolean active;

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

    public List<String> getMayorNames() {
        return mayorNames;
    }

    public boolean isActive() {
        return active;
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

    public void setMayorNames(List<String> mayorNames) {
        this.mayorNames = mayorNames;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "RegionRequest{" +
                "name='" + name + '\'' +
                ", server=" + server +
                ", iBounds=" + iBounds +
                ", oBounds=" + oBounds +
                ", mayorNames=" + mayorNames +
                ", active=" + active +
                '}';
    }
}
