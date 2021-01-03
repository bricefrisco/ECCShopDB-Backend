package com.shopdb.ecocitycraft.shopdb.models.regions;

import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
public class RegionResponse {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private Server server;
    @NotNull
    private Location iBounds;
    @NotNull
    private Location oBounds;
    @NotNull
    private boolean active;
    private List<String> mayors;
    private int numChestShops;
    private Timestamp lastUpdated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionResponse that = (RegionResponse) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
