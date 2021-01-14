package com.shopdb.ecocitycraft.shopdb.models.signs;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedChestShopSigns {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private List<ChestShopSignDto> results;
}
