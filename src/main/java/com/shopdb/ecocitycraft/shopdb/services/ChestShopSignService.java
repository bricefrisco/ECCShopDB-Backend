package com.shopdb.ecocitycraft.shopdb.services;

import com.shopdb.ecocitycraft.shopdb.database.entities.ChestShopSign;
import com.shopdb.ecocitycraft.shopdb.database.entities.Player;
import com.shopdb.ecocitycraft.shopdb.database.entities.Region;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.SortBy;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import com.shopdb.ecocitycraft.shopdb.database.repositories.ChestShopSignRepository;
import com.shopdb.ecocitycraft.shopdb.database.repositories.ChestShopSignSpecification;
import com.shopdb.ecocitycraft.shopdb.models.exceptions.ErrorReasonConstants;
import com.shopdb.ecocitycraft.shopdb.models.signs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChestShopSignService implements ErrorReasonConstants, RegexConstants {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChestShopSignService.class);

    @Autowired
    private ChestShopSignRepository chestShopSignRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private RegionService regionService;

    public SignsResponse createSigns(SignsRequest request) {
        Region region = regionService.findRegionByServerAndName(request.getServer(), request.getRegionName());
        long numSignsRemoved = chestShopSignRepository.deleteByTown(region);

        List<Sign> signs = ChestShopSignValidator.getValidSigns(ChestShopSignValidator.getInBoundSigns(request.getSigns(), region));
        HashMap<String, Player> players = playerService.getOrAddPlayers(ChestShopSignValidator.getPlayerNames(signs));

        List<ChestShopSign> chestShopSigns = new ArrayList<>();
        for (Sign sign : signs) {
            Player player = players.get(sign.getNameLine().toLowerCase());
            chestShopSigns.add(mapDTOToSign(chestShopSigns, sign, player, region));
        }

        chestShopSignRepository.saveAll(chestShopSigns);
        regionService.setLastUpdatedToNow(region);

        return new SignsResponse(numSignsRemoved, chestShopSigns.size(), regionService.mapRegionResponse(region));
    }

    public PaginatedChestShopSigns getSigns(SignParams params) {
        Sort sort;

        if (params.getSortBy() == SortBy.BEST_PRICE) {
            sort = params.getTradeType() == TradeType.BUY ? Sort.by("buyPriceEach").ascending() : Sort.by("sellPriceEach").descending();
        } else if (params.getSortBy() == SortBy.QUANTITY){
            sort = Sort.by("quantity").descending();
        } else if (params.getSortBy() == SortBy.MATERIAL) {
            sort = Sort.by("material").ascending();
        } else {
            sort = Sort.by("count").descending();
        }

        Specification<ChestShopSign> specification = ChestShopSignSpecification.withSpecification(params);
        Pageable pageable = PageRequest.of(params.getPage() - 1, params.getPageSize(), sort);
        Page<ChestShopSign> pageableResults = chestShopSignRepository.findAll(specification, pageable);

        PaginatedChestShopSigns result = new PaginatedChestShopSigns();
        result.setCurrentPage(params.getPage());
        result.setTotalPages(pageableResults.getTotalPages());
        result.setTotalElements(pageableResults.getTotalElements());
        result.setResults(mapSigns(pageableResults.getContent()));

        return result;
    }

    public PaginatedChestShopSigns getSignsByRegion(Region region, TradeType tradeType, int page, int pageSize) {
        ChestShopSign example = new ChestShopSign();
        example.setTown(region);

        if (tradeType == TradeType.BUY) {
            example.setBuySign(true);
        }

        if (tradeType == TradeType.SELL) {
            example.setSellSign(true);
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("material").ascending());
        Page<ChestShopSign> pageableResults = chestShopSignRepository.findAll(Example.of(example), pageable);

        PaginatedChestShopSigns result = new PaginatedChestShopSigns();
        result.setCurrentPage(page);
        result.setTotalPages(pageableResults.getTotalPages());
        result.setTotalElements(pageableResults.getTotalElements());
        result.setResults(mapSigns(pageableResults.getContent()));

        return result;
    }

    public PaginatedChestShopSigns getSignsByPlayer(Player player, TradeType tradeType, int page, int pageSize) {
        ChestShopSign example = new ChestShopSign();
        example.setOwner(player);

        if (tradeType == TradeType.BUY) {
            example.setBuySign(true);
        }

        if (tradeType == TradeType.SELL) {
            example.setSellSign(true);
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("material").ascending());
        Page<ChestShopSign> pageableResults = chestShopSignRepository.findAll(Example.of(example), pageable);

        PaginatedChestShopSigns result = new PaginatedChestShopSigns();
        result.setCurrentPage(page);
        result.setTotalPages(pageableResults.getTotalPages());
        result.setTotalElements(pageableResults.getTotalElements());
        result.setResults(mapSigns(pageableResults.getContent()));

        return result;
    }

    public List<ChestShopSign> getSigns(Region town, TradeType tradeType, boolean filterIdenticalSigns) {
        ChestShopSign example = new ChestShopSign();
        example.setTown(town);

        if (tradeType == TradeType.BUY) {
            example.setBuySign(true);
        }

        if (tradeType == TradeType.SELL) {
            example.setSellSign(true);
        }

        if (filterIdenticalSigns) {
            example.setDistinct(true);
        }

        return chestShopSignRepository.findAll(Example.of(example));
    }

    public List<ChestShopSignDto> getSigns(Player owner, Server server, TradeType tradeType, boolean filterIdenticalSigns) {
        ChestShopSign example = new ChestShopSign();
        example.setOwner(owner);

        if (server != null) {
            example.setServer(server);
        }

        if (tradeType == TradeType.BUY) {
            example.setBuySign(true);
        }

        if (tradeType == TradeType.SELL) {
            example.setSellSign(true);
        }

        if (filterIdenticalSigns) {
            example.setDistinct(true);
        }

        return mapSigns(chestShopSignRepository.findAll(Example.of(example)));
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
        return server == null ? chestShopSignRepository.findDistinctMaterials() : chestShopSignRepository.findDistinctMaterialsByServer(server.name());
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

    private ChestShopSign mapDTOToSign(List<ChestShopSign> signs, Sign sign, Player owner, Region region) {
        ChestShopSign chestShopSign = new ChestShopSign();
        // Basic Info
        chestShopSign.setOwner(owner);
        chestShopSign.setTown(region);
        chestShopSign.setLocation(sign.getLocation());
        chestShopSign.setServer(region.getServer());

        // Quantity and Count
        QuantityLine quantityLine = parseQuantityLine(sign.getQuantityLine());
        chestShopSign.setQuantity(quantityLine.getQuantity());
        chestShopSign.setCount(quantityLine.getCount());

        // Material
        chestShopSign.setMaterial(sign.getMaterialLine().toLowerCase().replace("_", " "));

        // Buy Price / Sell Price
        Prices prices = ChestShopSignValidator.determinePrices(sign.getPriceLine());
        chestShopSign.setBuyPrice(prices.getBuyPrice());
        chestShopSign.setSellPrice(prices.getSellPrice());
        chestShopSign.setBuyPriceEach(determineBuyPriceEach(quantityLine.getQuantity(), prices.getBuyPrice()));
        chestShopSign.setSellPriceEach(determineSellPriceEach(quantityLine.getQuantity(), prices.getSellPrice()));

        // Is Buy Sign / Is Sell Sign / Is Unique
        chestShopSign.setBuySign(prices.getBuyPrice() != null);
        chestShopSign.setSellSign(prices.getSellPrice() != null);
        chestShopSign.setDistinct(!signs.contains(chestShopSign));

        return chestShopSign;
    }

    private QuantityLine parseQuantityLine(String quantityLine) {
        int quantity = Integer.parseInt(quantityLine.split(":")[0].replace("Q ", "").trim());
        int count = Integer.parseInt(quantityLine.split(":")[1].replace("C ", "").trim());
        return new QuantityLine(quantity, count);
    }

    private Double determineSellPriceEach(Integer quantity, Double sellPrice) {
        if (quantity == null || sellPrice == null) {
            return null;
        }

        return sellPrice / quantity;
    }

    private Double determineBuyPriceEach(Integer quantity, Double buyPrice) {
        if (quantity == null || buyPrice == null) {
            return null;
        }

        return buyPrice / quantity;
    }


}
