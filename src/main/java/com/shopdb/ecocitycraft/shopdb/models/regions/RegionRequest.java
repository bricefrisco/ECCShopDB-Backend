package com.shopdb.ecocitycraft.shopdb.models.regions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class RegionRequest {
    private String name;
    private String server;
    @JsonProperty("iBounds") // override lombok getters/setters, needed for JSON parsing.
    private Location iBounds;
    @JsonProperty("oBounds")
    private Location oBounds;
    @JsonProperty("owners")
    private Set<String> mayorNames;
    private Boolean active = false;

    public Location getiBounds() {
        return iBounds;
    }

    public Location getoBounds() {
        return oBounds;
    }
}
