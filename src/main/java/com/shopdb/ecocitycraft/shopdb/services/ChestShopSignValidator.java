package com.shopdb.ecocitycraft.shopdb.services;

import com.shopdb.ecocitycraft.shopdb.database.entities.Region;
import com.shopdb.ecocitycraft.shopdb.models.signs.Prices;
import com.shopdb.ecocitycraft.shopdb.models.signs.RegexConstants;
import com.shopdb.ecocitycraft.shopdb.models.signs.Sign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public final class ChestShopSignValidator implements RegexConstants {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChestShopSignValidator.class);

    public static Prices parseBuyAndSellSign(String priceLine) {
        Matcher matcher = BUY_SELL_LINE_PATTERN.matcher(priceLine);
        matcher.matches();

        double buyPrice = matcher.group(1).equals("S") ?
                Double.parseDouble(matcher.group(4)) :
                Double.parseDouble(matcher.group(2));

        double sellPrice = matcher.group(1).equals("S") ?
                Double.parseDouble(matcher.group(2)) :
                Double.parseDouble(matcher.group(4));

        return new Prices(buyPrice == 0 ? null : buyPrice, sellPrice == 0 ? null : sellPrice);
    }

    public static Prices determinePrices(String priceLine) {
        if (isBuyAndSellLine(priceLine)) {
            return parseBuyAndSellSign(priceLine);
        } else if (isBuyLine(priceLine)) {
            return parseBuySign(priceLine);
        } else if (isSellLine(priceLine)) {
            return parseSellSign(priceLine);
        }

        return new Prices(null, null);
    }

    private static Prices parseBuySign(String priceLine) {
        Matcher matcher = BUY_LINE_PATTERN.matcher(priceLine);
        matcher.matches();

        double buyPrice = Double.parseDouble(matcher.group(1));
        return new Prices(buyPrice, null);
    }

    private static Prices parseSellSign(String priceLine) {
        Matcher matcher = SELL_LINE_PATTERN.matcher(priceLine);
        matcher.matches();

        double sellPrice = Double.parseDouble(matcher.group(1));
        return new Prices(null, sellPrice);
    }

    public static List<Sign> getInBoundSigns(List<Sign> signs, Region region) {
        return signs.stream().filter(sign -> isInBounds(sign, region)).collect(Collectors.toList());
    }

    public static List<Sign> getValidSigns(List<Sign> signs) {
        return signs.stream().filter(ChestShopSignValidator::isChestShopSign).collect(Collectors.toList());
    }

    public static Set<String> getPlayerNames(List<Sign> signs) {
        Set<String> playerNames = new HashSet<>();
        for (Sign sign : signs) {
            playerNames.add(sign.getNameLine().toLowerCase());
        }
        return playerNames;
    }

    private static boolean isInBounds(Sign sign, Region region) {
        return isBetween(region.getiBounds().getX(), region.getoBounds().getX(), sign.getLocation().getX()) &&
                isBetween(region.getiBounds().getY(), region.getoBounds().getY(), sign.getLocation().getY()) &&
                isBetween(region.getiBounds().getZ(), region.getoBounds().getZ(), sign.getLocation().getZ());
    }

    private static boolean isBetween(int s1, int s2, int x) {
        return x <= Math.max(s1, s2) && x >= Math.min(s1, s2);
    }

    private static boolean isBuyAndSellLine(String priceLine) {
        return BUY_SELL_LINE_PATTERN.matcher(priceLine).matches();
    }

    private static boolean isBuyLine(String priceLine) {
        return BUY_LINE_PATTERN.matcher(priceLine).matches();
    }

    private static boolean isSellLine(String priceLine) {
        return SELL_LINE_PATTERN.matcher(priceLine).matches();
    }

    private static boolean isChestShopSign(Sign sign) {
        return couldBeChestShopSign(sign) &&
                (BUY_SELL_LINE_PATTERN.matcher(sign.getPriceLine()).matches() ||
                        BUY_LINE_PATTERN.matcher(sign.getPriceLine()).matches() ||
                        SELL_LINE_PATTERN.matcher(sign.getPriceLine()).matches());

    }

    private static boolean couldBeChestShopSign(Sign sign) {
        return NAME_LINE_PATTERN.matcher(sign.getNameLine()).matches() &&
                QUANTITY_LINE_PATTERN.matcher(sign.getQuantityLine()).matches() &&
                PRICE_LINE_PATTERN.matcher(sign.getPriceLine()).matches() &&
                MATERIAL_LINE_PATTERN.matcher(sign.getMaterialLine()).matches();
    }
}
