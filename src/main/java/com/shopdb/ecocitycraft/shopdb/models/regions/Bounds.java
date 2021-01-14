package com.shopdb.ecocitycraft.shopdb.models.regions;

import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bounds {
    private Location lowerBounds;
    private Location upperBounds;
}
