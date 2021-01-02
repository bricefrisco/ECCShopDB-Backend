package com.shopdb.ecocitycraft.shopdb.models.signs;

import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;

import javax.validation.constraints.NotNull;
import java.util.List;

public class SignsRequest {
    @NotNull
    private Server server;

    @NotNull
    private String regionName;

    @NotNull
    private List<Sign> signs;

    public Server getServer() {
        return server;
    }

    public String getRegionName() {
        return regionName;
    }

    public List<Sign> getSigns() {
        return signs;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public void setSigns(List<Sign> signs) {
        this.signs = signs;
    }

    @Override
    public String toString() {
        return "SignsRequest{" +
                "server=" + server +
                ", regionName='" + regionName + '\'' +
                ", signs=" + signs +
                '}';
    }
}
