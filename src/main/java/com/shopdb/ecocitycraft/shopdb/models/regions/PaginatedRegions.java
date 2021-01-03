package com.shopdb.ecocitycraft.shopdb.models.regions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PaginatedRegions {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private List<RegionResponse> results;
}
