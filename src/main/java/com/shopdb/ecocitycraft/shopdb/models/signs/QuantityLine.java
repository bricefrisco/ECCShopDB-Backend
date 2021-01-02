package com.shopdb.ecocitycraft.shopdb.models.signs;

public class QuantityLine {
    private int quantity;
    private int count;

    public QuantityLine(int quantity, int count) {
        this.quantity = quantity;
        this.count = count;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCount() {
        return count;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
