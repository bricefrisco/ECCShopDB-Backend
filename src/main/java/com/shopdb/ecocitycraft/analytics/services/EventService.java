package com.shopdb.ecocitycraft.analytics.services;

import com.shopdb.ecocitycraft.analytics.database.entities.Event;
import com.shopdb.ecocitycraft.analytics.database.repositories.EventRepository;
import com.shopdb.ecocitycraft.analytics.database.repositories.EventSpecification;
import com.shopdb.ecocitycraft.analytics.models.EventDTO;
import com.shopdb.ecocitycraft.analytics.models.GetEventsParams;
import com.shopdb.ecocitycraft.analytics.models.PaginatedEventResponse;
import com.shopdb.ecocitycraft.analytics.models.enums.EventType;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import com.shopdb.ecocitycraft.shopdb.models.players.PlayersParams;
import com.shopdb.ecocitycraft.shopdb.models.regions.RegionsParams;
import com.shopdb.ecocitycraft.shopdb.models.signs.SignParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.stream.Collectors;

@Service
public class EventService {
    public static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    public void saveEvent(EventDTO eventDTO) {
        Event event = new Event();
        event.setEventType(eventDTO.getEventType());
        event.setTradeType(eventDTO.getTradeType());
        event.setServer(eventDTO.getServer());
        event.setMaterial(eventDTO.getMaterial());
        event.setName(eventDTO.getName());
        event.setPage(eventDTO.getPage());
        event.setTimestamp(new Timestamp(System.currentTimeMillis()));
        eventRepository.saveAndFlush(event);
        LOGGER.info("Successfully inserted " + event.getEventType().name() + " event.");
    }

    public PaginatedEventResponse getAllEvents(GetEventsParams params) {
        Pageable pageable = PageRequest.of(params.getPage() - 1, params.getPageSize(), Sort.by("timestamp").descending());
        Page<Event> pageableResults = eventRepository.findAll(pageable);
        return mapPages(pageableResults, params.getPage());
    }

    public PaginatedEventResponse getChestShopEvents(GetEventsParams params) {
        Event example = new Event();
        example.setEventType(EventType.CHEST_SHOPS);

        Pageable pageable = PageRequest.of(params.getPage() - 1, params.getPageSize(), Sort.by("timestamp").descending());
        Page<Event> pageableResults = eventRepository.findAll(Example.of(example), pageable);

        return mapPages(pageableResults, params.getPage());
    }

    public PaginatedEventResponse getRegionsEvents(GetEventsParams params) {
        Event example = new Event();
        example.setEventType(EventType.REGIONS);

        Pageable pageable = PageRequest.of(params.getPage() - 1, params.getPageSize(), Sort.by("timestamp").descending());
        Page<Event> pageableResults = eventRepository.findAll(Example.of(example), pageable);

        return mapPages(pageableResults, params.getPage());
    }

    public PaginatedEventResponse getPlayersEvents(GetEventsParams params) {
        Event example = new Event();
        example.setEventType(EventType.PLAYERS);

        Pageable pageable = PageRequest.of(params.getPage() - 1, params.getPageSize(), Sort.by("timestamp").descending());
        Page<Event> pageableResults = eventRepository.findAll(Example.of(example), pageable);

        return mapPages(pageableResults, params.getPage());
    }

    public PaginatedEventResponse getRegionEvents(GetEventsParams params) {
        Pageable pageable = PageRequest.of(params.getPage() - 1, params.getPageSize(), Sort.by("timestamp").descending());
        Page<Event> pageableResults = eventRepository.findAll(EventSpecification.regionEvents(), pageable);
        return mapPages(pageableResults, params.getPage());
    }

    public PaginatedEventResponse getPlayerEvents(GetEventsParams params) {
        Pageable pageable = PageRequest.of(params.getPage() - 1, params.getPageSize(), Sort.by("timestamp").descending());
        Page<Event> pageableResults = eventRepository.findAll(EventSpecification.playerEvents(), pageable);
        return mapPages(pageableResults, params.getPage());
    }

    public void sendChestShopSearchAnalytics(SignParams params) {
        LOGGER.info("Sending chest shop search analytics");
        EventDTO event = new EventDTO();
        event.setEventType(EventType.CHEST_SHOPS);
        event.setPage(params.getPage());
        event.setTradeType(params.getTradeType());
        event.setServer(params.getServer());
        event.setMaterial(params.getMaterial());
        saveEvent(event);
    }

    public void sendRegionSearchAnalytics(RegionsParams params) {
        EventDTO event = new EventDTO();
        event.setEventType(EventType.REGIONS);
        event.setPage(params.getPage());
        event.setName(params.getName());
        event.setServer(params.getServer());
        saveEvent(event);
    }

    public void sendPlayerViewAnalytics(String name) {
        EventDTO event = new EventDTO();
        event.setEventType(EventType.PLAYER);
        event.setName(name);
        saveEvent(event);
    }

    public void sendPlayerSearchAnalytics(PlayersParams params) {
        EventDTO event = new EventDTO();
        event.setEventType(EventType.PLAYERS);
        event.setPage(params.getPage());
        event.setName(params.getName());
        saveEvent(event);
    }

    public void sendRegionViewAnalytics(Server server, String name) {
        EventDTO event = new EventDTO();
        event.setEventType(EventType.REGION);
        event.setName(name);
        event.setServer(server);
        saveEvent(event);
    }

    public void sendRegionMayorsViewAnalytics(Server server, String name, int page) {
        EventDTO event = new EventDTO();
        event.setEventType(EventType.REGION_PLAYERS);
        event.setName(name);
        event.setServer(server);
        event.setPage(page);
        saveEvent(event);
    }

    public void sendRegionChestShopsViewAnalytics(Server server, String name, TradeType tradeType, int page) {
        EventDTO event = new EventDTO();
        event.setEventType(EventType.REGION_CHEST_SHOPS);
        event.setName(name);
        event.setServer(server);
        event.setTradeType(tradeType);
        event.setPage(page);
        saveEvent( event);
    }

    public void sendPlayerRegionsViewAnalytics(String name, int page) {
        EventDTO event = new EventDTO();
        event.setEventType(EventType.PLAYER_REGIONS);
        event.setName(name);
        event.setPage(page);
        saveEvent(event);
    }

    public void sendPlayerChestShopsViewAnalytics(String name, TradeType tradeType, int page) {
        EventDTO event = new EventDTO();
        event.setEventType(EventType.PLAYER_CHEST_SHOPS);
        event.setName(name);
        event.setPage(page);
        event.setTradeType(tradeType);
        saveEvent(event);
    }

    private PaginatedEventResponse mapPages(Page<Event> pageableResults, int page) {
        PaginatedEventResponse response = new PaginatedEventResponse();
        response.setResults(pageableResults.getContent().stream().map(this::mapEvent).collect(Collectors.toList()));
        response.setCurrentPage(page);
        response.setTotalPages(pageableResults.getTotalPages());
        response.setTotalElements(pageableResults.getTotalElements());
        return response;
    }

    private EventDTO mapEvent(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventType(event.getEventType());
        eventDTO.setMaterial(event.getMaterial());
        eventDTO.setName(event.getName());
        eventDTO.setPage(event.getPage());
        eventDTO.setServer(event.getServer());
        eventDTO.setTradeType(event.getTradeType());
        eventDTO.setTimestamp(event.getTimestamp());
        return eventDTO;
    }

}
