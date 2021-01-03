package com.shopdb.ecocitycraft.shopdb.services;

import com.shopdb.ecocitycraft.shopdb.database.entities.Player;
import com.shopdb.ecocitycraft.shopdb.database.entities.Region;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import com.shopdb.ecocitycraft.shopdb.database.repositories.RegionRepository;
import com.shopdb.ecocitycraft.shopdb.models.exceptions.AlreadyExistentException;
import com.shopdb.ecocitycraft.shopdb.models.constants.ErrorReasonConstants;
import com.shopdb.ecocitycraft.shopdb.models.exceptions.NotFoundException;
import com.shopdb.ecocitycraft.shopdb.models.players.PaginatedPlayerResponse;
import com.shopdb.ecocitycraft.shopdb.models.regions.PaginatedRegions;
import com.shopdb.ecocitycraft.shopdb.models.regions.RegionRequest;
import com.shopdb.ecocitycraft.shopdb.models.regions.RegionResponse;
import com.shopdb.ecocitycraft.shopdb.models.regions.RegionsParams;
import com.shopdb.ecocitycraft.shopdb.models.signs.PaginatedChestShopSigns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegionService implements ErrorReasonConstants {
    private final RegionRepository repository;
    private PlayerService playerService;
    private ChestShopSignService chestShopSignService;

    public RegionService(RegionRepository repository) {
        this.repository = repository;
    }

    public PaginatedChestShopSigns getRegionChestShopSigns(Server server, String name, int page, int pageSize, TradeType tradeType) {
        Region region = findRegionByServerAndName(server, name);
        return chestShopSignService.getSignsByRegion(region, tradeType, page, pageSize);
    }

    public PaginatedPlayerResponse getRegionMayors(Server server, String name, int page, int pageSize) {
        Region region = findRegionByServerAndName(server, name);
        return playerService.getMayorsByRegion(region, page, pageSize);
    }

    public PaginatedRegions getRegions(RegionsParams params) {
        Region example = new Region();

        if (params.getServer() != null) {
            example.setServer(params.getServer());
        }

        if (params.isActive()) {
            example.setActive(params.isActive());
        }

        if (params.getName() != null) {
            example.setName(params.getName().toLowerCase());
        }

        Pageable pageable = PageRequest.of(params.getPage() - 1, params.getPageSize(), Sort.by("name").ascending());
        Page<Region> pageableResults = repository.findAll(Example.of(example), pageable);

        PaginatedRegions result = new PaginatedRegions();
        result.setCurrentPage(params.getPage());
        result.setTotalPages(pageableResults.getTotalPages());
        result.setTotalElements(pageableResults.getTotalElements());
        result.setResults(pageableResults.getContent().stream().map(this::mapRegionResponse).collect(Collectors.toList()));
        return result;
    }

    public PaginatedRegions getRegionsByPlayer(Player mayor, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("name"));

        Page<Region> pageableResults = repository.findByMayors(mayor, pageable);

        PaginatedRegions response = new PaginatedRegions();
        response.setResults(pageableResults.getContent().stream().map(this::mapRegionResponse).collect(Collectors.toList()));
        response.setCurrentPage(page);
        response.setTotalElements(pageableResults.getTotalElements());
        response.setTotalPages(pageableResults.getTotalPages());

        return response;
    }

    public RegionResponse getRegion(Server server, String name) {
        return mapRegionResponse(findRegionByServerAndName(server, name));
    }

    public RegionResponse createRegion(RegionRequest request) {
        if (regionExists(request.getServer(), request.getName())) {
            throw new AlreadyExistentException(String.format(RESOURCE_ALREADY_EXISTS, "Region", request.getName()));
        }

        Region region = mapRegionRequest(request);
        repository.saveAndFlush(region);
        return mapRegionResponse(region);
    }

    public RegionResponse updateRegion(RegionRequest request) {
        Region region = findRegionByServerAndName(request.getServer(), request.getName());

        region.setServer(request.getServer());
        region.setiBounds(request.getiBounds());
        region.setoBounds(request.getoBounds());

        HashMap<String, Player> players = playerService.getOrAddPlayers(new HashSet<>(request.getMayorNames()));
        region.setMayors(new ArrayList<>(players.values()));

        if (request.isActive()) { // Only update 'active' flag if true, as it defaults to 'false' if it is null.
            region.setActive(request.isActive());
        }

        repository.saveAndFlush(region);
        return mapRegionResponse(region);
    }

    public RegionResponse updateRegionActive(Server server, String name, boolean active) {
        Region region = findRegionByServerAndName(server, name);
        region.setActive(active);
        repository.saveAndFlush(region);
        return mapRegionResponse(region);
    }

    public void setLastUpdatedToNow(Region region) {
        region.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        repository.saveAndFlush(region);
    }

    public Region findRegionByServerAndName(Server server, String name) {
        Region region = repository.findOneByServerAndNameIgnoreCase(server, name);
        if (region == null) {
            throw new NotFoundException(String.format(RESOURCE_NOT_FOUND_BY_NAME, "Region", name));
        }
        return region;
    }

    public List<String> getRegionNames(Server server, boolean active) {
        if (server == null) {
            return active ? repository.findActiveRegionNames() : repository.findAllRegionNames();
        }

        return active ? repository.findActiveRegionNamesByServer(server.name()) : repository.findAllRegionsNamesByServer(server.name());
    }

    private Region mapRegionRequest(RegionRequest request) {
        Region region = new Region();
        region.setName(request.getName().toLowerCase());
        region.setServer(request.getServer());
        region.setiBounds(request.getiBounds());
        region.setoBounds(request.getoBounds());

        HashMap<String, Player> players = playerService.getOrAddPlayers(new HashSet<>(request.getMayorNames()));
        region.setMayors(new ArrayList<>(players.values()));

        region.setActive(request.isActive());
        return region;
    }

    public List<String> mapMayors(Region region) {
        return region.getMayors().stream().map(Player::getName).collect(Collectors.toList());
    }

    public RegionResponse mapRegionResponse(Region region) {
        RegionResponse response = new RegionResponse();
        response.setId(region.getId());
        response.setName(region.getName());
        response.setServer(region.getServer());
        response.setiBounds(region.getiBounds());
        response.setoBounds(region.getoBounds());
        response.setActive(region.isActive());
        response.setMayors(mapMayors(region));

        if (region.getChestShopSigns() != null) {
            response.setNumChestShops(region.getChestShopSigns().size());
        } else {
            response.setNumChestShops(0);
        }

        response.setLastUpdated(region.getLastUpdated());
        return response;
    }

    private boolean regionExists(Server server, String name) {
        return !(repository.findOneByServerAndNameIgnoreCase(server, name) == null);
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Autowired
    public void setChestShopSignService(ChestShopSignService chestShopSignService) {
        this.chestShopSignService = chestShopSignService;
    }
}
