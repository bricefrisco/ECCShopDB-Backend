package com.shopdb.ecocitycraft.analytics.database.repositories;

import com.shopdb.ecocitycraft.analytics.database.entities.Event;
import com.shopdb.ecocitycraft.analytics.models.enums.EventType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class EventSpecification {

    public static Specification<Event> regionEvents() {
        return (event, query, builder) -> {
            List<Predicate> conditions = new ArrayList<>();

            conditions.add(builder.equal(event.get("eventType"), EventType.REGION));
            conditions.add(builder.equal(event.get("eventType"), EventType.REGION_PLAYERS));
            conditions.add(builder.equal(event.get("eventType"), EventType.REGION_CHEST_SHOPS));

            return builder.or(conditions.toArray(new Predicate[0]));
        };
    }

    public static Specification<Event> playerEvents() {
        return (event, query, builder) -> {
            List<Predicate> conditions = new ArrayList<>();

            conditions.add(builder.equal(event.get("eventType"), EventType.PLAYER));
            conditions.add(builder.equal(event.get("eventType"), EventType.PLAYER_REGIONS));
            conditions.add(builder.equal(event.get("eventType"), EventType.PLAYER_CHEST_SHOPS));

            return builder.or(conditions.toArray(new Predicate[0]));
        };
    }
}
