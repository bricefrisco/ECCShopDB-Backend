package com.shopdb.ecocitycraft.shopdb.models.signs;

import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.SortBy;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class SignParams {
    @Min(1)
    private Integer page = 1;
    @Min(1) @Max(100)
    private Integer pageSize = 10;
    private String material;
    private Server server;
    private TradeType tradeType;
    private boolean filterIdenticalSigns = false;
    private boolean hideOutOfStock = false;
    private SortBy sortBy = SortBy.BEST_PRICE;
    private String regionName;

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public String getMaterial() {
        return material;
    }

    public Server getServer() {
        return server;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public boolean isFilterIdenticalSigns() {
        return filterIdenticalSigns;
    }

    public boolean isHideOutOfStock() {
        return hideOutOfStock;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public void setFilterIdenticalSigns(boolean filterIdenticalSigns) {
        this.filterIdenticalSigns = filterIdenticalSigns;
    }

    public void setHideOutOfStock(boolean hideOutOfStock) {
        this.hideOutOfStock = hideOutOfStock;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    @Override
    public String toString() {
        return "SignParams{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", material='" + material + '\'' +
                ", server=" + server +
                ", tradeType=" + tradeType +
                ", filterIdenticalSigns=" + filterIdenticalSigns +
                ", hideOutOfStock=" + hideOutOfStock +
                ", sortBy=" + sortBy +
                ", regionName='" + regionName + '\'' +
                '}';
    }
}
