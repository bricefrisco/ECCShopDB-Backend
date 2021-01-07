package com.shopdb.ecocitycraft.shopdb.models.signs.ecc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ShopEvent {
    private String id; // Serialized value of X,Y,Z coordinates to uniquely identify this chest shop
    private EventType eventType;
    private String world;
    private Integer x;
    private Integer y;
    private Integer z;
    private String owner;
    private Integer quantity;
    private Integer count;
    private BigDecimal buyPrice;
    private BigDecimal sellPrice;
    private String item;
    private Boolean full;
}
