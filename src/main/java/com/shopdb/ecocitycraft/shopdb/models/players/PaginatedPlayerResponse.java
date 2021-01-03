package com.shopdb.ecocitycraft.shopdb.models.players;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PaginatedPlayerResponse {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private List<PlayerResponse> results;
}
