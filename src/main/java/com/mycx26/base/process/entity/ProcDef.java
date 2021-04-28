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
 * 流程定义
 * </p>
 *
 * @author mycx26
 * @since 2020-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_proc_def")
public class ProcDef implements Serializable {

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
     * 流程定义名称
     */
    private String procDefName;

    /**
     * 主表
     */
    private String mainForm;

    /**
     * 子表
     */
    private String subForm;

    /**
     * 流水号前缀
     */
    private String flowNoPrefix;

    /**
     * 是否内部流程
     */
    @TableField("is_internal")
    private Boolean internal;

    /**
     * 详情视图key
     */
    private String detailViewKey;

    /**
     * 是否锁定资源
     */
    @TableField("is_lock_resource")
    private Boolean lockResource;

    /**
     * 锁定资源key
     */
    private String resourceKey;

    /**
     * 描述
     */
    private String description;

    /**
     * 引擎key
     */
    private String engineKey;

    /**
     * 引擎id
     */
    private String engineId;

    /**
     * 顺序号
     */
    private Integer orderNo;

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
