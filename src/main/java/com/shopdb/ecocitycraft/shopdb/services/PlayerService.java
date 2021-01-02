package com.shopdb.ecocitycraft.shopdb.services;

import com.shopdb.ecocitycraft.shopdb.database.entities.Player;
import com.shopdb.ecocitycraft.shopdb.database.entities.Region;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import com.shopdb.ecocitycraft.shopdb.database.repositories.PlayerRepository;
import com.shopdb.ecocitycraft.shopdb.models.exceptions.AlreadyExistentException;
import com.shopdb.ecocitycraft.shopdb.models.exceptions.ErrorReasonConstants;
import com.shopdb.ecocitycraft.shopdb.models.exceptions.NotFoundException;
import com.shopdb.ecocitycraft.shopdb.models.players.PaginatedPlayerResponse;
import com.shopdb.ecocitycraft.shopdb.models.players.PlayerRequest;
import com.shopdb.ecocitycraft.shopdb.models.players.PlayerResponse;
import com.shopdb.ecocitycraft.shopdb.models.players.PlayersParams;
import com.shopdb.ecocitycraft.shopdb.models.regions.PaginatedRegions;
import com.shopdb.ecocitycraft.shopdb.models.signs.PaginatedChestShopSigns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlayerService implements ErrorReasonConstants {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RegionService regionService;

    @Autowired
    private ChestShopSignService chestShopSignService;

    public PlayerResponse getPlayer(String name) {
        return mapPlayerResponse(getPlayerByName(name));
    }

    public PlayerResponse createPlayer(PlayerRequest request) {
        if (playerExists(request.getName())) {
            throw new AlreadyExistentException(String.format(RESOURCE_ALREADY_EXISTS, "Player", request.getName()));
        }

        Player player = mapPlayerRequest(request);
        playerRepository.saveAndFlush(player);
        return mapPlayerResponse(player);
    }

    public PlayerResponse updatePlayer(PlayerRequest request) {
        Player player = getPlayerByName(request.getName());
        player.setLastSeen(request.getLastSeen());
        player.setActive(request.getActive());
        player.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        playerRepository.saveAndFlush(player);
        return this.mapPlayerResponse(player);
    }

    public List<String> getPlayerNames(boolean active) {
        return active ? playerRepository.getActivePlayerNames() : playerRepository.getPlayerNames();
    }

    public PaginatedPlayerResponse getMayorsByRegion(Region town, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("name"));

        Page<Player> pageableResults = playerRepository.findByTowns(town, pageable);

        PaginatedPlayerResponse response = new PaginatedPlayerResponse();
        response.setResults(pageableResults.getContent().stream().map(this::mapPlayerResponse).collect(Collectors.toList()));
        response.setCurrentPage(page);
        response.setTotalElements(pageableResults.getTotalElements());
        response.setTotalPages(pageableResults.getTotalPages());

        return response;
    }

    public PaginatedRegions getPlayerTowns(String name, int page, int pageSize) {
        Player player = getPlayerByName(name);
        return regionService.getRegionsByPlayer(player, page, pageSize);
    }

    public PaginatedChestShopSigns getPlayerChestShops(String name, TradeType tradeType, int page, int pageSize) {
        return chestShopSignService.getSignsByPlayer(getPlayerByName(name), tradeType, page, pageSize);
    }

    public PaginatedPlayerResponse getPlayers(PlayersParams params) {
        Player example = new Player();
        if (params.getName() != null) {
            example.setName(params.getName().toLowerCase());
        }

        if (params.isActive()) {
            example.setActive(true);
        }

        Pageable pageable = PageRequest.of(params.getPage() - 1, params.getPageSize(), Sort.by("name"));
        Page<Player> pageableResults = playerRepository.findAll(Example.of(example), pageable);
        PaginatedPlayerResponse response = new PaginatedPlayerResponse();
        response.setResults(pageableResults.getContent().stream().map(this::mapPlayerResponse).collect(Collectors.toList()));
        response.setCurrentPage(params.getPage());
        response.setTotalElements(pageableResults.getTotalElements());
        response.setTotalPages(pageableResults.getTotalPages());

        return response;
    }

    public HashMap<String, Player> getOrAddPlayers(Set<String> playerNames) {
        HashMap<String, Player> players = new HashMap<>();

        for (String name : playerNames) {
            Player player = playerRepository.findOneByNameIgnoreCase(name);
            if (player == null) {
                player = new Player();
                player.setName(name.toLowerCase());
                playerRepository.saveAndFlush(player);
            }
            players.put(name, player);
        }

        return players;
    }

    public Player getPlayerByName(String name) {
        Player player = playerRepository.findOneByNameIgnoreCase(name);
        if (player == null) {
            throw new NotFoundException(String.format(RESOURCE_NOT_FOUND_BY_NAME, "Player", name));
        }
        return player;
    }

    public void saveAndFlush(Player player) {
        playerRepository.saveAndFlush(player);
    }

    private boolean playerExists(String name) {
        return !(playerRepository.findOneByNameIgnoreCase(name) == null);
    }

    private Player mapPlayerRequest(PlayerRequest request) {
        Player player = new Player();
        player.setName(request.getName());
        player.setLastSeen(request.getLastSeen());
        return player;
    }

    public PlayerResponse mapPlayerResponse(Player player) {
        PlayerResponse playerResponse = new PlayerResponse();
        playerResponse.setId(player.getId());
        playerResponse.setName(player.getName());
        playerResponse.setLastSeen(player.getLastSeen());
        playerResponse.setNumChestShops(player.getChestShopSigns().size());
        playerResponse.setTowns(player.getTowns().stream().map(region -> region.getServer().name() + "|" + region.getName()).collect(Collectors.toList()));

        if (player.getLastUpdated() != null) {
            playerResponse.setLastUpdated(player.getLastUpdated());
        }

        if (player.getActive() != null) {
            playerResponse.setActive(player.getActive());
        }
        return playerResponse;
    }
}