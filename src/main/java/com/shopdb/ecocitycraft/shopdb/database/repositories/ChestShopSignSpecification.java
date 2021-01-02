package com.shopdb.ecocitycraft.shopdb.database.repositories;

import com.shopdb.ecocitycraft.shopdb.database.entities.ChestShopSign;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import com.shopdb.ecocitycraft.shopdb.models.signs.SignParams;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ChestShopSignSpecification {

    public static Specification<ChestShopSign> withSpecification(SignParams params) {
        return (sign, query, builder) -> {
            List<Predicate> conditions = new ArrayList<>();

            if (params.getMaterial() != null) {
                conditions.add(builder.equal(sign.get("material"), params.getMaterial().toLowerCase()));
            }

            if (params.getServer() != null) {
                conditions.add(builder.equal(sign.get("server"), params.getServer()));
            }

            if (params.getTradeType() == TradeType.BUY) {
                conditions.add(builder.equal(sign.get("isBuySign"), true));
            }

            if (params.getTradeType() == TradeType.SELL) {
                conditions.add(builder.equal(sign.get("isSellSign"), true));
            }

            if (params.isFilterIdenticalSigns()) {
                conditions.add(builder.equal(sign.get("isDistinct"), true));
            }

            if (params.isHideOutOfStock() && params.getTradeType() == TradeType.BUY) {
                conditions.add(builder.greaterThan(sign.get("count"), 0));
            }

            return builder.and(conditions.toArray(new Predicate[0]));
        };
    }
}
