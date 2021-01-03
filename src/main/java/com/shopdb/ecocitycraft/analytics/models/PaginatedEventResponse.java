package com.shopdb.ecocitycraft.analytics.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PaginatedEventResponse {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private List<EventDTO> results;
}
