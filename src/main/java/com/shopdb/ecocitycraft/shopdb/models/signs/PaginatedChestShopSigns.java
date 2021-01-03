package com.shopdb.ecocitycraft.shopdb.models.signs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PaginatedChestShopSigns {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private List<ChestShopSignDto> results;
}
