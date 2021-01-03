package com.shopdb.ecocitycraft.shopdb.models.regions;

import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@ToString
public class RegionsParams {
    @Min(1)
    private Integer page = 1;
    @Min(1) @Max(100)
    private Integer pageSize = 10;
    private Server server;
    private boolean active;
    private String name;
}
