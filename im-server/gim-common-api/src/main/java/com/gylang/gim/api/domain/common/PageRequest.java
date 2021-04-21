package com.gylang.gim.api.domain.common;

import lombok.Data;

import java.util.List;

/**
 * @author gylang
 * data 2021/3/7
 */
@Data
public class PageRequest<T> {

    private List<OrderItem> orderList;

    private transient List<T> records;


    private Long size = 10L;

    private Long current = 1L;

    private transient T param;


}
