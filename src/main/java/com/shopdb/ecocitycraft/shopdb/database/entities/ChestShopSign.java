package com.shopdb.ecocitycraft.shopdb.database.entities;

import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;

import javax.persistence.*;

@Entity
public class ChestShopSign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Server server;

    @ManyToOne
    private Region town;

    @Embedded
    private Location location;

    @ManyToOne
    private Player owner;

    private Integer quantity;
    private Integer count;
    private Double buyPrice;
    private Double sellPrice;
    private Double buyPriceEach;
    private Double sellPriceEach;
    private String material;
    private Boolean isDistinct;
    private Boolean isBuySign;
    private Boolean isSellSign;

    public Long getId() {
        return id;
    }

    public Server getServer() {
        return server;
    }

    public Region getTown() {
        return town;
    }

    public Location getLocation() {
        return location;
    }

    public Player getOwner() {
        return owner;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getCount() {
        return count;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public Double getBuyPriceEach() {
        return buyPriceEach;
    }

    public Double getSellPriceEach() {
        return sellPriceEach;
    }

    public String getMaterial() {
        return material;
    }

    public Boolean getDistinct() {
        return isDistinct;
    }

    public Boolean getBuySign() {
        return isBuySign;
    }

    public Boolean getSellSign() {
        return isSellSign;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setTown(Region town) {
        this.town = town;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void setBuyPriceEach(Double buyPriceEach) {
        this.buyPriceEach = buyPriceEach;
    }

    public void setSellPriceEach(Double sellPriceEach) {
        this.sellPriceEach = sellPriceEach;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setDistinct(Boolean distinct) {
        isDistinct = distinct;
    }

    public void setBuySign(Boolean buySign) {
        isBuySign = buySign;
    }

    public void setSellSign(Boolean sellSign) {
        isSellSign = sellSign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChestShopSign that = (ChestShopSign) o;
        return quantity.equals(that.quantity) &&
                equals(buyPrice, that.buyPrice) &&
                equals(sellPrice, that.sellPrice) &&
                town.equals(that.town) &&
                owner.equals(that.owner) &&
                material.equals(that.material);
    }

    private boolean equals(Double a, Double b) {
        if (a == null) {
            return b == null;
        }

        return a.equals(b);
    }

    @Override
    public String toString() {
        return "ChestShopSign{" +
                "id=" + id +
                ", server=" + server +
                ", town=" + town +
                ", location=" + location +
                ", owner=" + owner +
                ", quantity=" + quantity +
                ", buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                ", material='" + material + '\'' +
                ", isDistinct=" + isDistinct +
                ", isBuySign=" + isBuySign +
                ", isSellSign=" + isSellSign +
                '}';
    }
}
