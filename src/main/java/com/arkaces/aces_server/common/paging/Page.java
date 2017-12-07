package com.arkaces.aces_server.common.paging;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
    private Integer pageSize;
    private Integer page;
    private String continuation;
    private List<T> items;
}
