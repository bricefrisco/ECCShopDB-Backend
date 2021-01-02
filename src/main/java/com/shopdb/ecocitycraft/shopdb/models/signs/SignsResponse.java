package com.shopdb.ecocitycraft.shopdb.models.signs;

import com.shopdb.ecocitycraft.shopdb.models.regions.RegionResponse;

import javax.validation.constraints.NotNull;

public class SignsResponse {
    private long numChestShopSignsRemoved;
    private int numChestShopSignsUpdated;

    @NotNull
    private RegionResponse region;

    public SignsResponse(long numChestShopSignsRemoved, int numChestShopSignsUpdated, @NotNull RegionResponse region) {
        this.numChestShopSignsRemoved = numChestShopSignsRemoved;
        this.numChestShopSignsUpdated = numChestShopSignsUpdated;
        this.region = region;
    }

    public long getNumChestShopSignsRemoved() {
        return numChestShopSignsRemoved;
    }

    public int getNumChestShopSignsUpdated() {
        return numChestShopSignsUpdated;
    }

    public RegionResponse getRegion() {
        return region;
    }

    public void setNumChestShopSignsRemoved(long numChestShopSignsRemoved) {
        this.numChestShopSignsRemoved = numChestShopSignsRemoved;
    }

    public void setNumChestShopSignsUpdated(int numChestShopSignsUpdated) {
        this.numChestShopSignsUpdated = numChestShopSignsUpdated;
    }

    public void setRegion(RegionResponse region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "SignsResponse{" +
                "numChestShopSignsRemoved=" + numChestShopSignsRemoved +
                ", numChestShopSignsUpdated=" + numChestShopSignsUpdated +
                ", region=" + region +
                '}';
    }
}
