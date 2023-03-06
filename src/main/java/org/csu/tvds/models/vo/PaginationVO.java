package org.csu.tvds.models.vo;

import lombok.Data;

@Data
public class PaginationVO<T> {
    private long currentPage;
    private long totalPage;
    private long pageSize;
    private T page;
}
