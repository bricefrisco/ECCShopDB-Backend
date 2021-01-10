package com.shopdb.ecocitycraft.shopdb.models.players;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedPlayerResponse {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private List<PlayerResponse> results;
}
