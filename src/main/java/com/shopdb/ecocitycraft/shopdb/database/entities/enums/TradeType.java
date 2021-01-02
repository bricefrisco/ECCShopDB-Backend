package com.shopdb.ecocitycraft.shopdb.database.entities.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TradeType {
    @JsonProperty("buy")
    BUY,
    @JsonProperty("sell")
    SELL
}
