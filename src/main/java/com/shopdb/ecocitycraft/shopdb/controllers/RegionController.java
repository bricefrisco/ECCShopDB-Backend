package com.shopdb.ecocitycraft.shopdb.controllers;

import com.shopdb.ecocitycraft.analytics.services.EventService;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.models.regions.PaginatedRegions;
import com.shopdb.ecocitycraft.shopdb.models.regions.RegionRequest;
import com.shopdb.ecocitycraft.shopdb.models.regions.RegionResponse;
import com.shopdb.ecocitycraft.shopdb.models.regions.RegionsParams;
import com.shopdb.ecocitycraft.shopdb.services.RegionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/regions")
@Validated
public class RegionController {
    private final EventService eventService;
    private final RegionService regionService;

    public RegionController(EventService eventService, RegionService regionService) {
        this.eventService = eventService;
        this.regionService = regionService;
    }

    @GetMapping
    public PaginatedRegions getRegions(@Valid @ModelAttribute RegionsParams regionsParams) {
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
