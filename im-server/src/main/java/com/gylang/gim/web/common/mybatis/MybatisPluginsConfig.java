package com.gylang.gim.web.common.mybatis;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gylang
 * data 2020/9/6
 * @version v0.0.1
 */
@Configuration
public class MybatisPluginsConfig {

    @Value("${mybatis.pagehelper.overflow:false}")
    private boolean overflow;
    @Value("${mybatis.pagehelper.limit:200}")
    private int limit;
    @Value("${mybatis.pagehelper.countSqlParser:true}")
    private boolean countSqlParser;
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setOverflow(overflow);
        paginationInterceptor.setLimit(limit);
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(countSqlParser));
        return paginationInterceptor;
    }


}
