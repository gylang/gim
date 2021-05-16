package com.gylang.gim.web.common.mybatis;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.api.R;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.web.common.util.MappingUtil;
import lombok.Data;

import java.util.List;
import java.util.function.Function;

/**
 * @author gylang
 * data 2021/3/7
 */
@Data
public class Page<T> implements IPage<T> {

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
    public Page<T> setRecords(List records) {
        this.records = records;
        return this;
    }


    @Override
    public long getTotal() {
        return this.total;
    }


    @Override
    public Page<T> setTotal(long total) {
        this.total = total;
        return this;
    }


    @Override
    public long getSize() {
        return this.size;
    }


    @Override
    public Page<T> setSize(long size) {
        this.size = size;
        return this;
    }


    @Override
    public long getCurrent() {
        return this.current;
    }


    @Override
    public Page<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }

    public <S> PageResponse<S> toDTO(Class<S> clazz) {
        PageResponse<S> pageResponse = new PageResponse<>();
        pageResponse.setCurrent(this.getCurrent());
        pageResponse.setRecords(MappingUtil.mapAsList(this.getRecords(), clazz));
        pageResponse.setSize(this.getSize());
        pageResponse.setTotal(this.getTotal());
        pageResponse.setPages(this.getPages());
        return pageResponse;
    }

    public <R> Page<R> converterQuery(Function<T, R> func) {
        Page<R> page = new Page<>();
        page.setCurrent(this.getCurrent());
        if (null != this.getParam()) {
            page.setParam(func.apply(this.getParam()));
        }
        page.setSize(this.getSize());
        page.setTotal(this.getTotal());
        page.setPages(this.getPages());
        page.setOrderList(this.getOrderList());
        return page;
    }

    public <R> Page<R> converterQuery(Class<R> clazz) {
        return converterQuery(p -> MappingUtil.map(p, clazz));
    }
}
