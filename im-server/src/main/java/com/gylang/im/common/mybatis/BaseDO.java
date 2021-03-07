package com.gylang.im.common.mybatis;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 实体类基本数据
 * @author gylang
 * data 2020/9/2
 * @version v0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDelete;
}
