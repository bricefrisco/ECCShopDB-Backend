package com.shopdb.ecocitycraft.shopdb.models.regions;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedRegions {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private List<RegionResponse> results;
}
