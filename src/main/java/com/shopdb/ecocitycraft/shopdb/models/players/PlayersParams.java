package com.shopdb.ecocitycraft.shopdb.models.players;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@ToString
public class PlayersParams {
    @Min(1)
    private Integer page = 1;
    @Min(1) @Max(100)
    private Integer pageSize = 10;
    private String name;
    private boolean active;
}
