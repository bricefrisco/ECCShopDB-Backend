package com.shopdb.ecocitycraft.analytics.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EventType {
    @JsonProperty("chest-shops")
    CHEST_SHOPS,
    @JsonProperty("regions")
    REGIONS,
    @JsonProperty("region")
    REGION,
    @JsonProperty("region-players")
    REGION_PLAYERS,
    @JsonProperty("region-chest-shops")
    REGION_CHEST_SHOPS,
    @JsonProperty("players")
    PLAYERS,
    @JsonProperty("player")
    PLAYER,
    @JsonProperty("player-regions")
    PLAYER_REGIONS,
    @JsonProperty("player-chest-shops")
    PLAYER_CHEST_SHOPS
}
