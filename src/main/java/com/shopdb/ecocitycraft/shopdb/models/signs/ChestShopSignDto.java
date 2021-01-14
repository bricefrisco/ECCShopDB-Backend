package com.shopdb.ecocitycraft.shopdb.models.signs;

import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import com.shopdb.ecocitycraft.shopdb.models.players.PlayerResponse;
import com.shopdb.ecocitycraft.shopdb.models.regions.RegionResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
public class ChestShopSignDto {
    private String id;
    private RegionResponse town;
    private Location location;
    private PlayerResponse owner;
    private int quantity;
    private int count;
    private double buyPrice;
    private double sellPrice;
    private boolean isFull;
    private String material;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChestShopSignDto that = (ChestShopSignDto) o;
        return quantity == that.quantity &&
                Double.compare(that.buyPrice, buyPrice) == 0 &&
                Double.compare(that.sellPrice, sellPrice) == 0 &&
                town.equals(that.town) &&
                owner.equals(that.owner) &&
                material.equals(that.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(town, owner, quantity, buyPrice, sellPrice, material);
    }
}
