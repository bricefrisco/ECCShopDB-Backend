package com.shopdb.ecocitycraft.shopdb.models.signs;

import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import com.shopdb.ecocitycraft.shopdb.models.players.PlayerResponse;
import com.shopdb.ecocitycraft.shopdb.models.regions.RegionResponse;

import java.util.Objects;

public class ChestShopSignDto {
    private long id;
    private RegionResponse town;
    private Location location;
    private PlayerResponse owner;
    private int quantity;
    private int count;
    private double buyPrice;
    private double sellPrice;
    private String material;

    public long getId() {
        return id;
    }

    public RegionResponse getTown() {
        return town;
    }

    public Location getLocation() {
        return location;
    }

    public PlayerResponse getOwner() {
        return owner;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCount() {
        return count;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public String getMaterial() {
        return material;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTown(RegionResponse town) {
        this.town = town;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setOwner(PlayerResponse owner) {
        this.owner = owner;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return "ChestShopSignDto{" +
                "id=" + id +
                ", town=" + town +
                ", location=" + location +
                ", owner=" + owner +
                ", quantity=" + quantity +
                ", buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                ", material='" + material + '\'' +
                '}';
    }

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
