package com.gylang.im.api.domain.common;

import lombok.Data;

import java.util.List;

/**
 * @author gylang
 * data 2021/3/7
 */
@Data
public class PageDTO<T> {

    private List<OrderItem> orderList;

    private List<T> records;

    private Long total;

    private Long size = 10L;

    private Long current = 1L;

    private T param;


    
    public List<OrderItem> orders() {
        return this.orderList;
    }

    
    public List<T> getRecords() {
        return this.records;
    }

    
    public PageDTO<T> setRecords(List records) {
        this.records = records;
        return this;
    }

    
    public long getTotal() {
        return this.total;
    }

    
    public PageDTO<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    
    public long getSize() {
        return this.size;
    }

    
    public PageDTO<T> setSize(long size) {
        this.size = size;
        return this;
    }

    
    public long getCurrent() {
        return this.current;
    }

    
    public PageDTO<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }
}
