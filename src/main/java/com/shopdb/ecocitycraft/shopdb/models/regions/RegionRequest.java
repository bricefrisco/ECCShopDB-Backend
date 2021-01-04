package com.shopdb.ecocitycraft.shopdb.models.regions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class RegionRequest {
    @NotBlank
    private String name;
    @NotNull
    private Server server;
    @NotNull
    @JsonProperty("iBounds")
    private Location iBounds;
    @NotNull
    @JsonProperty("oBounds")
    private Location oBounds;
    @NotNull
    private List<String> mayorNames;
    private Boolean active;
}
