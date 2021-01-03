package com.shopdb.ecocitycraft.analytics.models;

import com.shopdb.ecocitycraft.analytics.models.enums.EventType;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
public class EventDTO {
    @NotNull
    private EventType eventType;
    private String material;
    private String name;
    private Integer page;
    private TradeType tradeType;
    private Server server;
    private Timestamp timestamp;
}
