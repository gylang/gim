package com.gylang.gim.web.common.mybatis;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Data;

import java.util.List;

/**
 * @author gylang
 * data 2021/3/7
 */
@Data
public class PageDTO<T> implements IPage<T> {

    private List<OrderItem> orderList;

    private List<T> records;

    private Long total;

    private Long size = 10L;

    private Long current = 1L;

    private T param;


    @Override
    public List<OrderItem> orders() {
        return this.orderList;
    }


    @Override
    public List<T> getRecords() {
        return this.records;
    }


    @Override
    public PageDTO<T> setRecords(List records) {
        this.records = records;
        return this;
    }


    @Override
    public long getTotal() {
        return this.total;
    }


    @Override
    public PageDTO<T> setTotal(long total) {
        this.total = total;
        return this;
    }


    @Override
    public long getSize() {
        return this.size;
    }


    @Override
    public PageDTO<T> setSize(long size) {
        this.size = size;
        return this;
    }


    @Override
    public long getCurrent() {
        return this.current;
    }


    @Override
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
