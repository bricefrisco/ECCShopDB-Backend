package com.shopdb.ecocitycraft.shopdb.services;

import com.shopdb.ecocitycraft.shopdb.database.entities.ChestShopSign;
import com.shopdb.ecocitycraft.shopdb.database.entities.Player;
import com.shopdb.ecocitycraft.shopdb.database.entities.Region;
import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.SortBy;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import com.shopdb.ecocitycraft.shopdb.database.repositories.ChestShopSignRepository;
import com.shopdb.ecocitycraft.shopdb.database.repositories.Specifications;
import com.shopdb.ecocitycraft.shopdb.models.constants.ErrorReasonConstants;
import com.shopdb.ecocitycraft.shopdb.models.constants.RegexConstants;
import com.shopdb.ecocitycraft.shopdb.models.signs.*;
import com.shopdb.ecocitycraft.shopdb.models.signs.ecc.EventType;
import com.shopdb.ecocitycraft.shopdb.models.signs.ecc.ShopEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChestShopSignService implements ErrorReasonConstants, RegexConstants {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChestShopSignService.class);

    private final ChestShopSignRepository chestShopSignRepository;
    private PlayerService playerService;
    private RegionService regionService;

    public ChestShopSignService(ChestShopSignRepository chestShopSignRepository) {
        this.chestShopSignRepository = chestShopSignRepository;
    }

    public PaginatedChestShopSigns getSigns(SignParams params) {
        Sort sort = getSort(params.getSortBy(), params.getTradeType());
        Specification<ChestShopSign> specification = Specifications.chestShopSpecification(params);
        Pageable pageable = PageRequest.of(params.getPage() - 1, params.getPageSize(), sort);

        Page<ChestShopSign> pageableResults = chestShopSignRepository.findAll(specification, pageable);

        List<ChestShopSignDto> results = mapSigns(pageableResults.getContent());

        return new PaginatedChestShopSigns(
                params.getPage(),
                pageableResults.getTotalPages(),
                pageableResults.getTotalElements(),
                results
        );
    }

    public String processShopEvents(List<ShopEvent> shopEvents) {
        LOGGER.info("Beginning to process " + shopEvents.size() + " shop events...");

        List<String> shopIdsToDelete = new ArrayList<>();
        List<ShopEvent> upserts = new ArrayList<>();
        Set<String> playerNames = new HashSet<>();

        LOGGER.info("Sorting shop events...");
        for (ShopEvent shopEvent : shopEvents) {
            if (shopEvent.getEventType().equals(EventType.DELETE)) {
                shopIdsToDelete.add(shopEvent.getId());
            } else {
                upserts.add(shopEvent);
                playerNames.add(shopEvent.getOwner());
            }
        }

        LOGGER.info("Found " + shopIdsToDelete.size() + " shop deletion events.");
        LOGGER.info("Found " + upserts.size() + " shops to create or modify.");

        if (shopIdsToDelete.size() > 0) {
            LOGGER.info("Deleting " + shopIdsToDelete.size() + " shops...");
            chestShopSignRepository.deleteByIdIn(shopIdsToDelete);
        }

        if (upserts.size() == 0) {
            String response = "Successfully created/updated 0 chest shops, and removed " + shopIdsToDelete.size() + " chest shops.";
            LOGGER.info(response);
            return response;
        }

        LOGGER.info("Retrieving/adding " + playerNames.size() + " owners...");
        HashMap<String, Player> players = playerService.getOrAddPlayers(playerNames);

        LOGGER.info("Mapping " + shopEvents.size() + " events to chest shops...");
        List<ChestShopSign> signs = new ArrayList<>();
        for (ShopEvent upsert : upserts) {
            if (!eventIsValid(upsert)) continue;
            Optional<ChestShopSign> maybeSign = chestShopSignRepository.findById(upsert.getId());
            ChestShopSign sign = maybeSign.map(chestShopSign -> convert(chestShopSign, upsert, players)).orElseGet(() -> convert(upsert, players));
            signs.add(sign);
        }

        LOGGER.info("Adding " + signs.size() + " chest shops...");
        if (signs.size() > 0) {
            chestShopSignRepository.saveAll(signs);
        }


        String response = "Successfully created/updated " + signs.size() + " chest shops, and removed " + shopIdsToDelete.size() + " chest shops.";
        LOGGER.info(response);
        return response;
    }

    private boolean eventIsValid(ShopEvent event) {
        if (event.getId() == null || event.getId().isEmpty()) {
            LOGGER.info("Skipping event " + event.toString() + " - ID is null or empty.");
            return false;
        }

        if (event.getEventType() == null) {
            LOGGER.info("Skipping event " + event.toString() + " - event type not specified.");
            return false;
        }

        if (event.getWorld() == null || event.getWorld().isEmpty()) {
            LOGGER.info("Skipping event " + event.toString() + " - no world specified.");
            return false;
        }

        if (!event.getWorld().equals("world") && !event.getWorld().equals("rising_n") && !event.getWorld().equals("rising_e")) {
            LOGGER.info("Skipping event " + event.toString() + " - server cannot be determined.");
            return false;
        }

        if (event.getX() == null || event.getY() == null || event.getZ() == null) {
            LOGGER.info("Skipping event " + event.toString() + " - X, Y, or Z coordinate is missing.");
            return false;
        }

        if (event.getOwner() == null || event.getOwner().isEmpty()) {
            LOGGER.info("Skipping event " + event.toString() + " - owner is missing");
            return false;
        }

        if (event.getQuantity() == null || event.getQuantity() == 0) {
            LOGGER.info("Skipping event " + event.toString() + " - shop quantity is missing");
            return false;
        }

        if (event.getCount() == null) {
            LOGGER.info("Skipping event " + event.toString() + " - count is missing");
            return false;
        }

        if (event.getItem() == null || event.getItem().isEmpty()) {
            LOGGER.info("Skipping event " + event.toString() + " - item is missing");
            return false;
        }

        if (event.getFull() == null) {
            LOGGER.info("Skipping event " + event.toString() + " - 'full' indicator is missing");
            return false;
        }

        return true;
    }

    private ChestShopSign convert(ShopEvent event, HashMap<String, Player> players) {
        ChestShopSign chestShopSign = new ChestShopSign();
        chestShopSign.setId(event.getId());

        String world = event.getWorld();
        switch (world) {
            case "world":
                chestShopSign.setServer(Server.MAIN);
                break;
            case "rising_n":
                chestShopSign.setServer(Server.MAIN_NORTH);
                break;
            case "rising_e":
                chestShopSign.setServer(Server.MAIN_EAST);
                break;
        }

        Region region = regionService.findByCoordinates(event.getX(), event.getY(), event.getZ(), chestShopSign.getServer().name());
        if (region != null) {
            chestShopSign.setTown(region);
        }

        Location location = new Location();
        location.setX(event.getX());
        location.setY(event.getY());
        location.setZ(event.getZ());
        chestShopSign.setLocation(location);

        return convert(chestShopSign, event, players);
    }

    private ChestShopSign convert(ChestShopSign sign, ShopEvent event, HashMap<String, Player> players) {
        sign.setOwner(players.get(event.getOwner()));
        sign.setQuantity(event.getQuantity());
        sign.setCount(event.getCount());

        if (event.getSellPrice() != null && event.getSellPrice().doubleValue() != -1.0) {
            sign.setSellPrice(event.getSellPrice().doubleValue());
            sign.setSellPriceEach(determineSellPriceEach(event.getQuantity(), event.getSellPrice().doubleValue()));
            sign.setIsSellSign(Boolean.TRUE);
        } else {
            sign.setSellPrice(null); // Needed for overriding previous values
            sign.setSellPriceEach(null);
            sign.setIsSellSign(Boolean.FALSE);
        }

        if (event.getBuyPrice() != null && event.getBuyPrice().doubleValue() != -1.0) {
            sign.setBuyPrice(event.getBuyPrice().doubleValue());
            sign.setBuyPriceEach(determineBuyPriceEach(event.getQuantity(), event.getBuyPrice().doubleValue()));
            sign.setIsBuySign(Boolean.TRUE);
        } else {
            sign.setBuyPrice(null);
            sign.setBuyPriceEach(null);
            sign.setIsBuySign(Boolean.FALSE);
        }

        sign.setMaterial(event.getItem().toLowerCase());

        sign.setIsHidden(sign.getTown() == null || !sign.getTown().getActive());
        sign.setIsFull(event.getFull());
        sign.setIsSellSign(sign.getSellPrice() != null);

        return sign;
    }

    public List<String> getChestShopSignMaterialNames(Server server, TradeType tradeType) {
        if (tradeType == null) {
            return getChestShopSignMaterialNames(server);
        }

        if (server == null) {
            return getChestShopSignMaterialNames(tradeType);
        }

        if (tradeType == TradeType.BUY) {
            return chestShopSignRepository.findDistinctBuyMaterialsByServer(server.name());
        }

        return chestShopSignRepository.findDistinctSellMaterialsByServer(server.name());
    }

    public List<String> getChestShopSignMaterialNames(TradeType tradeType) {
        if (tradeType == null) {
            return chestShopSignRepository.findDistinctMaterials();
        } else if (tradeType == TradeType.BUY) {
            return chestShopSignRepository.findDistinctBuyMaterials();
        } else {
            return chestShopSignRepository.findDistinctSellMaterials();
        }
    }

    public List<String> getChestShopSignMaterialNames(Server server) {
        return server == null ? chestShopSignRepository.findDistinctMaterials() :
                chestShopSignRepository.findDistinctMaterialsByServer(server.name());
    }

    private List<ChestShopSignDto> mapSigns(List<ChestShopSign> signs) {
        return signs.stream().map(this::mapSignToDTO).collect(Collectors.toList());
    }

    private ChestShopSignDto mapSignToDTO(ChestShopSign sign) {
        ChestShopSignDto result = new ChestShopSignDto();
        result.setId(sign.getId());
        result.setTown(regionService.mapRegionResponse(sign.getTown()));
        result.setLocation(sign.getLocation());
        result.setOwner(playerService.mapPlayerResponse(sign.getOwner()));
        result.setQuantity(sign.getQuantity());
        result.setCount(sign.getCount());

        if (sign.getBuyPrice() != null) {
            result.setBuyPrice(sign.getBuyPrice());
        }
        if (sign.getSellPrice() != null) {
            result.setSellPrice(sign.getSellPrice());
        }

        result.setMaterial(sign.getMaterial());
        return result;
    }

    private Double determineSellPriceEach(Integer quantity, Double sellPrice) {
        return quantity == null || sellPrice == null ? null : sellPrice / quantity;
    }

    private Double determineBuyPriceEach(Integer quantity, Double buyPrice) {
        return quantity == null || buyPrice == null ? null : buyPrice / quantity;
    }

    private Sort getSort(SortBy sortBy, TradeType tradeType) {
        if (sortBy == SortBy.BEST_PRICE && tradeType == TradeType.BUY) return Sort.by("buyPriceEach").ascending();
        if (sortBy == SortBy.BEST_PRICE && tradeType == TradeType.SELL) return Sort.by("sellPriceEach").descending();
        if (sortBy == SortBy.QUANTITY) return Sort.by("quantity").descending();
        if (sortBy == SortBy.MATERIAL) return Sort.by("material").ascending();
        return Sort.by("count").descending();
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Autowired
    public void setRegionService(RegionService regionService) {
        this.regionService = regionService;
    }
}
