package com.shopdb.ecocitycraft.shopdb.controllers;

import com.shopdb.ecocitycraft.analytics.services.EventService;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import com.shopdb.ecocitycraft.shopdb.models.players.PaginatedPlayerResponse;
import com.shopdb.ecocitycraft.shopdb.models.regions.PaginatedRegions;
import com.shopdb.ecocitycraft.shopdb.models.regions.RegionRequest;
import com.shopdb.ecocitycraft.shopdb.models.regions.RegionResponse;
import com.shopdb.ecocitycraft.shopdb.models.regions.RegionsParams;
import com.shopdb.ecocitycraft.shopdb.models.signs.PaginatedChestShopSigns;
import com.shopdb.ecocitycraft.shopdb.services.RegionService;
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
@RequestMapping("/regions")
@CrossOrigin(origins = "*")
@Validated
public class RegionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegionController.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private RegionService regionService;

    @GetMapping
    public PaginatedRegions getAllRegions(@Valid @ModelAttribute RegionsParams regionsParams) {
        PaginatedRegions response = regionService.getRegions(regionsParams);
        if (!response.getResults().isEmpty()) {
            eventService.sendRegionSearchAnalytics(regionsParams);
        }
        return regionService.getRegions(regionsParams);
    }

    @GetMapping("/region-names")
    public List<String> getRegionNames(@RequestParam(required = false) Server server,
                                       @RequestParam(required = false, defaultValue = "true") boolean active) {
        return regionService.getRegionNames(server, active);
    }

    @GetMapping("/{server}/{name}")
    public RegionResponse getRegion(@PathVariable Server server, @PathVariable String name, @RequestParam(required = false, defaultValue = "true") boolean sendAnalytics) {
        RegionResponse response = regionService.getRegion(server, name);
        if (sendAnalytics) { // So the bot won't send analytics
            eventService.sendRegionViewAnalytics(server, name);
        }
        return response;
    }

    @GetMapping("/{server}/{name}/chest-shops")
    public PaginatedChestShopSigns getRegionChestShopSigns(
            @PathVariable Server server,
            @PathVariable String name,
            @Valid @Min(1) @RequestParam(required = false, defaultValue = "1") int page,
            @Valid @Min(1) @Max(100) @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) TradeType tradeType) {
        PaginatedChestShopSigns response = regionService.getRegionChestShopSigns(server, name, page, pageSize, tradeType);
        eventService.sendRegionChestShopsViewAnalytics(server, name, tradeType, page);
        return response;
    }

    @GetMapping("/{server}/{name}/mayors")
    public PaginatedPlayerResponse getRegionMayors(
            @PathVariable Server server,
            @PathVariable String name,
            @Valid @Min(1) @RequestParam(required = false, defaultValue = "1") int page,
            @Valid @Min(1) @Max(100) @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        PaginatedPlayerResponse response = regionService.getRegionMayors(server, name, page, pageSize);
        eventService.sendRegionMayorsViewAnalytics(server, name, page);
        return response;
    }

    @PostMapping
    public RegionResponse createRegion(@Valid @RequestBody RegionRequest regionRequest) {
        return regionService.createRegion(regionRequest);
    }

    @PutMapping
    public RegionResponse updateRegion(@Valid @RequestBody RegionRequest regionRequest) {
        return regionService.updateRegion(regionRequest);
    }

    @PutMapping("/{server}/{name}")
    public RegionResponse updateRegionActive(
            @PathVariable Server server,
            @PathVariable String name,
            @RequestParam("active") boolean active
    ) {
        return regionService.updateRegionActive(server, name, active);
    }
}
