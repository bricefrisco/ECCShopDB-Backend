package com.shopdb.ecocitycraft.shopdb.models.signs;

import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class Sign {
    @NotNull
    private Location location;
    @NotNull
    private String nameLine;
    @NotNull
    private String quantityLine;
    @NotNull
    private String priceLine;
    @NotNull
    private String materialLine;
}
