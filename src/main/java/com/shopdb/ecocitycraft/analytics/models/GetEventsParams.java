package com.shopdb.ecocitycraft.analytics.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@ToString
public class GetEventsParams {
    @Min(1)
    private int page;

    @Min(1) @Max(100)
    private int pageSize;
}
