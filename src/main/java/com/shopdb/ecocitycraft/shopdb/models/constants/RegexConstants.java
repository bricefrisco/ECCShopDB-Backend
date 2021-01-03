package com.shopdb.ecocitycraft.shopdb.models.constants;

import java.util.regex.Pattern;

public interface RegexConstants {
    Pattern NAME_LINE_PATTERN = Pattern.compile("^[\\w]+$");
    Pattern QUANTITY_LINE_PATTERN = Pattern.compile("^Q [1-9][0-9]{0,4} : C [0-9]{0,5}$");
    Pattern PRICE_LINE_PATTERN = Pattern.compile("^[\\d.BS :]+$");
    Pattern MATERIAL_LINE_PATTERN = Pattern.compile("^[\\w _#:-]+$");
    Pattern BUY_SELL_LINE_PATTERN = Pattern.compile("^([BS])\\s?([0-9.]+)\\s?:\\s?([BS])\\s?([0-9.]+)$");
    Pattern BUY_LINE_PATTERN = Pattern.compile("^B\\s?([0-9.]+)$");
    Pattern SELL_LINE_PATTERN = Pattern.compile("^S\\s?([0-9.]+)$");
}
