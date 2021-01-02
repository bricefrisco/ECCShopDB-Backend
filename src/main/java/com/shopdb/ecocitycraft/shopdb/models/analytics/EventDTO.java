package com.shopdb.ecocitycraft.shopdb.models.analytics;

import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class EventDTO {
    @NotNull
    private EventType eventType;

    private String material;
    private String name;
    private Integer page;
    private TradeType tradeType;
    private Server server;
    private Timestamp timestamp;

    public EventType getEventType() {
        return eventType;
    }

    public String getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public Integer getPage() {
        return page;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public Server getServer() {
        return server;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventType=" + eventType +
                ", material='" + material + '\'' +
                ", name='" + name + '\'' +
                ", page=" + page +
                ", tradeType=" + tradeType +
                ", server=" + server +
                ", timestamp=" + timestamp +
                '}';
    }
}
