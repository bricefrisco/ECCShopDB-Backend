package com.shopdb.ecocitycraft.shopdb.models.signs;

import com.shopdb.ecocitycraft.shopdb.models.regions.RegionResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
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
}
