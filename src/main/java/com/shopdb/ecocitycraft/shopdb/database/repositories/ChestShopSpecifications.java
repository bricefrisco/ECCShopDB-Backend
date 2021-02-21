package com.shopdb.ecocitycraft.shopdb.database.repositories;

import com.shopdb.ecocitycraft.shopdb.database.entities.ChestShopSign;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import com.shopdb.ecocitycraft.shopdb.models.signs.SignParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChestShopSpecifications {
    private ChestShopSignRepository repository;

    public Specification<ChestShopSign> chestShopSpecification(SignParams params) {
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

            if (params.isHideFull() && params.getTradeType() == TradeType.SELL) {
                conditions.add(builder.notEqual(sign.get("isFull"), Boolean.TRUE));
            }

            if (params.getRegionName() != null) {
                conditions.add(builder.equal(sign.join("town").get("name"), params.getRegionName().toLowerCase()));
            }

            if (params.getPlayerName() != null) {
                conditions.add(builder.equal(sign.join("owner").get("name"), params.getPlayerName().toLowerCase()));
            }

            if (params.isDistinct()) {
                List<String> filterIDs;
                if (params.getMaterial() == null || params.getMaterial().isEmpty()) {
                    if (params.getTradeType() == TradeType.BUY) {
                        filterIDs = repository.findDistinctBuyIDs();
                    } else {
                        filterIDs = repository.findDistinctSellIDs();
                    }
                } else {
                    if (params.getTradeType() == TradeType.BUY) {
                        filterIDs = repository.findDistinctBuyIDs(params.getMaterial());
                    } else {
                        filterIDs = repository.findDistinctSellIDs(params.getMaterial());
                    }
                }

                conditions.add(builder.and(sign.get("id").in(filterIDs)));
            }

            return builder.and(conditions.toArray(new Predicate[0]));
        };
    }

    @Autowired
    public void setRepository(ChestShopSignRepository repository) {
        this.repository = repository;
    }
}
