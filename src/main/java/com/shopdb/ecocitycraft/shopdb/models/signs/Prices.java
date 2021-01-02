package com.shopdb.ecocitycraft.shopdb.models.signs;

public class Prices {
    private Double buyPrice;
    private Double sellPrice;

    public Prices(Double buyPrice, Double sellPrice) {
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    @Override
    public String toString() {
        return "Prices{" +
                "buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                '}';
    }
}
