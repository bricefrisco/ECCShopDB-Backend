package com.shopdb.ecocitycraft.shopdb.models.players;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class PlayerResponse {
    @NotNull
    private long id;

    @NotNull
    private String name;
    private Timestamp lastSeen;
    private Timestamp lastUpdated;
    private boolean active;
    private int numChestShops;
    private List<String> towns;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerResponse that = (PlayerResponse) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
