package com.shopdb.ecocitycraft.shopdb.database.entities.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SortBy {
    @JsonProperty("best-price")
    BEST_PRICE,
    @JsonProperty("quantity")
    QUANTITY,
    @JsonProperty("material")
    MATERIAL,
    @JsonProperty("quantity-available")
    QUANTITY_AVAILABLE
}
