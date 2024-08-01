package com.liveCode.todoList.utils;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class CustomPage<T> {
    private List<T> items;
    private long totalItems;
    private int currentPage;
    private int totalPages;

    public CustomPage(Page<T> page) {
        this.items = page.getContent();
        this.totalItems = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.totalPages = page.getTotalPages();
    }
}


