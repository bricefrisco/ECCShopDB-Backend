package com.shopdb.ecocitycraft.analytics.models;

import java.util.List;

public class PaginatedEventResponse {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private List<EventDTO> results;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<EventDTO> getResults() {
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

    public void setResults(List<EventDTO> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "PaginatedEventResponse{" +
                "currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                ", results=" + results +
                '}';
    }
}
