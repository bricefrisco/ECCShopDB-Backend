package com.shopdb.ecocitycraft.shopdb.services;

import com.shopdb.ecocitycraft.shopdb.database.entities.ChestShopSign;
import com.shopdb.ecocitycraft.shopdb.database.entities.Player;
import com.shopdb.ecocitycraft.shopdb.database.entities.Region;
import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.repositories.ChestShopSignRepository;
import com.shopdb.ecocitycraft.shopdb.database.repositories.RegionRepository;
import com.shopdb.ecocitycraft.shopdb.database.repositories.Specifications;
import com.shopdb.ecocitycraft.shopdb.models.constants.ErrorReasonConstants;
import com.shopdb.ecocitycraft.shopdb.models.exceptions.NotFoundException;
import com.shopdb.ecocitycraft.shopdb.models.regions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RegionService implements ErrorReasonConstants {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegionService.class);
    private static final Pattern p = Pattern.compile("[a-zA-Z0-9+_]{3,16}");
    private final HashMap<String, Server> servers = new HashMap<>();

    private final RegionRepository repository;
    private final ChestShopSignRepository chestShopSignRepository;
    private PlayerService playerService;

    public RegionService(RegionRepository repository, ChestShopSignRepository chestShopSignRepository) {
        this.repository = repository;
        this.chestShopSignRepository = chestShopSignRepository;

        servers.put("rising", Server.MAIN);
        servers.put("rising_n", Server.MAIN_NORTH);
        servers.put("rising_e", Server.MAIN_EAST);
        // TODO: Remove
        servers.put("world", Server.MAIN);
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

    public String processRegions(List<RegionRequest> regionRequests) {
        LOGGER.info("Beginning to process " + regionRequests.size() + " region requests.");

        LOGGER.info("Filtering out invalid region requests...");
        regionRequests = regionRequests.stream().filter(this::regionRequestIsValid).collect(Collectors.toList());

        LOGGER.info("Mapping " + regionRequests.size() + " region requests...");

        List<Region> inserts = new ArrayList<>();
        List<Region> updates = new ArrayList<>();

        for (RegionRequest request : regionRequests) {
            Server server = servers.get(request.getServer());
            Bounds bounds = sort(request.getiBounds(), request.getoBounds());

            Region region = repository.findOneByServerAndNameIgnoreCase(server, request.getName());

            if (region == null) {
                if (hasConflictingRegion(request.getName(), bounds.getLowerBounds(), bounds.getUpperBounds(), server)) continue;

                region = new Region();
                region.setActive(Boolean.FALSE);
                region.setName(request.getName());
                region.setServer(server);

                inserts.add(region);
            } else {
                updates.add(region);
            }

            region.setName(request.getName());
            region.setServer(server);
            region.setIBounds(bounds.getLowerBounds());
            region.setOBounds(bounds.getUpperBounds());

            if (request.getMayorNames().size() > 0) {
                List<Player> owners = new ArrayList<>(playerService.getOrAddPlayers(request.getMayorNames()).values());
                region.setMayors(owners);
            } else {
                region.setMayors(new ArrayList<>());
            }

            region.setLastUpdated(new Timestamp(System.currentTimeMillis()));

            repository.saveAndFlush(region);
        }

        for (Region region : inserts) {
            linkChestShopSigns(region);
        }

        String response = "Successfully updated " + updates.size() + " regions, and inserted " + inserts.size() + " regions.";
        LOGGER.info(response);
        return response;
    }

    public boolean regionRequestIsValid(RegionRequest regionRequest) {
        if (regionRequest == null) {
            LOGGER.warn("Filtering out null region request.");
            return false;
        }

        if (regionRequest.getServer() == null) {
            LOGGER.warn("Filtering out region request with null server: " + regionRequest);
            return false;
        }

        if (regionRequest.getName() == null || regionRequest.getName().isEmpty()) {
            LOGGER.warn("Filtering out region request with invalid name: " + regionRequest);
            return false;
        }

        Server server = servers.get(regionRequest.getServer());
        if (server == null) {
            LOGGER.warn("Filtering out region request with invalid server: " + regionRequest);
            return false;
        }

        if (regionRequest.getiBounds() == null) {
            LOGGER.warn("Filtering out region request with invalid iBounds: " + regionRequest);
            return false;
        }

        if (regionRequest.getoBounds() == null) {
            LOGGER.warn("Filtering out region request with invalid oBounds: " + regionRequest);
            return false;
        }

        if (regionRequest.getMayorNames() == null) {
            LOGGER.warn("Filtering out region request with null mayors: " + regionRequest);
            return false;
        }

        for (String name : regionRequest.getMayorNames()) {
            if (!p.matcher(name).matches()) {
                LOGGER.warn("Filtering out region request with invalid mayor(s): " + regionRequest);
                return false;
            }
        }

        return true;
    }

    private boolean hasConflictingRegion(String name, Location iBounds, Location oBounds, Server server) {
        Region conflictingRegion = findByLocations(iBounds, oBounds, server);
        if (conflictingRegion != null) {
            LOGGER.warn("Region " + name + " conflicts with " + conflictingRegion.getName() + " on " + server.name() + " - these coordinates overlap.");
            return true;
        }

        conflictingRegion = findRegionsInCoordinates(iBounds, oBounds, server);
        if (conflictingRegion != null) {
            LOGGER.warn("Region " + name + " conflicts with " + conflictingRegion.getName() + " on " + server.name() + " - these coordinates overlap.");
            return true;
        }

        return false;
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

    private void linkChestShopSigns(Region region) {
        List<ChestShopSign> signs = getChestShopSignByRegion(region);
        for (ChestShopSign sign : signs) {
            sign.setTown(region);
        }
        chestShopSignRepository.saveAll(signs);
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

    private List<String> mapMayors(Region region) {
        return region.getMayors().stream().map(Player::getName).collect(Collectors.toList());
    }

    public Region findByCoordinates(int x, int y, int z, String server) {
        return repository.findByCoordinates(x, y, z, server);
    }

    private Region findByLocations(Location iBounds, Location oBounds, Server server) {
        Region region = findByCoordinates(iBounds.getX(), iBounds.getY(), iBounds.getZ(), server.name());
        if (region != null) return region;
        return findByCoordinates(oBounds.getX(), oBounds.getY(), oBounds.getZ(), server.name());
    }

    private Region findRegionsInCoordinates(Location iBounds, Location oBounds, Server server) {
        return repository.findRegionsInCoordinates(iBounds.getX(), oBounds.getX(), iBounds.getZ(), oBounds.getZ(), server.name());
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }
}
