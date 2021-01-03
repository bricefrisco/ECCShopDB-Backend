package com.shopdb.ecocitycraft.analytics.database.entities;

import com.shopdb.ecocitycraft.analytics.models.enums.EventType;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
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
