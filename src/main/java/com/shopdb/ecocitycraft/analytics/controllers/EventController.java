package com.shopdb.ecocitycraft.analytics.controllers;

import com.shopdb.ecocitycraft.analytics.models.GetEventsParams;
import com.shopdb.ecocitycraft.analytics.models.PaginatedEventResponse;
import com.shopdb.ecocitycraft.analytics.services.EventService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/all")
    public PaginatedEventResponse getAllEvents(@Valid @ModelAttribute GetEventsParams params) {
        return eventService.getAllEvents(params);
    }

    @GetMapping("/chest-shops")
    public PaginatedEventResponse getChestShopEvents(@Valid @ModelAttribute GetEventsParams params) {
        return eventService.getChestShopEvents(params);
    }

    @GetMapping("/regions")
    public PaginatedEventResponse getRegionsEvents(@Valid @ModelAttribute GetEventsParams params) {
        return eventService.getRegionsEvents(params);
    }

    @GetMapping("/players")
    public PaginatedEventResponse getPlayersEvents(@Valid @ModelAttribute GetEventsParams params) {
        return eventService.getPlayersEvents(params);
    }

    @GetMapping("/region")
    public PaginatedEventResponse getRegionEvents(@Valid @ModelAttribute GetEventsParams params) {
        return eventService.getRegionEvents(params);
    }

    @GetMapping("/player")
    public PaginatedEventResponse getPlayerEvents(@Valid @ModelAttribute GetEventsParams params) {
        return eventService.getPlayerEvents(params);
    }
}
