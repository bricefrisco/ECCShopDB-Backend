package com.shopdb.ecocitycraft.shopdb.controllers;

import com.shopdb.ecocitycraft.analytics.services.EventService;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import com.shopdb.ecocitycraft.shopdb.models.players.PaginatedPlayerResponse;
import com.shopdb.ecocitycraft.shopdb.models.players.PlayerRequest;
import com.shopdb.ecocitycraft.shopdb.models.players.PlayerResponse;
import com.shopdb.ecocitycraft.shopdb.models.players.PlayersParams;
import com.shopdb.ecocitycraft.shopdb.models.regions.PaginatedRegions;
import com.shopdb.ecocitycraft.shopdb.models.signs.PaginatedChestShopSigns;
import com.shopdb.ecocitycraft.shopdb.services.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/players")
@CrossOrigin(origins = "*")
@Validated
public class PlayerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerController.class);

    @Autowired
    private PlayerService playerService;

    @Autowired
    private EventService eventService;

    @GetMapping
    public PaginatedPlayerResponse getPlayers(@Valid @ModelAttribute PlayersParams playersParams) {
        PaginatedPlayerResponse response = playerService.getPlayers(playersParams);
        if (!response.getResults().isEmpty()) {
            eventService.sendPlayerSearchAnalytics(playersParams);
        }
        return response;
    }

    @GetMapping("/player-names")
    public List<String> getPlayerNames(@RequestParam(required = false) boolean active) {
        return playerService.getPlayerNames(active);
    }

    @GetMapping("/{name}")
    public PlayerResponse getPlayer(@PathVariable String name) {
        PlayerResponse response = playerService.getPlayer(name);
        eventService.sendPlayerViewAnalytics(name);
        return response;
    }

    @GetMapping("/{name}/towns")
    public PaginatedRegions getPlayerTowns(@PathVariable String name,
                                           @Valid @Min(1) @RequestParam(required = false, defaultValue = "1") int page,
                                           @Valid @Min(1) @Max(100) @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        PaginatedRegions response = playerService.getPlayerTowns(name, page, pageSize);
        eventService.sendPlayerRegionsViewAnalytics(name, page);
        return response;
    }

    @GetMapping("/{name}/chest-shops")
    public PaginatedChestShopSigns getSigns(@PathVariable String name,
                                            @RequestParam(required = false) TradeType tradeType,
                                            @Valid @Min(1) @RequestParam(required = false, defaultValue = "1") int page,
                                            @Valid @Min(1) @Max(100) @RequestParam(required = false, defaultValue = "10") int pageSize) {
        PaginatedChestShopSigns response = playerService.getPlayerChestShops(name, tradeType, page, pageSize);
        eventService.sendPlayerChestShopsViewAnalytics(name, tradeType, page);
        return response;
    }

    @PostMapping
    public PlayerResponse createPlayer(@Valid @RequestBody PlayerRequest request) {
        return playerService.createPlayer(request);
    }

    @PutMapping
    public PlayerResponse updatePlayer(@Valid @RequestBody PlayerRequest request) {
        return playerService.updatePlayer(request);
    }
}
