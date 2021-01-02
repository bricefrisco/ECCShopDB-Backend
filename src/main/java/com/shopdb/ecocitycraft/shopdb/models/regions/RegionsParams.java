package com.shopdb.ecocitycraft.shopdb.models.regions;

import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class RegionsParams {

    @Min(1)
    private Integer page = 1;

    @Min(1) @Max(100)
    private Integer pageSize = 10;

    private Server server;

    private boolean active;

    private String name;

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Server getServer() {
        return server;
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RegionsParams{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", server=" + server +
                ", active=" + active +
                ", name='" + name + '\'' +
                '}';
    }
}
