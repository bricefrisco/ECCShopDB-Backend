package com.shopdb.ecocitycraft.shopdb.models.regions;

import java.util.List;

public class PaginatedRegions {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private List<RegionResponse> results;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<RegionResponse> getResults() {
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

    public void setResults(List<RegionResponse> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "PaginatedRegions{" +
                "currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                ", results=" + results +
                '}';
    }
}
