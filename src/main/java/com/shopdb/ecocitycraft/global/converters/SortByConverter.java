package com.shopdb.ecocitycraft.global.converters;

import com.shopdb.ecocitycraft.shopdb.database.entities.enums.SortBy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SortByConverter implements Converter<String, SortBy> {
    @Override
    public SortBy convert(String value) {
        return SortBy.valueOf(value.toUpperCase().replace("-", "_"));
    }
}
