package com.shopdb.ecocitycraft.shopdb.services;

import com.shopdb.ecocitycraft.shopdb.database.entities.Player;
import com.shopdb.ecocitycraft.shopdb.database.entities.Region;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import com.shopdb.ecocitycraft.shopdb.database.repositories.RegionRepository;
import com.shopdb.ecocitycraft.shopdb.database.repositories.Specifications;
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
import org.springframework.data.jpa.domain.Specification;
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

    public RegionService(RegionRepository repository) {
        this.repository = repository;
    }

    public PaginatedRegions getRegions(RegionsParams params) {
        Specification<Region> specification = Specifications.regionSpecification(params);
        Pageable pageable = PageRequest.of(params.getPage() - 1, params.getPageSize(), Sort.by("name").ascending());

        Page<Region> pageableResults = repository.findAll(specification, pageable);

        List<RegionResponse> results = pageableResults.getContent().stream().map(this::mapRegionResponse).collect(Collectors.toList());

        return new PaginatedRegions(
                params.getPage(),
                pageableResults.getTotalPages(),
                pageableResults.getTotalElements(),
                results
        );
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
        region.setIBounds(request.getIBounds());
        region.setOBounds(request.getOBounds());

        HashMap<String, Player> players = playerService.getOrAddPlayers(new HashSet<>(request.getMayorNames()));
        region.setMayors(new ArrayList<>(players.values()));

        if (request.getActive()) { // Only update 'active' flag if true, as it defaults to 'false' if it is null.
            region.setActive(Boolean.TRUE);
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
        region.setIBounds(request.getIBounds());
        region.setOBounds(request.getOBounds());

        HashMap<String, Player> players = playerService.getOrAddPlayers(new HashSet<>(request.getMayorNames()));
        region.setMayors(new ArrayList<>(players.values()));

        region.setActive(request.getActive());
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
        response.setIBounds(region.getIBounds());
        response.setOBounds(region.getOBounds());
        response.setActive(region.getActive());
        response.setMayors(mapMayors(region));

        if (region.getChestShopSigns() != null) {
            response.setNumChestShops(region.getChestShopSigns().size());
        } else {
            response.setNumChestShops(0);
        }

        response.setLastUpdated(region.getLastUpdated());
        return response;
    }

    public Region findByCoordinates(int x, int y, int z, String server) {
        return repository.findByCoordinates(x, y, z, server);
    }

    private boolean regionExists(Server server, String name) {
        return !(repository.findOneByServerAndNameIgnoreCase(server, name) == null);
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }
}
