package com.shopdb.ecocitycraft.analytics.services;

import com.shopdb.ecocitycraft.analytics.database.entities.Event;
import com.shopdb.ecocitycraft.analytics.database.repositories.EventRepository;
import com.shopdb.ecocitycraft.analytics.database.repositories.EventSpecification;
import com.shopdb.ecocitycraft.analytics.models.EventDTO;
import com.shopdb.ecocitycraft.analytics.models.GetEventsParams;
import com.shopdb.ecocitycraft.analytics.models.PaginatedEventResponse;
import com.shopdb.ecocitycraft.analytics.models.enums.EventType;
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

    public void saveAndBroadcastEvent(EventDTO eventDTO) {
        Event event = new Event();
        event.setEventType(eventDTO.getEventType());
        event.setTradeType(eventDTO.getTradeType());
        event.setServer(eventDTO.getServer());
        event.setMaterial(eventDTO.getMaterial());
        event.setName(eventDTO.getName());
        event.setPage(eventDTO.getPage());
        event.setTimestamp(new Timestamp(System.currentTimeMillis()));
        eventRepository.saveAndFlush(event);
        LOGGER.info("Successfully inserted and broadcast " + event.getEventType().name() + " event.");
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
