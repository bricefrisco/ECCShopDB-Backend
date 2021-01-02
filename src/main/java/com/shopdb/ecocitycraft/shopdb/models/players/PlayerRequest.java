package com.shopdb.ecocitycraft.shopdb.models.players;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

public class PlayerRequest {

    @NotBlank
    @Size(min = 3, max = 16, message = "Name must be between 3 and 16 characters")
    private String name;

    private Timestamp lastSeen;

    private Boolean active;

    public String getName() {
        return name;
    }

    public Timestamp getLastSeen() {
        return lastSeen;
    }

    public Boolean getActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastSeen(Timestamp lastSeen) {
        this.lastSeen = lastSeen;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "PlayerRequest{" +
                "name='" + name + '\'' +
                ", lastSeen=" + lastSeen +
                ", active=" + active +
                '}';
    }
}
