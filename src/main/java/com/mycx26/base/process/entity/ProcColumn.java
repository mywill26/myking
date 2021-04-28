package com.mycx26.base.process.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 流程列配置
 * </p>
 *
 * @author mycx26
 * @since 2020-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_proc_column")
public class ProcColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 表名
     */
    private String tblName;

    /**
     * 列名
     */
    private String colName;

    /**
     * 属性名
     */
    private String propName;

    /**
     * 是否必填
     */
    @TableField("is_required")
    private Boolean required;

    /**
     * 是否新增
     */
    @TableField("is_added")
    private Boolean added;

    /**
     * 创建人id
     */
    private String creatorId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人id
     */
    private String modifierId;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 是否可用
     */
    private Boolean yn;

}
