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
 * 待办列配置
 * </p>
 *
 * @author mycx26
 * @since 2020-07-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_to_do_col")
public class ToDoCol implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 流程定义key
     */
    private String procDefKey;

    /**
     * 查询表单类型code
     */
    private String formTypeCode;

    /**
     * 表名
     */
    private String tblName;

    /**
     * 列code
     */
    private String colCode;

    /**
     * 属性名
     */
    private String propName;

    /**
     * 列名
     */
    private String colName;

    /**
     * 列类型code
     */
    private String colTypeCode;

    /**
     * 枚举类型code
     */
    private String enumTypeCode;

    /**
     * 是否显示
     */
    @TableField("is_display")
    private Boolean display;

    /**
     * 顺序号
     */
    private Integer orderNo;

    /**
     * 结尾子句
     */
    private String endClause;

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
