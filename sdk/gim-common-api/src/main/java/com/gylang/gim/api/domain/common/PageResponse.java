package com.gylang.gim.api.domain.common;

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





    

}
