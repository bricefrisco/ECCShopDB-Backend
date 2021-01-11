package com.shopdb.ecocitycraft.shopdb.database.repositories;

import com.shopdb.ecocitycraft.shopdb.database.entities.ChestShopSign;
import com.shopdb.ecocitycraft.shopdb.database.entities.Player;
import com.shopdb.ecocitycraft.shopdb.database.entities.Region;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import com.shopdb.ecocitycraft.shopdb.models.players.PlayersParams;
import com.shopdb.ecocitycraft.shopdb.models.regions.RegionsParams;
import com.shopdb.ecocitycraft.shopdb.models.signs.SignParams;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Specifications {
    public static Specification<ChestShopSign> chestShopSpecification(SignParams params) {
        return (sign, query, builder) -> {
            List<Predicate> conditions = new ArrayList<>();

            conditions.add(builder.notEqual(sign.get("isHidden"), Boolean.TRUE));

            if (params.getMaterial() != null) {
                conditions.add(builder.equal(sign.get("material"), params.getMaterial().toLowerCase()));
            }

            if (params.getServer() != null) {
                conditions.add(builder.equal(sign.get("server"), params.getServer()));
            }

            if (params.getTradeType() == TradeType.BUY) {
                conditions.add(builder.equal(sign.get("isBuySign"), Boolean.TRUE));
            }

            if (params.getTradeType() == TradeType.SELL) {
                conditions.add(builder.equal(sign.get("isSellSign"), Boolean.TRUE));
            }

            if (params.isHideOutOfStock() && params.getTradeType() == TradeType.BUY) {
                conditions.add(builder.greaterThan(sign.get("count"), 0));
            }

            if (params.isHideOutOfStock() && params.getTradeType() == TradeType.SELL) {
                conditions.add(builder.notEqual(sign.get("isFull"), Boolean.TRUE));
            }

            if (params.getRegionName() != null) {
                conditions.add(builder.equal(sign.join("town").get("name"), params.getRegionName().toLowerCase()));
            }

            if (params.getPlayerName() != null) {
                conditions.add(builder.equal(sign.join("owner").get("name"), params.getPlayerName().toLowerCase()));
            }

            return builder.and(conditions.toArray(new Predicate[0]));
        };
    }

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
                conditions.add(builder.equal(player.join("towns").get("name"), params.getRegionName().toLowerCase()));
                conditions.add(builder.equal(player.join("towns").get("server"), params.getServer()));
            }

            return builder.and(conditions.toArray(new Predicate[0]));
        };
    }
}
