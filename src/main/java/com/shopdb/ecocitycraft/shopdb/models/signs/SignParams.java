package com.shopdb.ecocitycraft.shopdb.models.signs;

import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.SortBy;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@ToString
public class SignParams {
    @Min(1)
    private Integer page = 1;
    @Min(1) @Max(100)
    private Integer pageSize = 10;
    private String material;
    private Server server;
    private TradeType tradeType;
    private boolean hideOutOfStock = false;
    private SortBy sortBy = SortBy.BEST_PRICE;
    private String regionName;
    private String playerName;
    private boolean hideFull = false;
    private boolean distinct = false;
}
