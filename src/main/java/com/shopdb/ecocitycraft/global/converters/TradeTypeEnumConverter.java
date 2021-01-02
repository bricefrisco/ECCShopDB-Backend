package com.shopdb.ecocitycraft.global.converters;

import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TradeTypeEnumConverter implements Converter<String, TradeType> {
    @Override
    public TradeType convert(String value) {
        return TradeType.valueOf(value.toUpperCase());
    }
}
