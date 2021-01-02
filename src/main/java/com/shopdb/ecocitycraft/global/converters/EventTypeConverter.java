package com.shopdb.ecocitycraft.global.converters;

import com.shopdb.ecocitycraft.analytics.models.enums.EventType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EventTypeConverter implements Converter<String, EventType> {
    @Override
    public EventType convert(String value) {
        return EventType.valueOf(value.toUpperCase().replace("-", "_"));
    }
}