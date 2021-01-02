package com.shopdb.ecocitycraft.analytics.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class GetEventsParams {
    @Min(1)
    private int page;

    @Min(1) @Max(100)
    private int pageSize;

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "GetEventsParams{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
