package com.shopdb.ecocitycraft.shopdb.controllers;

import com.shopdb.ecocitycraft.analytics.services.EventService;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.TradeType;
import com.shopdb.ecocitycraft.shopdb.models.signs.PaginatedChestShopSigns;
import com.shopdb.ecocitycraft.shopdb.models.signs.SignParams;
import com.shopdb.ecocitycraft.shopdb.models.signs.SignsRequest;
import com.shopdb.ecocitycraft.shopdb.models.signs.SignsResponse;
import com.shopdb.ecocitycraft.shopdb.services.ChestShopSignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/chest-shops")
public class ChestShopSignController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChestShopSignController.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private ChestShopSignService chestShopSignService;

    @GetMapping
    public PaginatedChestShopSigns getChestShopSigns(@Valid @ModelAttribute SignParams signParams) {
        PaginatedChestShopSigns response = chestShopSignService.getSigns(signParams);
        if (!response.getResults().isEmpty()) {
            eventService.sendChestShopSearchAnalytics(signParams);
        }
        return response;
    }

    @GetMapping("/material-names")
    public List<String> getChestShopSignMaterialNames(@RequestParam(required = false) Server server,
                                                      @RequestParam(required = false) TradeType tradeType) {
        return chestShopSignService.getChestShopSignMaterialNames(server, tradeType);
    }

    @PostMapping
    private SignsResponse createChestShopSigns(@Valid @RequestBody SignsRequest request) {
        return chestShopSignService.createSigns(request);
    }
}
