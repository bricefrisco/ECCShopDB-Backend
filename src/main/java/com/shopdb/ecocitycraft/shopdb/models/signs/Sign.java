package com.shopdb.ecocitycraft.shopdb.models.signs;

import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;

import javax.validation.constraints.NotNull;

public class Sign {

    @NotNull
    private Location location;

    @NotNull
    private String nameLine;

    @NotNull
    private String quantityLine;

    @NotNull
    private String priceLine;

    @NotNull
    private String materialLine;

    public Location getLocation() {
        return location;
    }

    public String getNameLine() {
        return nameLine;
    }

    public String getQuantityLine() {
        return quantityLine;
    }

    public String getPriceLine() {
        return priceLine;
    }

    public String getMaterialLine() {
        return materialLine;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setNameLine(String nameLine) {
        this.nameLine = nameLine;
    }

    public void setQuantityLine(String quantityLine) {
        this.quantityLine = quantityLine;
    }

    public void setPriceLine(String priceLine) {
        this.priceLine = priceLine;
    }

    public void setMaterialLine(String materialLine) {
        this.materialLine = materialLine;
    }

    @Override
    public String toString() {
        return "Sign{" +
                "location=" + location +
                ", nameLine='" + nameLine + '\'' +
                ", quantityLine='" + quantityLine + '\'' +
                ", priceLine='" + priceLine + '\'' +
                ", materialLine='" + materialLine + '\'' +
                '}';
    }
}
