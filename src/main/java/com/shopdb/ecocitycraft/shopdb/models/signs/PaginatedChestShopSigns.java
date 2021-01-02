package com.shopdb.ecocitycraft.shopdb.models.signs;

import java.util.List;

public class PaginatedChestShopSigns {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private List<ChestShopSignDto> results;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<ChestShopSignDto> getResults() {
        return results;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public void setResults(List<ChestShopSignDto> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "PaginatedChestShopSigns{" +
                "currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                ", results=" + results +
                '}';
    }
}
