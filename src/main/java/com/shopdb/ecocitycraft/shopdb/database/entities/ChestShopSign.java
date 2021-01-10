package com.shopdb.ecocitycraft.shopdb.database.entities;

import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ChestShopSign {

    @Id
    private String id;

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
    private Boolean isFull;
    private Boolean isHidden;
    private Boolean isBuySign;
    private Boolean isSellSign;

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
                ", isBuySign=" + isBuySign +
                ", isSellSign=" + isSellSign +
                '}';
    }
}
