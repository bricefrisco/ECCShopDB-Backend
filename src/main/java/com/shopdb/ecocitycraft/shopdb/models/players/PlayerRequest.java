package com.shopdb.ecocitycraft.shopdb.models.players;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Getter
@Setter
public class PlayerRequest {
    @NotBlank
    @Size(min = 3, max = 16, message = "Name must be between 3 and 16 characters")
    private String name;
    private Timestamp lastSeen;
    private Boolean active;
}
