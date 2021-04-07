package com.gylang.gim.web.api.domain.common;

import lombok.Data;

import java.util.List;

/**
 * @author gylang
 * data 2021/3/7
 */
@Data
public class PageResponse<T> {


    private List<T> records;

    private Long total;

    private Long size = 10L;

    private Long current = 1L;

    private Long pages;





    
    public List<T> getRecords() {
        return this.records;
    }


    
    public PageResponse<T> setRecords(List records) {
        this.records = records;
        return this;
    }


    
    public long getTotal() {
        return this.total;
    }


    
    public PageResponse<T> setTotal(long total) {
        this.total = total;
        return this;
    }


    
    public long getSize() {
        return this.size;
    }


    
    public PageResponse<T> setSize(long size) {
        this.size = size;
        return this;
    }


    
    public long getCurrent() {
        return this.current;
    }


    
    public PageResponse<T> setCurrent(long current) {
        this.current = current;
        return this;
    }


}
