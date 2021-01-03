package com.shopdb.ecocitycraft.shopdb.models.signs;

import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class SignsRequest {
    @NotNull
    private Server server;
    @NotNull
    private String regionName;
    @NotNull
    private List<Sign> signs;
}
