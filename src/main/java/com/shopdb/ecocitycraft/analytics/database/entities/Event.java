package com.shopdb.ecocitycraft.analytics.database.entities;

import com.shopdb.ecocitycraft.analytics.models.enums.EventType;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    @Enumerated(EnumType.STRING)
    private Server server;

    private String material;
    private String name;
    private Integer page;
    private Timestamp timestamp;

    public Long getId() {
        return id;
    }

    public EventType getEventType() {
        return eventType;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public Server getServer() {
        return server;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public void setServer(Server server) {
        this.server = server;
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

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", eventType=" + eventType +
                ", tradeType=" + tradeType +
                ", server=" + server +
                ", material='" + material + '\'' +
                ", name='" + name + '\'' +
                ", page=" + page +
                ", timestamp=" + timestamp +
                '}';
    }
}
