package com.shopdb.ecocitycraft.shopdb.models.regions;

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
    private Location iBounds;
    @NotNull
    private Location oBounds;
    @NotNull
    private List<String> mayorNames;
    private boolean active;

    public void setiBounds(Location iBounds) {
        this.iBounds = iBounds;
    }

    public void setoBounds(Location oBounds) {
        this.oBounds = oBounds;
    }

    public Location getiBounds() {
        return iBounds;
    }

    public Location getoBounds() {
        return oBounds;
    }
}
