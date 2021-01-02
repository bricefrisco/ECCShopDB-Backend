package com.shopdb.ecocitycraft.shopdb.database.entities.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Server {
    @JsonProperty("main")
    MAIN,
    @JsonProperty("main-north")
    MAIN_NORTH,
    @JsonProperty("main-east")
    MAIN_EAST
}
