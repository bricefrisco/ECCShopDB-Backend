package com.shopdb.ecocitycraft.shopdb.models.players;

import java.util.List;

public class PaginatedPlayerResponse {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private List<PlayerResponse> results;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<PlayerResponse> getResults() {
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

    public void setResults(List<PlayerResponse> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "PaginatedPlayerResponse{" +
                "currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                ", results=" + results +
                '}';
    }
}
