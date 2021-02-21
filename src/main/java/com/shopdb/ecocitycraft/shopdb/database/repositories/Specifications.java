package com.shopdb.ecocitycraft.shopdb.database.repositories;

import com.shopdb.ecocitycraft.shopdb.database.entities.Player;
import com.shopdb.ecocitycraft.shopdb.database.entities.Region;
import com.shopdb.ecocitycraft.shopdb.models.players.PlayersParams;
import com.shopdb.ecocitycraft.shopdb.models.regions.RegionsParams;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class Specifications {

    public static Specification<Region> regionSpecification(RegionsParams params) {
        return (region, query, builder) -> {
            List<Predicate> conditions = new ArrayList<>();

            if (params.getServer() != null) {
                conditions.add(builder.equal(region.get("server"), params.getServer()));
            }

            if (params.isActive()) {
                conditions.add(builder.equal(region.get("active"), Boolean.TRUE));
            }

            if (params.getName() != null) {
                conditions.add(builder.equal(region.get("name"), params.getName().toLowerCase()));
            }

            if (params.getMayorName() != null) {
                conditions.add(builder.equal(region.join("mayors").get("name"), params.getMayorName().toLowerCase()));
            }

            return builder.and(conditions.toArray(new Predicate[0]));
        };
    }

    public static Specification<Player> playerSpecification(PlayersParams params) {
        return (player, query, builder) -> {
            List<Predicate> conditions = new ArrayList<>();

            if (params.getName() != null) {
                conditions.add(builder.equal(player.get("name"), params.getName().toLowerCase()));
            }

            if (params.isActive()) {
                conditions.add(builder.equal(player.get("active"), Boolean.TRUE));
            }

            if (params.getRegionName() != null && params.getServer() != null) {
                Join<Player, Region> region = player.join("towns", JoinType.INNER);

                Predicate a = builder.equal(region.get("server"), params.getServer());
                Predicate b = builder.equal(region.get("name"), params.getRegionName().toLowerCase());

                conditions.add(builder.and(a, b));
            }

            return builder.and(conditions.toArray(new Predicate[0]));
        };
    }
}
