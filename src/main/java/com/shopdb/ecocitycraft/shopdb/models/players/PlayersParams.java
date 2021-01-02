package com.shopdb.ecocitycraft.shopdb.models.players;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class PlayersParams {

    @Min(1)
    private Integer page = 1;

    @Min(1) @Max(100)
    private Integer pageSize = 10;

    private String name;

    private boolean active;

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "PlayersParams{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}
