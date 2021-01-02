package com.shopdb.ecocitycraft.global.converters;

import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ServerEnumConverter implements Converter<String, Server> {
    @Override
    public Server convert(String value) {
        return Server.valueOf(value.toUpperCase().replace("-", "_"));
    }
}
