package com.shopdb.ecocitycraft.shopdb.services;

import com.shopdb.ecocitycraft.shopdb.database.entities.ChestShopSign;
import com.shopdb.ecocitycraft.shopdb.database.entities.Player;
import com.shopdb.ecocitycraft.shopdb.database.entities.Region;
import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.repositories.ChestShopSignRepository;
import com.shopdb.ecocitycraft.shopdb.database.repositories.RegionRepository;
import com.shopdb.ecocitycraft.shopdb.database.repositories.Specifications;
import com.shopdb.ecocitycraft.shopdb.models.exceptions.AlreadyExistentException;
import com.shopdb.ecocitycraft.shopdb.models.constants.ErrorReasonConstants;
import com.shopdb.ecocitycraft.shopdb.models.exceptions.NotFoundException;
import com.shopdb.ecocitycraft.shopdb.models.regions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegionService implements ErrorReasonConstants {
    private final RegionRepository repository;
    private final ChestShopSignRepository chestShopSignRepository;
    private PlayerService playerService;

    public RegionService(RegionRepository repository, ChestShopSignRepository chestShopSignRepository) {
        this.repository = repository;
        this.chestShopSignRepository = chestShopSignRepository;
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

    private void hideChestShopSigns(Region region) {
        hideChestShopSigns(getChestShopSignByRegion(region));
    }

    private void unHideChestShopSigns(Region region) {
        unHideChestShopSigns(getChestShopSignByRegion(region));
    }

    private List<ChestShopSign> getChestShopSignByRegion(Region region) {
        return getChestShopSignByLocation(region.getServer(), region.getIBounds(), region.getOBounds());
    }

    private List<ChestShopSign> getChestShopSignByLocation(Server server, Location iBounds, Location oBounds) {
        int lx = iBounds.getX();
        int ly = iBounds.getY();
        int lz = iBounds.getZ();
        int ux = oBounds.getX();
        int uy = oBounds.getY();
        int uz = oBounds.getZ();
        return chestShopSignRepository.getChestShopSignByLocation(server.name(), lx, ux, ly, uy, lz, uz);
    }

    private void unHideChestShopSigns(List<ChestShopSign> chestShopSigns) {
        if (chestShopSigns.size() == 0) return;
        for (ChestShopSign chestShopSign : chestShopSigns) {
            chestShopSign.setIsHidden(Boolean.FALSE);
        }
        chestShopSignRepository.saveAll(chestShopSigns);
    }

    private void hideChestShopSigns(List<ChestShopSign> chestShopSigns) {
        if (chestShopSigns.size() == 0) return;
        for (ChestShopSign chestShopSign : chestShopSigns) {
            chestShopSign.setIsHidden(Boolean.TRUE);
        }
        chestShopSignRepository.saveAll(chestShopSigns);
    }

    public RegionResponse updateRegion(RegionRequest request) {
        Region region = findRegionByServerAndName(request.getServer(), request.getName());

        region.setServer(request.getServer());

        Bounds bounds = sort(request.getIBounds(), request.getOBounds());
        region.setIBounds(bounds.getLowerBounds());
        region.setOBounds(bounds.getUpperBounds());

        HashMap<String, Player> players = playerService.getOrAddPlayers(new HashSet<>(request.getMayorNames()));
        region.setMayors(new ArrayList<>(players.values()));

        repository.saveAndFlush(region);
        return mapRegionResponse(region);
    }

    public RegionResponse updateRegionActive(Server server, String name, boolean active) {
        Region region = findRegionByServerAndName(server, name);

        if (active) {
            region.setActive(true);
            unHideChestShopSigns(region);
        } else {
            region.setActive(false);
            hideChestShopSigns(region);
        }

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

        Bounds bounds = sort(request.getIBounds(), request.getOBounds());
        region.setIBounds(bounds.getLowerBounds());
        region.setOBounds(bounds.getUpperBounds());

        HashMap<String, Player> players = playerService.getOrAddPlayers(new HashSet<>(request.getMayorNames()));
        region.setMayors(new ArrayList<>(players.values()));

        region.setActive(request.getActive());
        return region;
    }

    private Bounds sort(Location l1, Location l2) {
        Location lowerBounds = new Location();
        Location upperBounds = new Location();

        if (l1.getX() <= l2.getX()) {
            lowerBounds.setX(l1.getX());
            upperBounds.setX(l2.getX());
        } else {
            lowerBounds.setX(l2.getX());
            upperBounds.setX(l1.getX());
        }

        if (l1.getY() <= l2.getY()) {
            lowerBounds.setY(l1.getY());
            upperBounds.setY(l2.getY());
        } else {
            lowerBounds.setY(l2.getY());
            upperBounds.setY(l1.getY());
        }

        if (l1.getZ() <= l2.getZ()) {
            lowerBounds.setZ(l1.getZ());
            upperBounds.setZ(l2.getZ());
        } else {
            lowerBounds.setZ(l2.getZ());
            upperBounds.setZ(l1.getZ());
        }

        return new Bounds(lowerBounds, upperBounds);
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
